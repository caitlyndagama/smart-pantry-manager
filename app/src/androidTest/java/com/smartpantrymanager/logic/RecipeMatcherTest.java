package com.smartpantrymanager.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static java.util.Collections.list;

import com.smartpantrymanager.model.PantryItem;
import com.smartpantrymanager.model.Recipe;
import com.smartpantrymanager.model.RecipeIngredient;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class RecipeMatcherTest {

    private Recipe omelette;
    private Map<Integer, Double> pantry;

    @Before
    public void setUp() {
        omelette = new Recipe(1, "Tomato omelette",
                Arrays.asList(new RecipeIngredient(1, 2, null),
                        new RecipeIngredient(2, 30, null),
                        new RecipeIngredient(3, 1, null),
                        new RecipeIngredient(4, 1, null),
                        new RecipeIngredient(5, 10, null)));

        pantry = new HashMap<>();
        pantry.put(1, 6.0);
        pantry.put(2, 1000.0);
        pantry.put(3, 4.0);
        pantry.put(4, 500.0);
        pantry.put(5, 250.0);
    }

    @Test
    public void allFiveIngredientsPresent_recipeIsSuggested() {
        assertTrue(RecipeMatcher.canMake(omelette, pantry));
    }
    @Test
    public void fourOfFiveIngredients_recipeIsNotSuggested() {
        pantry.remove(3); // no tomato
        assertFalse(RecipeMatcher.canMake(omelette, pantry));
        assertTrue(RecipeMatcher.findStrictMatches(list(omelette), pantry).isEmpty());
    }
    @Test
    public void notEnoughOfOneIngredient_recipeIsNotSuggested() {
        pantry.put(1, 1.0); // only 1 egg, recipe needs 2
        assertFalse(RecipeMatcher.canMake(omelette, pantry));
    }
    @Test
    public void exactlyTheRequiredQuantity_isEnough() {
        pantry.put(1, 2.0);
        assertTrue(RecipeMatcher.canMake(omelette, pantry));
    }
    @Test
    public void unitsAreComparedAfterConversion() {
// User stored 0.03 l of milk: converted to 30 ml, exactly enough
        pantry.put(2, UnitConverter.toBase(0.03, "l"));
        assertTrue(RecipeMatcher.canMake(omelette, pantry));
    }
    @Test
    public void recipeWithNoIngredientData_isNeverSuggested() {
        Recipe empty = new Recipe(2, "Mystery", new ArrayList<>());
        assertFalse(RecipeMatcher.canMake(empty, pantry));
    }
    @Test
    public void almostThereListHoldsOnlyOneMissingIngredient() {
        pantry.remove(3); // missing exactly one
        assertEquals(1, RecipeMatcher.findAlmostThere(list(omelette), pantry).size());
        pantry.remove(5); // now missing two
        assertTrue(RecipeMatcher.findAlmostThere(list(omelette), pantry).isEmpty());
    }
    @Test
    public void expiredItemsCanBeLeftOutOfMatching() {
        Calendar today = Calendar.getInstance();
        today.set(2026, Calendar.OCTOBER, 20);
        List<PantryItem> items = Arrays.asList(
                new PantryItem(1, 1, "Eggs", 6, "2026-10-25", null), // fresh
                new PantryItem(2, 2, "Milk", 1000, "2026-10-18", null)); // expired
        Map<Integer, Double> strict = RecipeMatcher.buildPantryMap(items, true, today);
        assertTrue(strict.containsKey(1));
        assertFalse(strict.containsKey(2));
        Map<Integer, Double> lenient = RecipeMatcher.buildPantryMap(items, false, today);
        assertTrue(lenient.containsKey(2));
    }
    private static List<Recipe> list(Recipe... recipes) {
        return Arrays.asList(recipes);
    }
}