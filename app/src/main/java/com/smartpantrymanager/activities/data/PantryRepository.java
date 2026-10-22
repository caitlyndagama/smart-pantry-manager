package com.smartpantrymanager.activities.data;

import android.util.Log;

import com.smartpantrymanager.activities.logic.IngredientNormaliser;
import com.smartpantrymanager.activities.logic.UnitConverter;
import com.smartpantrymanager.activities.model.Ingredient;
import com.smartpantrymanager.activities.model.PantryItem;
import com.smartpantrymanager.activities.model.Recipe;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class PantryRepository {
    private static final String TAG = "PantryRepository";
    public interface DataCallback<T> {
        void onSuccess(T result);
        void onError(String message);
    }
    private final SupaBaseAPI api = APIClient.getApi();

    public void getPantryItems(DataCallback<List<PantryItem>> callback) {
        enqueue(api.getPantryItems(), callback);
    }
    public void getPantryItem(int pantryItemId, DataCallback<PantryItem> callback) {
        enqueueFirst(api.getPantryItem("eq." + pantryItemId), callback);
    }
    public void getRecipes(DataCallback<List<Recipe>> callback) {
        enqueue(api.getRecipes(), callback);
    }
    public void getRecipe(int recipeId, DataCallback<Recipe> callback) {
        enqueueFirst(api.getRecipe("eq." + recipeId), callback);
    }

    public void savePantryItem(Integer pantryItemId, String typedName, double quantity,
                               String unit, String expiryDate,
                               DataCallback<PantryItem> callback) {
        final String canonicalName = IngredientNormaliser.normalise(typedName);
        final String baseUnit = UnitConverter.baseUnitOf(unit);
        final double baseQuantity = UnitConverter.toBase(quantity, unit);
        findOrCreateIngredient(canonicalName, baseUnit, new DataCallback<Ingredient>() {
            @Override
            public void onSuccess(Ingredient ingredient) {
// e.g. eggs are counted in pcs, so "200 g of eggs" is rejected
                if (!baseUnit.equals(ingredient.getBaseUnit())) {
                    callback.onError(capitalise(canonicalName) + " is measured by "
                            + UnitConverter.describeBaseUnit(ingredient.getBaseUnit())
                            + ". Choose a matching unit.");
                    return;
                }
                Map<String, Object> body = new HashMap<>();
                body.put("ingredient_id", ingredient.getId());
                body.put("display_name", typedName.trim());
                body.put("quantity", baseQuantity);
                body.put("expiry_date", expiryDate); // null clears the date
                if (pantryItemId == null) {
                    enqueueFirst(api.createPantryItem(body), callback);
                } else {
                    enqueueFirst(api.updatePantryItem("eq." + pantryItemId, body), callback);
                }
            }
            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    private void findOrCreateIngredient(String canonicalName, String baseUnit,
                                        DataCallback<Ingredient> callback) {
        enqueue(api.findIngredient("eq." + canonicalName), new DataCallback<List<Ingredient>>() {
            @Override
            public void onSuccess(List<Ingredient> found) {
                if (found != null && !found.isEmpty()) {
                    callback.onSuccess(found.get(0));
                    return;
                }
                Map<String, Object> body = new HashMap<>();
                body.put("canonical_name", canonicalName);
                body.put("base_unit", baseUnit);
                enqueueFirst(api.createIngredient(body), callback);
            }
            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void deletePantryItem(int pantryItemId, DataCallback<Void> callback) {
        enqueue(api.deletePantryItem("eq." + pantryItemId), callback);
    }

    private <T> void enqueue(Call<T> call, DataCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "HTTP " + response.code() + " for " + call.request().url());
                    callback.onError(messageForStatus(response.code()));
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e(TAG, "Network failure", t);
                callback.onError("Can't reach the database. Check your internet connection and try again.");
            }
        });
    }

    private <T> void enqueueFirst(Call<List<T>> call, DataCallback<T> callback) {
        enqueue(call, new DataCallback<List<T>>() {
            @Override
            public void onSuccess(List<T> rows) {
                if (rows == null || rows.isEmpty()) {
                    callback.onError("That item no longer exists.");
                } else {
                    callback.onSuccess(rows.get(0));
                }
            }
            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
    private static String messageForStatus(int code) {
        switch (code) {
            case 409:
                return "That ingredient is already in your pantry. Edit the existing item.";
            case 401:
                return "Unauthorized. Request lacked valid authentication credentials.";
            case 403:
                return "The app isn't allowed to access the database. Check the Supabase key and table permissions.";
            case 404:
                return "The database table wasn't found.";
            default:
                return "Something went wrong (error " + code + "). Please try again.";
        }
    }
    private static String capitalise(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.substring(0, 1).toUpperCase(Locale.ROOT) + text.substring(1);
    }
}
