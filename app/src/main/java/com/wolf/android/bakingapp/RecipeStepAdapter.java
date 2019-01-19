package com.wolf.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeStepAdapter extends
        RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray mStepsJsonArray;

    public RecipeStepAdapter(Context context, JSONArray stepsJsonArray) {
        this.mInflater = LayoutInflater.from(context);
        this.mStepsJsonArray = stepsJsonArray;
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecipeStepAdapter mRecipeStepAdapter;
        LinearLayout stepTitleContainer;
        TextView stepNumberTextView;
        TextView stepShortDescriptionTextView;
        OnRecipeStepClickListener callback;
        Bundle stepsDetailBundle = new Bundle();

        public RecipeStepViewHolder(@NonNull View itemView,
                                          RecipeStepAdapter recipeStepAdapter) {
            super(itemView);
            this.mRecipeStepAdapter = recipeStepAdapter;
            callback = (OnRecipeStepClickListener) itemView.getContext();
            stepTitleContainer = itemView.findViewById(R.id.step_title_container);
            stepNumberTextView = itemView.findViewById(R.id.step_number_textview);
            stepShortDescriptionTextView =
                    itemView.findViewById(R.id.step_short_description_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            stepsDetailBundle.putString("recipeSteps", mStepsJsonArray.toString());
            stepsDetailBundle.putInt("currentStepIndex", getLayoutPosition());
            callback.onRecipeStepSelected(getLayoutPosition(), stepsDetailBundle);
        }
    }

    @NonNull
    @Override
    public RecipeStepAdapter.RecipeStepViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recipe_step_item, viewGroup,
                false);
        return new RecipeStepViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeStepAdapter.RecipeStepViewHolder recipeStepViewHolder,
            int i) {
        try {
            JSONObject currentStepsObject = mStepsJsonArray.getJSONObject(i);
            String stepNumber =
                    Integer.toString(currentStepsObject.getInt("id") + 1);
            String stepShortDescription = currentStepsObject.getString("shortDescription");
            recipeStepViewHolder.stepNumberTextView.setText(stepNumber);
            recipeStepViewHolder.stepShortDescriptionTextView.setText(stepShortDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mStepsJsonArray.length();
    }
}

