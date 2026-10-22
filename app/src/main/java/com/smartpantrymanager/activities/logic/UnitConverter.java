package com.smartpantrymanager.activities.logic;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class UnitConverter {

    public static final String[] USER_UNITS = {"g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs"};

    private static final Map<String, Double> FACTOR_TO_BASE = new HashMap<>();
    private static final Map<String, String> BASE_UNIT = new HashMap<>();

    static {
        FACTOR_TO_BASE.put("g", 1.0);
        FACTOR_TO_BASE.put("kg", 1000.0);
        FACTOR_TO_BASE.put("ml", 1.0);
        FACTOR_TO_BASE.put("l", 1000.0);
        FACTOR_TO_BASE.put("tsp", 5.0);// metric teaspoon
        FACTOR_TO_BASE.put("tbsp", 15.0);// metric tablespoon
        FACTOR_TO_BASE.put("cup", 250.0);// metric cup (as used in South Africa)
        FACTOR_TO_BASE.put("pcs", 1.0);

        BASE_UNIT.put("g", "g");
        BASE_UNIT.put("kg", "g");
        BASE_UNIT.put("ml", "ml");
        BASE_UNIT.put("l", "ml");
        BASE_UNIT.put("tsp", "ml");
        BASE_UNIT.put("tbsp", "ml");
        BASE_UNIT.put("cup", "ml");
        BASE_UNIT.put("pcs", "pcs");
    }

    private UnitConverter() {
        // Utility class: no instances
    }

    public static boolean isSupported(String unit) {
        return unit != null && FACTOR_TO_BASE.containsKey(unit);
    }

    public static String baseUnitOf(String unit) {
        String base = BASE_UNIT.get(unit);
        if (base == null) {
            throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
        return base;
    }

    public static double toBase(double quantity, String unit) {
        Double factor = FACTOR_TO_BASE.get(unit);
        if (factor == null) {
            throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
        return quantity * factor;
    }

    public static String describeBaseUnit(String baseUnit) {
        switch (baseUnit) {
            case "g":  return "weight (g or kg)";
            case "ml": return "volume (ml, l, tsp, tbsp or cup)";
            default:   return "pieces (pcs)";
        }
    }

    public static String formatNumber(double value) {
        DecimalFormat df = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ROOT));
        return df.format(value);
    }

    public static String format(double baseQuantity, String baseUnit) {
        if ("g".equals(baseUnit) && baseQuantity >= 1000) {
            return formatNumber(baseQuantity / 1000) + " kg";
        }
        if ("ml".equals(baseUnit) && baseQuantity >= 1000) {
            return formatNumber(baseQuantity / 1000) + " l";
        }
        return formatNumber(baseQuantity) + " " + baseUnit;
    }
}