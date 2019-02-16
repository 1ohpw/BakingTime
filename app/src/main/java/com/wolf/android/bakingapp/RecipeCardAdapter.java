package com.wolf.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private JSONArray mRecipeJsonArray;

    public RecipeCardAdapter(Context context, JSONArray recipeJsonArray) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mRecipeJsonArray = recipeJsonArray;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecipeCardAdapter mRecipeCardAdapter;
        ImageView recipeImageView;
        TextView recipeNameTextView;
        TextView recipeServingsTextView;

        public RecipeViewHolder(View itemView, RecipeCardAdapter recipeCardAdapter) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.recipe_imageview);
            recipeNameTextView = itemView.findViewById(R.id.recipe_name_textview);
            recipeServingsTextView = itemView.findViewById(R.id.recipe_servings_textview);
            this.mRecipeCardAdapter = recipeCardAdapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int positionClicked = getLayoutPosition();
            try {
                JSONObject recipeClicked = mRecipeJsonArray.getJSONObject(positionClicked);
                Intent intentToRecipeStepActivity = new Intent(itemView.getContext(),
                        RecipeStepActivity.class);
                intentToRecipeStepActivity.putExtra("recipe", recipeClicked.toString());
                intentToRecipeStepActivity.putExtra("currentStepIndex", positionClicked);
                itemView.getContext().startActivity(intentToRecipeStepActivity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public RecipeCardAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                                                 int i) {
        View itemView = mInflater.inflate(R.layout.recipe_card_item, viewGroup, false);
        return new RecipeViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardAdapter.RecipeViewHolder recipeViewHolder,
                                 int i) {
        try {
            JSONObject currentRecipeObject = mRecipeJsonArray.getJSONObject(i);
            String imageUrlString = currentRecipeObject.getString("image");
            String currentRecipeName = currentRecipeObject.getString("name");
            String currentRecipeServings =
                    Integer.toString(currentRecipeObject.getInt("servings"));

            int imageId;
            switch(currentRecipeName) {
                case "Nutella Pie":
                    imageId = R.drawable.nutella_pie;
                    break;
                case "Brownies":
                    imageId = R.drawable.brownies;
                    break;
                case "Yellow Cake":
                    imageId = R.drawable.yellow_cake;
                    break;
                case "Cheesecake":
                    imageId = R.drawable.cheesecake;
                    break;
                default:
                    imageId = R.drawable.default_cupcake;
            }

            if(imageUrlString != null && !imageUrlString.isEmpty()) {
                Glide.with(this.getContext()).load(imageUrlString)
                        .into(recipeViewHolder.recipeImageView);
            } else {
                recipeViewHolder.recipeImageView.setImageResource(imageId);
            }
            recipeViewHolder.recipeNameTextView.setText(currentRecipeName);
            recipeViewHolder.recipeServingsTextView.setText(currentRecipeServings);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mRecipeJsonArray.length();
    }

    public Context getContext() {
        return mContext;
    }
}
