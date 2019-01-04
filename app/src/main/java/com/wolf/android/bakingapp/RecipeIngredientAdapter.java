package com.wolf.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeIngredientAdapter extends
        RecyclerView.Adapter<RecipeIngredientAdapter.RecipeIngredientViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray mIngredientsJsonArray;

    public RecipeIngredientAdapter(Context context, JSONArray ingredientsJsonArray) {
        this.mInflater = LayoutInflater.from(context);
        this.mIngredientsJsonArray = ingredientsJsonArray;
    }

    class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {
        RecipeIngredientAdapter mRecipeIngredientAdapter;
        TextView ingredientNameTextView;
        TextView ingredientMeasureTextView;

        public RecipeIngredientViewHolder(@NonNull View itemView,
                                          RecipeIngredientAdapter recipeIngredientAdapter) {
            super(itemView);
            this.mRecipeIngredientAdapter = recipeIngredientAdapter;
            ingredientNameTextView = itemView.findViewById(R.id.ingredient_name_textview);
            ingredientMeasureTextView = itemView.findViewById(R.id.ingredient_measure_textview);
        }
    }

    @NonNull
    @Override
    public RecipeIngredientAdapter.RecipeIngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recipe_ingredient_item, viewGroup,
                false);
        return new RecipeIngredientViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeIngredientAdapter.RecipeIngredientViewHolder recipeIngredientViewHolder,
            int i) {
        try {
            JSONObject currentIngredientObject = mIngredientsJsonArray.getJSONObject(i);
            String ingredientName = currentIngredientObject.getString("ingredient");
            String ingredientMeasure =
                    Integer.toString(currentIngredientObject.getInt("quantity")) + " " +
                            currentIngredientObject.getString("measure").toLowerCase();
            recipeIngredientViewHolder.ingredientNameTextView.setText(ingredientName);
            recipeIngredientViewHolder.ingredientMeasureTextView.setText(ingredientMeasure);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mIngredientsJsonArray.length();
    }
}
