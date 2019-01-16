package com.wolf.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        JSONArray mIngredientsArray;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.mContext = context;
            String ingredientsArrayString = intent.getStringExtra("ingredientsArray");
            try {
                this.mIngredientsArray = new JSONArray(ingredientsArrayString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCreate() {}
        @Override
        public void onDataSetChanged() {}
        @Override
        public void onDestroy() {}

        @Override
        public int getCount() {
            return mIngredientsArray.length();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.recipe_ingredient_item);
            try {
                JSONObject currentIngredientObject = mIngredientsArray.getJSONObject(i);
                String ingredientName = currentIngredientObject.getString("ingredient");
                String ingredientMeasure =
                        Integer.toString(currentIngredientObject.getInt("quantity")) + " " +
                                currentIngredientObject.getString("measure").toLowerCase();
                views.setTextViewText(R.id.ingredient_name_textview, ingredientName);
                views.setTextViewText(R.id.ingredient_measure_textview, ingredientMeasure);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
