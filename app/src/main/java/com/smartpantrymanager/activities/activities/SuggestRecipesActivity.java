package com.smartpantrymanager.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smartpantrymanager.R;
import com.smartpantrymanager.activities.adapters.RecipeAdapter;
import com.smartpantrymanager.activities.data.PantryRepository;
import com.smartpantrymanager.activities.data.SettingsManager;
import com.smartpantrymanager.activities.logic.RecipeMatcher;
import com.smartpantrymanager.activities.model.PantryItem;
import com.smartpantrymanager.activities.model.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
public class SuggestRecipesActivity extends AppCompatActivity {
    private static final String TAG = "SuggestedRecipes";
    private NestedScrollView scrollRecipes;
    private ProgressBar progressRecipes;
    private LinearLayout layoutRecipesError;
    private TextView textRecipesError;
    private TextView textMatchSummary;
    private RecyclerView recyclerSuggested;
    private LinearLayout layoutRecipesEmpty;
    private LinearLayout layoutAlmostThere;
    private RecipeAdapter suggestedAdapter;
    private RecipeAdapter almostThereAdapter;
    private PantryRepository repository;
    private SettingsManager settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);
        ScreenHelper.applySystemBarInsets(findViewById(R.id.main));
        repository = new PantryRepository();
        settings = new SettingsManager(this);
        scrollRecipes = findViewById(R.id.scrollRecipes);
        progressRecipes = findViewById(R.id.progressRecipes);
        layoutRecipesError = findViewById(R.id.layoutRecipesError);
        textRecipesError = findViewById(R.id.textRecipesError);
        textMatchSummary = findViewById(R.id.textMatchSummary);
        recyclerSuggested = findViewById(R.id.recyclerSuggested);
        layoutRecipesEmpty = findViewById(R.id.layoutRecipesEmpty);
        layoutAlmostThere = findViewById(R.id.layoutAlmostThere);
        RecyclerView recyclerAlmostThere = findViewById(R.id.recyclerAlmostThere);

        suggestedAdapter = new RecipeAdapter(false, this::openRecipe);
        recyclerSuggested.setLayoutManager(new LinearLayoutManager(this));
        recyclerSuggested.setAdapter(suggestedAdapter);
        almostThereAdapter = new RecipeAdapter(true, this::openRecipe);
        recyclerAlmostThere.setLayoutManager(new LinearLayoutManager(this));
        recyclerAlmostThere.setAdapter(almostThereAdapter);
        findViewById(R.id.buttonRecipesRetry).setOnClickListener(v -> loadSuggestions());
        findViewById(R.id.buttonAddIngredients).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditIngredientActivity.class)));
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        ScreenHelper.setUpBottomNav(this, bottomNav, R.id.nav_recipes);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestions();
    }
    private void loadSuggestions() {
        progressRecipes.setVisibility(View.VISIBLE);
        layoutRecipesError.setVisibility(View.GONE);
        scrollRecipes.setVisibility(View.GONE);
        repository.getPantryItems(new PantryRepository.DataCallback<List<PantryItem>>() {
            @Override
            public void onSuccess(List<PantryItem> pantryItems) {
                repository.getRecipes(new PantryRepository.DataCallback<List<Recipe>>() {
                    @Override
                    public void onSuccess(List<Recipe> recipes) {
                        if (isDestroyed()) return;
                        showResults(pantryItems, recipes);
                    }
                    @Override
                    public void onError(String message) {
                        showError(message);
                    }
                });
            }
            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }
    private void showResults(List<PantryItem> pantryItems, List<Recipe> recipes) {
        Map<Integer, Double> pantry = RecipeMatcher.buildPantryMap(
                pantryItems, settings.isExcludeExpired(), Calendar.getInstance());
        List<Recipe> matches = RecipeMatcher.findStrictMatches(recipes, pantry);
        List<Recipe> almostThere = RecipeMatcher.findAlmostThere(recipes, pantry);
        Log.d(TAG, pantry.size() + " usable pantry items -> " + matches.size()
                + " strict matches, " + almostThere.size() + " almost there");
        textMatchSummary.setText(getResources().getQuantityString(
                R.plurals.recipes_match, matches.size(), matches.size()));
        suggestedAdapter.setRecipes(matches, pantry);
        almostThereAdapter.setRecipes(almostThere, pantry);

        boolean noMatches = matches.isEmpty();
        layoutRecipesEmpty.setVisibility(noMatches ? View.VISIBLE : View.GONE);
        recyclerSuggested.setVisibility(noMatches ? View.GONE : View.VISIBLE);
        layoutAlmostThere.setVisibility(almostThere.isEmpty() ? View.GONE : View.VISIBLE);
        progressRecipes.setVisibility(View.GONE);
        scrollRecipes.setVisibility(View.VISIBLE);
    }
    private void showError(String message) {
        if (isDestroyed()) return;
        progressRecipes.setVisibility(View.GONE);
        textRecipesError.setText(message);
        layoutRecipesError.setVisibility(View.VISIBLE);
    }
    private void openRecipe(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}