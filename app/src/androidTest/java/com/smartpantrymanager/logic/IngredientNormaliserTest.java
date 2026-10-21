package com.smartpantrymanager.logic;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class IngredientNormaliserTest {
    @Test
    public void pluralAndCaseAndSpacesAreIgnored() {
        assertEquals("tomato", IngredientNormaliser.normalise(" Tomatoes "));
        assertEquals("egg", IngredientNormaliser.normalise("EGGS"));
        assertEquals("green pepper", IngredientNormaliser.normalise("Green Peppers"));
        assertEquals("berry", IngredientNormaliser.normalise("berries"));
        assertEquals("peach", IngredientNormaliser.normalise("peaches"));
        assertEquals("potato", IngredientNormaliser.normalise("Potatoes"));
    }
    @Test
    public void singularNamesStayTheSame() {
        assertEquals("tomato", IngredientNormaliser.normalise("tomato"));
        assertEquals("rice", IngredientNormaliser.normalise("Rice"));
        assertEquals("hummus", IngredientNormaliser.normalise("hummus"));
    }
    @Test
    public void synonymsMapToTheSeededName() {
        assertEquals("tinned tomato", IngredientNormaliser.normalise("Canned tomatoes"));
        assertEquals("maize meal", IngredientNormaliser.normalise("Mealie meal"));
        assertEquals("cooking oil", IngredientNormaliser.normalise("Sunflower oil"));
        assertEquals("garlic", IngredientNormaliser.normalise("garlic cloves"));
        assertEquals("oats", IngredientNormaliser.normalise("Oats"));
        assertEquals("baked beans", IngredientNormaliser.normalise("baked bean"));
    }
    @Test
    public void blankInputGivesEmptyString() {
        assertEquals("", IngredientNormaliser.normalise(" "));
        assertEquals("", IngredientNormaliser.normalise(null));
    }
    @Test
    public void seededNamesAreCanonical() {
        String[] seeded = {
                "egg", "milk", "butter", "flour", "sugar", "salt", "black pepper",
                "cooking oil", "onion", "garlic", "tomato", "cheddar cheese", "bread",
                "rice", "pasta", "potato", "carrot", "green pepper", "chicken breast",
                "mince", "tinned tomato", "baked beans", "banana", "oats", "honey",
                "cinnamon", "baking powder", "mixed herbs", "soy sauce", "curry powder",
                "chickpea", "frozen peas", "tuna", "mayonnaise", "cucumber", "mushroom",
                "cream", "maize meal", "cabbage", "spinach", "yoghurt", "apple",
                "tortilla wrap"};
        for (String name : seeded) {
            assertEquals(name, IngredientNormaliser.normalise(name));
        }
    }
}
