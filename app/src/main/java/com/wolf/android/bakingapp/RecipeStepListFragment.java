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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeStepListFragment extends Fragment {

    public RecipeStepListFragment(){}

    TextView ingredientsTitleTextView;
    Button addToWidgetButton;
    RecyclerView ingredientsRecyclerView;
    TextView stepsTitleTextView;
    RecyclerView stepsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_list, container, false);

        ingredientsTitleTextView = rootView.findViewById(R.id.ingredients_title_textview);
        addToWidgetButton = rootView.findViewById(R.id.add_to_widget_button);
        stepsTitleTextView = rootView.findViewById(R.id.steps_title_textview);
        ingredientsRecyclerView = rootView.findViewById(R.id.ingredients_recyclerview);
        stepsRecyclerView = rootView.findViewById(R.id.steps_recyclerview);
        Intent intentFromMainActivity = getActivity().getIntent();
        String recipeJsonString = intentFromMainActivity.getStringExtra("recipe");
        try {
            JSONObject recipeJsonObject = new JSONObject(recipeJsonString);
            final JSONArray ingredientsArray = recipeJsonObject.getJSONArray("ingredients");
            JSONArray stepsArray = recipeJsonObject.getJSONArray("steps");

            RecipeIngredientAdapter recipeIngredientAdapter = new RecipeIngredientAdapter(
                    getActivity(), ingredientsArray);
            Utils.bindAdapter(getActivity(), ingredientsRecyclerView, recipeIngredientAdapter, 1);
            RecipeStepAdapter recipeStepAdapter = new RecipeStepAdapter(getActivity(), stepsArray);
            Utils.bindAdapter(getActivity(), stepsRecyclerView, recipeStepAdapter, 1);

            Utils.setVisibilityToggleListener(ingredientsTitleTextView, ingredientsRecyclerView);

            addToWidgetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRecipeIngredientsToWidget(ingredientsArray);
                }
            });
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void addRecipeIngredientsToWidget(JSONArray ingredientsArray) {
//        RemoteViews views = new RemoteViews(getActivity().getPackageName(), R.layout.recipe_widget_list);
//        Intent intent = new Intent(getActivity(), ListWidgetService.class);
//        intent.putExtra("ingredientsArray", ingredientsArray.toString());
//        views.setRemoteAdapter(R.id.widget_ingredient_list, intent);
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
//        appWidgetManager.updateAppWidget(new ComponentName(getActivity().getPackageName(),
//                RecipeWidgetProvider.class.getName()), views);

//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(), RecipeWidgetProvider.class));
//        appWidgetManager.updateAppWidget(new ComponentName(getActivity().getPackageName(),
//               RecipeWidgetProvider.class.getName()), ingredientsArray);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(), RecipeWidgetProvider.class));
        //Now update all widgets
        RecipeWidgetProvider.updateRecipeWidgets(getActivity(), appWidgetManager, ingredientsArray,appWidgetIds);
    }
}
