package com.smartpantrymanager.activities.data;

import com.smartpantrymanager.activities.model.Ingredient;
import com.smartpantrymanager.activities.model.PantryItem;
import com.smartpantrymanager.activities.model.Recipe;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
public abstract class SupaBaseAPI {
    @GET("rest/v1/pantry_items?select=*,ingredients(*)&order=display_name.asc")
    abstract Call<List<PantryItem>> getPantryItems();

    @GET("rest/v1/pantry_items?select=*,ingredients(*)")
    abstract Call<List<PantryItem>> getPantryItem(@Query("pantry_item_id") String idFilter);

    @Headers("Prefer: return=representation")
    @POST("rest/v1/pantry_items?select=*,ingredients(*)")
    abstract Call<List<PantryItem>> createPantryItem(@Body Map<String, Object> body);

    @Headers("Prefer: return=representation")
    @PATCH("rest/v1/pantry_items?select=*,ingredients(*)")
    abstract Call<List<PantryItem>> updatePantryItem(@Query("pantry_item_id") String idFilter,
                                                     @Body Map<String, Object> body);

    @DELETE("rest/v1/pantry_items")
    abstract Call<Void> deletePantryItem(@Query("pantry_item_id") String idFilter);

    @GET("rest/v1/ingredients")
    abstract Call<List<Ingredient>> findIngredient(@Query("canonical_name") String nameFilter);

    @Headers("Prefer: return=representation")
    @POST("rest/v1/ingredients")
    abstract Call<List<Ingredient>> createIngredient(@Body Map<String, Object> body);

    @GET("rest/v1/recipes?select=*,recipe_ingredients(ingredient_id,quantity,ingredients(*))&order=name.asc")
    abstract Call<List<Recipe>> getRecipes();

    @GET("rest/v1/recipes?select=*,recipe_ingredients(ingredient_id,quantity,ingredients(*))")
    abstract Call<List<Recipe>> getRecipe(@Query("recipe_id") String idFilter);
}