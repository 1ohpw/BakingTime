package com.wolf.android.bakingapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private String recipeJsonUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/" +
            "59121517_baking/baking.json";
    private RecyclerView recipeCardRecyclerView;
    private RecipeCardAdapter recipeCardAdapter;
    private int numberOfColumns = 1;
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isTwoPane = getResources().getBoolean(R.bool.isTwoPane);
        context = this;
        recipeCardRecyclerView = findViewById(R.id.recipe_card_recyclerview);
        if(isTwoPane) {
            numberOfColumns = 2;
        }
        getJSON(recipeJsonUrl);
    }

    private void getJSON(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        recipeCardAdapter = new RecipeCardAdapter(context, response);
                        Utils.bindAdapter(context, recipeCardRecyclerView, recipeCardAdapter,
                                numberOfColumns);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }
}
