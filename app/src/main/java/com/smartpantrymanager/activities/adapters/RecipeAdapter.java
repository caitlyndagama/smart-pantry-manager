package com.smartpantrymanager.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartpantrymanager.R;
import com.smartpantrymanager.activities.logic.RecipeMatcher;
import com.smartpantrymanager.activities.logic.UnitConverter;
import com.smartpantrymanager.activities.model.Recipe;
import com.smartpantrymanager.activities.model.RecipeIngredient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{
    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }
    private final List<Recipe> recipes = new ArrayList<>();
    private Map<Integer, Double> pantry = new HashMap<>();
    private final boolean showMissing;
    private final OnRecipeClickListener listener;
    public RecipeAdapter(boolean showMissing, OnRecipeClickListener listener) {
        this.showMissing = showMissing;
        this.listener = listener;
    }
    public void setRecipes(List<Recipe> newRecipes, Map<Integer, Double> pantry) {
        recipes.clear();
        if (newRecipes != null) {
            recipes.addAll(newRecipes);
        }
        this.pantry = pantry;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(row);
    }
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final TextView textRecipeName;
        private final TextView textRecipeMeta;
        private final TextView textRecipeMissing;
        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textRecipeName = itemView.findViewById(R.id.textRecipeName);
            textRecipeMeta = itemView.findViewById(R.id.textRecipeMeta);
            textRecipeMissing = itemView.findViewById(R.id.textRecipeMissing);
        }
        void bind(Recipe recipe) {
            Context context = itemView.getContext();
            int count = recipe.getIngredients().size();
            String ingredients = context.getResources()
                    .getQuantityString(R.plurals.ingredient_count, count, count);
            int minutes = recipe.getPrepMinutes() != null ? recipe.getPrepMinutes() : 0;
            textRecipeName.setText(recipe.getName());
            textRecipeMeta.setText(context.getString(R.string.recipe_meta, ingredients, minutes));
            if (showMissing) {
                List<RecipeIngredient> missing = RecipeMatcher.missingIngredients(recipe, pantry);
                StringBuilder names = new StringBuilder();
                for (RecipeIngredient ingredient : missing) {
                    if (names.length() > 0) {
                        names.append(", ");
                    }
                    names.append(ingredient.getName()).append(" (")
                            .append(UnitConverter.format(ingredient.getQuantity(), ingredient.getBaseUnit()))
                            .append(")");
                }
                textRecipeMissing.setText(context.getString(R.string.missing_prefix, names.toString()));
                textRecipeMissing.setVisibility(View.VISIBLE);
            } else {
                textRecipeMissing.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
        }
    }
}