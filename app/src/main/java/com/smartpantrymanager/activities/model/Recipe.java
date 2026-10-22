package com.smartpantrymanager.activities.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    @SerializedName("recipe_id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("steps")
    private String steps;

    @SerializedName("prep_minutes")
    private Integer prepMinutes;

    @SerializedName("recipe_ingredients")
    private List<RecipeIngredient> ingredients;

    public Recipe() {

    }

    public Recipe(int id, String name, List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSteps() { return steps; }
    public Integer getPrepMinutes() { return prepMinutes; }

    public List<RecipeIngredient> getIngredients() {
        return ingredients != null ? ingredients : new ArrayList<>();
    }
}