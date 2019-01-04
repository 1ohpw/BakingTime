package com.wolf.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeStepAdapter extends
        RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray mStepsJsonArray;

    public RecipeStepAdapter(Context context, JSONArray stepsJsonArray) {
        this.mInflater = LayoutInflater.from(context);
        this.mStepsJsonArray = stepsJsonArray;
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder {
        RecipeStepAdapter mRecipeStepAdapter;
        LinearLayout stepTitleContainer;
        TextView stepNumberTextView;
        TextView stepShortDescriptionTextView;
        LinearLayout stepDetailsContainer;
        SimpleExoPlayer stepVideoPlayer;
        SimpleExoPlayerView stepVideoPlayerView;
        TextView stepDescriptionTextView;

        public RecipeStepViewHolder(@NonNull View itemView,
                                          RecipeStepAdapter recipeStepAdapter) {
            super(itemView);
            this.mRecipeStepAdapter = recipeStepAdapter;
            stepTitleContainer = itemView.findViewById(R.id.step_title_container);
            stepNumberTextView = itemView.findViewById(R.id.step_number_textview);
            stepShortDescriptionTextView =
                    itemView.findViewById(R.id.step_short_description_textview);
            stepDetailsContainer = itemView.findViewById(R.id.step_details_container);
            stepVideoPlayerView = itemView.findViewById(R.id.step_video_player_view);
            stepDescriptionTextView = itemView.findViewById(R.id.step_description_textview);

            stepTitleContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(stepDetailsContainer.getVisibility() == View.GONE) {
                        stepDetailsContainer.setVisibility(View.VISIBLE);
                        try {
                            JSONObject currentStepsObject =
                                    mStepsJsonArray.getJSONObject(getLayoutPosition());
                            Uri stepVideoUri =
                                    Uri.parse(currentStepsObject.getString("videoURL"));
                            initializeStepVideoPlayer(stepVideoUri);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        releasePlayer();
                        stepDetailsContainer.setVisibility(View.GONE);
                    }
                }
            });
        }

        private void initializeStepVideoPlayer(Uri mediaUri) {
            if(stepVideoPlayer == null) {
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                stepVideoPlayer = ExoPlayerFactory.newSimpleInstance(itemView.getContext(),
                        trackSelector, loadControl);
                stepVideoPlayerView.getVideoSurfaceView().setVisibility(View.VISIBLE);
                stepVideoPlayerView.setPlayer(stepVideoPlayer);
                String userAgent = Util.getUserAgent(itemView.getContext(), "BakingTime");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                        new DefaultDataSourceFactory(itemView.getContext(), userAgent),
                        new DefaultExtractorsFactory(), null, null);
                stepVideoPlayer.prepare(mediaSource);
                stepVideoPlayer.setPlayWhenReady(true);
            }
        }

        private void releasePlayer() {
            stepVideoPlayerView.getVideoSurfaceView().setVisibility(View.GONE);
            stepVideoPlayer.stop();
            stepVideoPlayer.release();
            stepVideoPlayer = null;
        }
    }

    @NonNull
    @Override
    public RecipeStepAdapter.RecipeStepViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recipe_step_item, viewGroup,
                false);
        return new RecipeStepViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeStepAdapter.RecipeStepViewHolder recipeStepViewHolder,
            int i) {
        try {
            JSONObject currentStepsObject = mStepsJsonArray.getJSONObject(i);
            String stepNumber =
                    Integer.toString(currentStepsObject.getInt("id") + 1);
            String stepShortDescription = currentStepsObject.getString("shortDescription");
            String stepDescription = currentStepsObject.getString("description");
            recipeStepViewHolder.stepNumberTextView.setText(stepNumber);
            recipeStepViewHolder.stepShortDescriptionTextView.setText(stepShortDescription);
            recipeStepViewHolder.stepDescriptionTextView.setText(stepDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mStepsJsonArray.length();
    }
}

