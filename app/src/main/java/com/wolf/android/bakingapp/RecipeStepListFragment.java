package com.wolf.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeStepListFragment extends Fragment {

    public RecipeStepListFragment(){}

    TextView ingredientsTitleTextView;
    JSONArray mIngredientsArray;
    RecyclerView ingredientsRecyclerView;
    TextView stepsTitleTextView;
    RecyclerView stepsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_list, container, false);

        ingredientsTitleTextView = rootView.findViewById(R.id.ingredients_title_textview);
        stepsTitleTextView = rootView.findViewById(R.id.steps_title_textview);
        ingredientsRecyclerView = rootView.findViewById(R.id.ingredients_recyclerview);
        stepsRecyclerView = rootView.findViewById(R.id.steps_recyclerview);
        Intent intentFromMainActivity = getActivity().getIntent();
        String recipeJsonString = intentFromMainActivity.getStringExtra("recipe");
        try {
            JSONObject recipeJsonObject = new JSONObject(recipeJsonString);
            JSONArray ingredientsArray = recipeJsonObject.getJSONArray("ingredients");
            mIngredientsArray = ingredientsArray;
            JSONArray stepsArray = recipeJsonObject.getJSONArray("steps");

            RecipeIngredientAdapter recipeIngredientAdapter = new RecipeIngredientAdapter(
                    getActivity(), ingredientsArray);
            Utils.bindAdapter(getActivity(), ingredientsRecyclerView, recipeIngredientAdapter, 1);
            RecipeStepAdapter recipeStepAdapter = new RecipeStepAdapter(getActivity(), stepsArray);
            Utils.bindAdapter(getActivity(), stepsRecyclerView, recipeStepAdapter, 1);
            Utils.setVisibilityToggleListener(ingredientsTitleTextView, ingredientsRecyclerView);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void addRecipeIngredientsToWidget(JSONArray ingredientsArray) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(),
                RecipeWidgetProvider.class));
        //Now update all widgets
        RecipeWidgetProvider.updateRecipeWidgets(getActivity(), appWidgetManager, ingredientsArray,
                appWidgetIds);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.recipe_steps_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_to_widget) {
            addRecipeIngredientsToWidget(mIngredientsArray);
            Toast.makeText(getActivity(), "Ingredients Added To Widget", Toast.LENGTH_SHORT)
                    .show();
        }
        return false;
    }
}
