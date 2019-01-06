package com.wolf.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class RecipeCardListFragment extends Fragment {

    public RecipeCardListFragment() {}

    private String recipeJsonUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/" +
            "59121517_baking/baking.json";

    private RecyclerView recipeCardRecyclerView;
    private RecipeCardAdapter recipeCardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_card_list, container, false);

        recipeCardRecyclerView = rootView.findViewById(R.id.recipe_card_recyclerview);

        getJSON(recipeJsonUrl);
        return rootView;
    }

    private void getJSON(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        recipeCardAdapter = new RecipeCardAdapter(getActivity(), response);
                        Utils.bindAdapter(getActivity(), recipeCardRecyclerView, recipeCardAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }
}
