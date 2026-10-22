package com.smartpantrymanager.model;

import com.google.gson.annotations.SerializedName;

public class RecipeIngredient {

    @SerializedName("ingredient_id")
    private int ingredientId;

    @SerializedName("quantity")
    private double quantity;

    @SerializedName("ingredients")
    private Ingredient ingredient;

    public RecipeIngredient() {

    }

    public RecipeIngredient(int ingredientId, double quantity, Ingredient ingredient) {
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.ingredient = ingredient;
    }

    public int getIngredientId() { return ingredientId; }
    public double getQuantity() { return quantity; }
    public Ingredient getIngredient() { return ingredient; }

    public String getName() {
        return ingredient != null ? ingredient.getCanonicalName() : "ingredient #" + ingredientId;
    }

    public String getBaseUnit() {
        return ingredient != null ? ingredient.getBaseUnit() : "";
    }
}