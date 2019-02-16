package com.wolf.android.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class RecipeStepDetailFragment extends Fragment {
    public RecipeStepDetailFragment() {}

    SimpleExoPlayer stepVideoPlayer;
    SimpleExoPlayerView stepVideoPlayerView;
    TextView stepDescriptionTextView;
    Button previousStepButton;
    Button nextStepButton;
    Uri stepVideoUri;
    Bundle stepDetailsBundle;
    JSONArray recipeStepsArray;
    boolean isTwoPane;
    String recipeStepsJson;
    long position = 0;
    int currentStepIndex;
    boolean playWhenReady = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        isTwoPane = getResources().getBoolean(R.bool.isTwoPane);
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        stepVideoPlayerView = rootView.findViewById(R.id.step_video_player_view);
        stepDescriptionTextView = rootView.findViewById(R.id.step_description_textview);
        previousStepButton = rootView.findViewById(R.id.previous_step_button);
        nextStepButton = rootView.findViewById(R.id.next_step_button);

        if(stepDetailsBundle == null) {
            Intent intentFromStepListActivity = getActivity().getIntent();
            stepDetailsBundle = intentFromStepListActivity.getExtras();
            currentStepIndex = stepDetailsBundle.getInt("currentStepIndex");
            if (isTwoPane) {
                previousStepButton.setVisibility(View.GONE);
                nextStepButton.setVisibility(View.GONE);
                try {
                    JSONObject recipeObject =
                            new JSONObject(stepDetailsBundle.getString("recipe"));
                    recipeStepsArray = new JSONArray(recipeObject.getJSONArray("steps"));
                    setStepDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (currentStepIndex == 0) {
                    previousStepButton.setVisibility(View.GONE);
                }

                recipeStepsJson = stepDetailsBundle.getString("steps");
                try {
                    recipeStepsArray = new JSONArray(recipeStepsJson);
                    setStepDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                nextStepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        previousStepButton.setVisibility(View.VISIBLE);
                        if (currentStepIndex < recipeStepsArray.length() - 1) {
                            currentStepIndex++;
                            if (currentStepIndex == recipeStepsArray.length() - 1) {
                                view.setVisibility(View.GONE);
                            } else {
                                view.setVisibility(View.VISIBLE);
                            }
                            setStepDetails();
                        }
                    }
                });

                previousStepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextStepButton.setVisibility(View.VISIBLE);
                        if (currentStepIndex > 0) {
                            if (currentStepIndex == 1) {
                                view.setVisibility(View.GONE);
                            } else {
                                view.setVisibility(View.VISIBLE);
                            }
                            currentStepIndex--;
                            setStepDetails();
                        }

                    }
                });

            }
        } else {
            if(isTwoPane) {
                previousStepButton.setVisibility(View.GONE);
                nextStepButton.setVisibility(View.GONE);
            }
            recipeStepsJson = stepDetailsBundle.getString("steps");
            currentStepIndex = stepDetailsBundle.getInt("currentStepIndex");
            try {
                recipeStepsArray = new JSONArray(recipeStepsJson);
                setStepDetails();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return rootView;
    }

    public void setStepDetailsBundle(Bundle stepDetailsBundle) {
        this.stepDetailsBundle = stepDetailsBundle;
    }

    public void setStepDetails() {
        try {
            JSONObject currentStepObject = recipeStepsArray.getJSONObject(currentStepIndex);
            String stepDescription = currentStepObject.getString("description");
            stepDescriptionTextView.setText(stepDescription);
            String stepVideoUrlString = currentStepObject.getString("videoURL");
            if (stepVideoUrlString != null && !stepVideoUrlString.isEmpty()) {
                stepVideoPlayerView.setVisibility(View.VISIBLE);
                if (isTwoPane) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2.0f);
                    stepVideoPlayerView.setLayoutParams(params);
                }
                stepVideoUri = Uri.parse(currentStepObject.getString("videoURL"));
                if(stepVideoPlayer != null) {
                    releasePlayer();
                }
                initializeStepVideoPlayer(stepVideoUri);
            } else {
                stepVideoPlayerView.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeStepVideoPlayer(Uri mediaUri) {
        if(stepVideoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            stepVideoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),
                    trackSelector, loadControl);
            String userAgent = Util.getUserAgent(getActivity(), "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            stepVideoPlayer.prepare(mediaSource);
            stepVideoPlayer.setPlayWhenReady(playWhenReady);
            stepVideoPlayer.seekTo(position);
            stepVideoPlayerView.setPlayer(stepVideoPlayer);
            stepVideoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && stepVideoPlayer == null) {
            if(stepVideoUri != null) {
                initializeStepVideoPlayer(stepVideoUri);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 && stepVideoPlayer == null) {
            if(stepVideoUri != null) {
                initializeStepVideoPlayer(stepVideoUri);
            }
        }
    }

    private void releasePlayer() {
        stepVideoPlayer.stop();
        stepVideoPlayer.release();
        stepVideoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 && stepVideoPlayer != null) {
            position = stepVideoPlayer.getCurrentPosition();
            playWhenReady = stepVideoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && stepVideoPlayer != null) {
            position = stepVideoPlayer.getCurrentPosition();
            playWhenReady = stepVideoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("position", position);
        outState.putBoolean("ready", playWhenReady);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getLong("position");
            playWhenReady = savedInstanceState.getBoolean("ready");
        }
    }
}
