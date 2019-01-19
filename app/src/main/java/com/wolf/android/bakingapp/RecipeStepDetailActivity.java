package com.wolf.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RecipeStepDetailActivity extends AppCompatActivity {
    RecipeStepDetailFragment recipeDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        if(savedInstanceState != null) {
            recipeDetailFragment = (RecipeStepDetailFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState,"RecipeStepDetailFragment");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment_container, recipeDetailFragment)
                    .commit();
        } else {
            recipeDetailFragment = new RecipeStepDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_detail_fragment_container, recipeDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "RecipeStepDetailFragment",
                recipeDetailFragment);
    }
}
