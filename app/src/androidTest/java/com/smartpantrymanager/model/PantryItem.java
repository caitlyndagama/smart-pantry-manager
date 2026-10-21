package com.smartpantrymanager.model;

import com.google.gson.annotations.SerializedName;

public class PantryItem {

    @SerializedName("pantry_item_id")
    private Integer id;

    @SerializedName("ingredient_id")
    private Integer ingredientId;

    @SerializedName("display_name")
    private String displayName; // what the user typed

    @SerializedName("quantity")
    private double quantity;// in base unit

    @SerializedName("expiry_date")
    private String expiryDate; // "yyyy-MM-dd" or null

    @SerializedName("ingredients")
    private Ingredient ingredient;

    public PantryItem() {

    }

    public PantryItem(Integer id, Integer ingredientId, String displayName,
                      double quantity, String expiryDate, Ingredient ingredient) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.displayName = displayName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.ingredient = ingredient;
    }

    public Integer getId() { return id; }
    public Integer getIngredientId() { return ingredientId; }
    public String getDisplayName() { return displayName; }
    public double getQuantity() { return quantity; }
    public String getExpiryDate() { return expiryDate; }
    public Ingredient getIngredient() { return ingredient; }

    public String getBaseUnit() {
        return ingredient != null ? ingredient.getBaseUnit() : "";
    }
}
