package com.wolf.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class RecipeStepActivity extends AppCompatActivity implements OnRecipeStepClickListener {
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        isTwoPane = getResources().getBoolean(R.bool.isTwoPane);

        if(isTwoPane) {
            RecipeStepDetailFragment recipeDetailFragment = new RecipeStepDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_detail_fragment_container, recipeDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onRecipeStepSelected(int position, Bundle stepDetails) {
        if(isTwoPane) {
            RecipeStepDetailFragment recipeDetailFragment = new RecipeStepDetailFragment();
            stepDetails.putBoolean("fragmentReplacementFlag", true);
            recipeDetailFragment.setStepDetailsBundle(stepDetails);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment_container, recipeDetailFragment)
                    .commit();
        } else {
            Intent intentToStepDetailActivity = new Intent(this,
                    RecipeStepDetailActivity.class);
            intentToStepDetailActivity.putExtras(stepDetails);
            startActivity(intentToStepDetailActivity);
        }
    }
}
