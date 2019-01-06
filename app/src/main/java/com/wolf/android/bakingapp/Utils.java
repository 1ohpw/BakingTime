package com.wolf.android.bakingapp;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class Utils {

    public static void bindAdapter(Context context, RecyclerView recyclerView,
                                   RecyclerView.Adapter recyclerViewAdapter, int numberOfColumns) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public static void setVisibilityToggleListener(View clickedView, final View viewToToggle) {
        clickedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(viewToToggle);
            }
        });
    }

    private static void toggleVisibility(View view) {
        if(view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
