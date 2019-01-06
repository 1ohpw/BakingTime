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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RecipeStepDetailFragment extends Fragment {
    public RecipeStepDetailFragment() {}

    SimpleExoPlayer stepVideoPlayer;
    SimpleExoPlayerView stepVideoPlayerView;
    TextView stepDescriptionTextView;
    Bundle stepDetailsBundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        stepVideoPlayerView = rootView.findViewById(R.id.step_video_player_view);
        stepDescriptionTextView = rootView.findViewById(R.id.step_description_textview);

        if(stepDetailsBundle == null) {
            Intent intentFromStepListActivity = getActivity().getIntent();
            stepDetailsBundle = intentFromStepListActivity.getExtras();
        }

        String stepDescription = stepDetailsBundle.getString("description");
        String stepVideoUrlString = stepDetailsBundle.getString("videoURL");

        if(stepDescription != null && stepVideoUrlString != null) {
            Uri stepVideoUri = Uri.parse(stepDetailsBundle.getString("videoURL"));
            stepDescriptionTextView.setText(stepDescription);
            initializeStepVideoPlayer(stepVideoUri);
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
            stepVideoPlayerView.setPlayer(stepVideoPlayer);
            String userAgent = Util.getUserAgent(getActivity(), "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            stepVideoPlayer.prepare(mediaSource);
            stepVideoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        stepVideoPlayer.stop();
        stepVideoPlayer.release();
        stepVideoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(stepVideoPlayer != null) {
            releasePlayer();
        }
    }
}
