package com.smartpantrymanager.activities.logic;

import com.smartpantrymanager.activities.model.PantryItem;
import com.smartpantrymanager.activities.model.Recipe;
import com.smartpantrymanager.activities.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RecipeMatcher {

    private static final double EPSILON = 0.0001;

    private RecipeMatcher() {
        // Utility class: no instances
    }

    public static Map<Integer, Double> buildPantryMap(List<PantryItem> items,
                                                      boolean excludeExpired,
                                                      Calendar today) {
        Map<Integer, Double> pantry = new HashMap<>();
        for (PantryItem item : items) {
            if (excludeExpired && ExpiryUtils.daysUntil(item.getExpiryDate(), today) < 0) {
                continue;
            }
            pantry.put(item.getIngredientId(), item.getQuantity());
        }
        return pantry;
    }

    public static boolean canMake(Recipe recipe, Map<Integer, Double> pantry) {
        List<RecipeIngredient> needed = recipe.getIngredients();
        if (needed.isEmpty()) {
            return false; // no ingredient data: cannot prove it is makeable
        }
        for (RecipeIngredient ingredient : needed) {
            Double have = pantry.get(ingredient.getIngredientId());
            if (have == null || have + EPSILON < ingredient.getQuantity()) {
                return false; // missing or not enough: the recipe fails
            }
        }
        return true;
    }

    public static List<Recipe> findStrictMatches(List<Recipe> recipes, Map<Integer, Double> pantry) {
        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (canMake(recipe, pantry)) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    public static List<RecipeIngredient> missingIngredients(Recipe recipe, Map<Integer, Double> pantry) {
        List<RecipeIngredient> missing = new ArrayList<>();
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            Double have = pantry.get(ingredient.getIngredientId());
            if (have == null || have + EPSILON < ingredient.getQuantity()) {
                missing.add(ingredient);
            }
        }
        return missing;
    }

    public static List<Recipe> findAlmostThere(List<Recipe> recipes, Map<Integer, Double> pantry) {
        List<Recipe> almost = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (!recipe.getIngredients().isEmpty()
                    && missingIngredients(recipe, pantry).size() == 1) {
                almost.add(recipe);
            }
        }
        return almost;
    }
}