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
    private static int dataChangeFlag = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, JSONArray jsonArray,
                                int appWidgetId) {
        mIngredientList = jsonArray;
        RemoteViews remoteViews;
        if(mIngredientList != null) {
            remoteViews = getIngredientListRemoteView(context);
        } else {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list);
            remoteViews.setEmptyView(R.id.widget_ingredient_list, R.id.empty_widget_text_view);
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {}
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

    private static RemoteViews getIngredientListRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list);
        views.setEmptyView(R.id.widget_ingredient_list, R.id.empty_widget_text_view);
        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra("ingredientsArray", mIngredientList.toString());
        dataChangeFlag++;
        intent.setData(Uri.fromParts("content", String.valueOf(dataChangeFlag), null));
        views.setRemoteAdapter(R.id.widget_ingredient_list, intent);
        return views;
    }
}

