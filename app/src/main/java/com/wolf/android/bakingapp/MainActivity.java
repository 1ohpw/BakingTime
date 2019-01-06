package com.wolf.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements OnRecipeCardClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.recipe_detail_fragment_container) != null) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_fragment_container, recipeDetailFragment)
                    .commit();
        }

    }

    @Override
    public void onRecipeCardClicked(Bundle bundle) {
        Intent intentToRecipeDetailActivity = new Intent(this,
                        RecipeDetailActivity.class);
                intentToRecipeDetailActivity.putExtra("recipe",
                        bundle.getString("recipe"));
                startActivity(intentToRecipeDetailActivity);
    }
}
