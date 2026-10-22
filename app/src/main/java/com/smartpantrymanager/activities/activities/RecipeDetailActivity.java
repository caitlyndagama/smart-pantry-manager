package com.smartpantrymanager.activities.activities;

import static android.provider.Settings.System.getString;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import com.smartpantrymanager.R;
import com.smartpantrymanager.activities.data.PantryRepository;
import com.smartpantrymanager.activities.data.SettingsManager;
import com.smartpantrymanager.activities.logic.RecipeMatcher;
import com.smartpantrymanager.activities.logic.UnitConverter;
import com.smartpantrymanager.activities.model.PantryItem;
import com.smartpantrymanager.activities.model.Recipe;
import com.smartpantrymanager.activities.model.RecipeIngredient;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeDetailActivity extends AppCompatActivity{
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";
    private MaterialToolbar toolbar;
    private NestedScrollView scrollDetail;
    private ProgressBar progressDetail;
    private TextView textDetailError;
    private TextView textDetailDescription;
    private TextView textDetailPrepTime;
    private TextView textDetailSteps;
    private LinearLayout containerDetailIngredients;
    private final PantryRepository repository = new PantryRepository();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ScreenHelper.applySystemBarInsets(findViewById(R.id.main));
        toolbar = findViewById(R.id.toolbarDetail);
        scrollDetail = findViewById(R.id.scrollDetail);
        progressDetail = findViewById(R.id.progressDetail);
        textDetailError = findViewById(R.id.textDetailError);
        textDetailDescription = findViewById(R.id.textDetailDescription);
        textDetailPrepTime = findViewById(R.id.textDetailPrepTime);
        textDetailSteps = findViewById(R.id.textDetailSteps);
        containerDetailIngredients = findViewById(R.id.containerDetailIngredients);
        toolbar.setNavigationOnClickListener(v -> finish());

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        if (recipeId == -1) {
            finish();
            return;
        }
        loadRecipe(recipeId);
    }
    private void loadRecipe(int recipeId) {
        repository.getRecipe(recipeId, new PantryRepository.DataCallback<Recipe>() {
            @Override
            public void onSuccess(Recipe recipe) {
                repository.getPantryItems(new PantryRepository.DataCallback<List<PantryItem>>() {
                    @Override
                    public void onSuccess(List<PantryItem> items) {
                        if (isDestroyed()) return;
                        boolean excludeExpired = new SettingsManager(RecipeDetailActivity.this).isExcludeExpired();
                        showRecipe(recipe, RecipeMatcher.buildPantryMap(items, excludeExpired, Calendar.getInstance()));
                    }
                    @Override
                    public void onError(String message) {
                        if (isDestroyed()) return;
                        showRecipe(recipe, new HashMap<>());
                    }
                });
            }
            @Override
            public void onError(String message) {
                if (isDestroyed()) return;
                progressDetail.setVisibility(View.GONE);
                textDetailError.setText(message);
                textDetailError.setVisibility(View.VISIBLE);
            }
        });
    }
    private void showRecipe(Recipe recipe, Map<Integer, Double> pantry) {
        toolbar.setTitle(recipe.getName());
        textDetailDescription.setText(recipe.getDescription());
        if (recipe.getPrepMinutes() != null) {
            textDetailPrepTime.setText(getString(R.string.prep_time, recipe.getPrepMinutes()));
        }

        containerDetailIngredients.removeAllViews();
        List<RecipeIngredient> missing = RecipeMatcher.missingIngredients(recipe, pantry);
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            String needed = UnitConverter.format(ingredient.getQuantity(), ingredient.getBaseUnit());
            Double have = pantry.get(ingredient.getIngredientId());
            boolean enough = !missing.contains(ingredient); // same rule as the matcher
            TextView row = new TextView(this);
            row.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            row.setPadding(0, 8, 0, 8);
            if (enough) {
                row.setText(getString(R.string.have_amount, ingredient.getName(), needed,
                        UnitConverter.format(have, ingredient.getBaseUnit())));
                row.setTextColor(ContextCompat.getColor(this, R.color.pantry_green_dark));
            } else {
                row.setText(getString(R.string.missing_amount, ingredient.getName(), needed));
                row.setTextColor(ContextCompat.getColor(this, R.color.alert_red));
            }
            containerDetailIngredients.addView(row);
        }

        StringBuilder steps = new StringBuilder();
        String[] lines = recipe.getSteps() == null ? new String[0] : recipe.getSteps().split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                steps.append("\n");
            }
            steps.append(i + 1).append(". ").append(lines[i].trim());
        }
        textDetailSteps.setText(steps.toString());
        progressDetail.setVisibility(View.GONE);
        scrollDetail.setVisibility(View.VISIBLE);
    }
}