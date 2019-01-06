package com.wolf.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailFragment extends Fragment {

    public RecipeDetailFragment(){}

    TextView ingredientsTitleTextView;
    RecyclerView ingredientsRecyclerView;
    TextView stepsTitleTextView;
    RecyclerView stepsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        ingredientsTitleTextView = rootView.findViewById(R.id.ingredients_title_textview);
        stepsTitleTextView = rootView.findViewById(R.id.steps_title_textview);
        ingredientsRecyclerView = rootView.findViewById(R.id.ingredients_recyclerview);
        stepsRecyclerView = rootView.findViewById(R.id.steps_recyclerview);
        Intent intentFromMainActivity = getActivity().getIntent();
        String recipeJsonString = intentFromMainActivity.getStringExtra("recipe");
        try {
            JSONObject recipeJsonObject = new JSONObject(recipeJsonString);
            JSONArray ingredientsArray = recipeJsonObject.getJSONArray("ingredients");
            JSONArray stepsArray = recipeJsonObject.getJSONArray("steps");

            RecipeIngredientAdapter recipeIngredientAdapter = new RecipeIngredientAdapter(
                    getActivity(), ingredientsArray);
            Utils.bindAdapter(getActivity(), ingredientsRecyclerView, recipeIngredientAdapter);
            RecipeStepAdapter recipeStepAdapter = new RecipeStepAdapter(getActivity(), stepsArray);
            Utils.bindAdapter(getActivity(), stepsRecyclerView, recipeStepAdapter);

            Utils.setVisibilityToggleListener(ingredientsTitleTextView, ingredientsRecyclerView);
            Utils.setVisibilityToggleListener(stepsTitleTextView, stepsRecyclerView);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
