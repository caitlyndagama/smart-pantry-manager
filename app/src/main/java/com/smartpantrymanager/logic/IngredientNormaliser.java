package com.smartpantrymanager.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class IngredientNormaliser {

    private static final Map<String, String> IRREGULAR = new HashMap<>();

    private static final Set<String> UNCHANGED = new HashSet<>();

    private static final Map<String, String> SYNONYMS = new HashMap<>();

    static {
        IRREGULAR.put("leaves", "leaf");
        IRREGULAR.put("loaves", "loaf");
        IRREGULAR.put("chillies", "chilli");
        IRREGULAR.put("chilies", "chili");
        IRREGULAR.put("cookies", "cookie");

        UNCHANGED.add("hummus");
        UNCHANGED.add("couscous");
        UNCHANGED.add("asparagus");
        UNCHANGED.add("molasses");
        UNCHANGED.add("swiss");

        SYNONYMS.put("canned tomato", "tinned tomato");
        SYNONYMS.put("chopped tomato", "tinned tomato");
        SYNONYMS.put("mealie meal", "maize meal");
        SYNONYMS.put("minced beef", "mince");
        SYNONYMS.put("beef mince", "mince");
        SYNONYMS.put("oil", "cooking oil");
        SYNONYMS.put("sunflower oil", "cooking oil");
        SYNONYMS.put("cheese", "cheddar cheese");
        SYNONYMS.put("chicken fillet", "chicken breast");
        SYNONYMS.put("wrap", "tortilla wrap");
        SYNONYMS.put("yogurt", "yoghurt");
        SYNONYMS.put("garlic clove", "garlic");
        SYNONYMS.put("bread slice", "bread");
        SYNONYMS.put("slice of bread", "bread");
        SYNONYMS.put("oat", "oats");
        SYNONYMS.put("mixed herb", "mixed herbs");
        SYNONYMS.put("baked bean", "baked beans");
        SYNONYMS.put("frozen pea", "frozen peas");
    }

    private IngredientNormaliser() {
        // Utility class: no instances
    }

    public static String normalise(String raw) {
        if (raw == null) {
            return "";
        }
        String cleaned = raw.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
        if (cleaned.isEmpty()) {
            return "";
        }

        int lastSpace = cleaned.lastIndexOf(' ');
        String start = cleaned.substring(0, lastSpace + 1);
        String lastWord = cleaned.substring(lastSpace + 1);
        String singular = start + singularise(lastWord);

        String synonym = SYNONYMS.get(singular);
        return synonym != null ? synonym : singular;
    }

    static String singularise(String word) {
        if (IRREGULAR.containsKey(word)) {
            return IRREGULAR.get(word);
        }
        if (UNCHANGED.contains(word) || word.length() <= 3) {
            return word;
        }
        if (word.endsWith("ies")) { // berries -> berry
            return word.substring(0, word.length() - 3) + "y";
        }
        if (word.endsWith("oes")) { // tomatoes -> tomato
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("ches") || word.endsWith("shes")
                || word.endsWith("sses") || word.endsWith("xes")) { // peaches -> peach
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("s") && !word.endsWith("ss")
                && !word.endsWith("us") && !word.endsWith("is")) {// eggs -> egg
            return word.substring(0, word.length() - 1);
        }
        return word;
    }
}