package com.smartpantrymanager.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("ingredient_id")
    private Integer id;

    @SerializedName("canonical_name")
    private String canonicalName;

    @SerializedName("base_unit")
    private String baseUnit;   // "g", "ml" or "pcs"

    public Ingredient() {

    }

    public Ingredient(Integer id, String canonicalName, String baseUnit) {
        this.id = id;
        this.canonicalName = canonicalName;
        this.baseUnit = baseUnit;
    }

    public Integer getId() { return id; }
    public String getCanonicalName() { return canonicalName; }
    public String getBaseUnit() { return baseUnit; }
}