package com.wolf.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailActivity extends AppCompatActivity {
    TextView ingredientsTitleTextView;
    RecyclerView ingredientsRecyclerView;
    TextView stepsTitleTextView;
    RecyclerView stepsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ingredientsTitleTextView = findViewById(R.id.ingredients_title_textview);
        stepsTitleTextView = findViewById(R.id.steps_title_textview);
        ingredientsRecyclerView = findViewById(R.id.ingredients_recyclerview);
        stepsRecyclerView = findViewById(R.id.steps_recyclerview);
        Intent intentFromMainActivity = getIntent();
        String recipeJsonString = intentFromMainActivity.getStringExtra("recipe");
        try {
            JSONObject recipeJsonObject = new JSONObject(recipeJsonString);
            JSONArray ingredientsArray = recipeJsonObject.getJSONArray("ingredients");
            JSONArray stepsArray = recipeJsonObject.getJSONArray("steps");

            RecipeIngredientAdapter recipeIngredientAdapter = new RecipeIngredientAdapter(
                    this, ingredientsArray);
            Utils.bindAdapter(this, ingredientsRecyclerView, recipeIngredientAdapter);
            RecipeStepAdapter recipeStepAdapter = new RecipeStepAdapter(this, stepsArray);
            Utils.bindAdapter(this, stepsRecyclerView, recipeStepAdapter);

            Utils.setVisibilityToggleListener(ingredientsTitleTextView, ingredientsRecyclerView);
            Utils.setVisibilityToggleListener(stepsTitleTextView, stepsRecyclerView);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
