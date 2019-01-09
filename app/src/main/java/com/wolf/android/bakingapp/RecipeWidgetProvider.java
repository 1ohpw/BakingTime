package com.wolf.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import org.json.JSONArray;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    private static JSONArray mIngredientList;
    private static int changeFlag = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, JSONArray jsonArray,
                                int appWidgetId) {
        mIngredientList = jsonArray;
       // RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list);
        //views.setTextViewText(R.id.test_text_view, "Yeah buddy");
        // Construct the RemoteViews object
//        if(mIngredientList != null) {
//            views = getIngredientListRemoteView(context);
//        } else {
//            views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list);
//        }

        // Instruct the widget manager to update the widget
        RemoteViews remoteViews;
        if(mIngredientList == null) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list);
            remoteViews.setTextViewText(R.id.test_text_view, "Null ingredientList");
        } else {
            remoteViews = getIngredientListRemoteView(context);
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredient_list);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredient_list);
        }
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           JSONArray jsonArray, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, jsonArray, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void setIngredientList(JSONArray ingredientList) {
        mIngredientList = ingredientList;
    }

    private static RemoteViews getIngredientListRemoteView(Context context) {
        changeFlag++;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list);
        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra("ingredientsArray", mIngredientList.toString());
        intent.setData(Uri.fromParts("content", String.valueOf(changeFlag), null));
        views.setRemoteAdapter(R.id.widget_ingredient_list, intent);
        views.setTextViewText(R.id.test_text_view, "From static method");
        return views;
    }
}

