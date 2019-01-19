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

public class RecipeStepDetailFragment extends Fragment {
    public RecipeStepDetailFragment() {}

    SimpleExoPlayer stepVideoPlayer;
    SimpleExoPlayerView stepVideoPlayerView;
    TextView stepDescriptionTextView;
    Uri stepVideoUri;
    Bundle stepDetailsBundle;
    long position = 0;
    boolean playWhenReady = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        boolean isTwoPane = getResources().getBoolean(R.bool.isTwoPane);
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        stepVideoPlayerView = rootView.findViewById(R.id.step_video_player_view);
        stepDescriptionTextView = rootView.findViewById(R.id.step_description_textview);

        if(stepDetailsBundle == null) {
            Intent intentFromStepListActivity = getActivity().getIntent();
            stepDetailsBundle = intentFromStepListActivity.getExtras();
        }

        String stepDescription = stepDetailsBundle.getString("description");
        stepDescriptionTextView.setText(stepDescription);
        String stepVideoUrlString = stepDetailsBundle.getString("videoURL");
        if(stepVideoUrlString != null && !stepVideoUrlString.isEmpty()) {
            if(isTwoPane) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        2.0f
                );
                stepVideoPlayerView.setLayoutParams(params);
            }
            stepVideoUri = Uri.parse(stepDetailsBundle.getString("videoURL"));
        } else {
            stepVideoPlayerView.setVisibility(View.GONE);
        }
        return rootView;
    }

    public void setStepDetailsBundle(Bundle stepDetailsBundle) {
        this.stepDetailsBundle = stepDetailsBundle;
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
