package com.kulloveth.bakingApp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
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
import com.kulloveth.bakingApp.AppUtils;
import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.ActivityStepDetailBinding;
import com.kulloveth.bakingApp.model.Step;
import com.squareup.picasso.Picasso;

import java.net.URLConnection;

import static com.kulloveth.bakingApp.ui.fragments.StepDetailFragment.SIMPLE_EXOPLAYER_POSITION;
import static com.kulloveth.bakingApp.ui.fragments.StepDetailFragment.SIMPLE_EXPOPLAYER_STTE;
import static com.kulloveth.bakingApp.ui.fragments.StepDetailFragment.STEP_KEY;

public class StepDetailActivity extends AppCompatActivity {

    ActivityStepDetailBinding binding;

    Step step;
    private long expoPlayerPosition;
    private boolean exoPlayerState;
    SimpleExoPlayer simpleExoPlayer;
    SimpleExoPlayerView simpleExoPlayerView;
    TextView noVideoMessage;

    private static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        simpleExoPlayerView = binding.mediaPlayer;
        noVideoMessage = binding.noVideoMessage;
        expoPlayerPosition = 0;

        Intent intent = getIntent();
        step = intent.getParcelableExtra(STEP_KEY);
        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(STEP_KEY);
            exoPlayerState = savedInstanceState.getBoolean(SIMPLE_EXPOPLAYER_STTE);
            expoPlayerPosition = savedInstanceState.getLong(SIMPLE_EXOPLAYER_POSITION);
        }

        setPortraitOrLandscape();


        if (!step.getVideoURL().equals("") && step.getVideoURL() != null) {
            if (AppUtils.isConnected(this)) {
                binding.thummbnail.setVisibility(View.GONE);
                noVideoMessage.setVisibility(View.GONE);
                initializePlayer(Uri.parse(step.getVideoURL()));
            } else {
                simpleExoPlayerView.setVisibility(View.GONE);
                noVideoMessage.setText(getResources().getString(R.string.no_internet_message));
            }
        } else {
            if (!step.getThumbnailURL().equals("") && isImageFile(step.getThumbnailURL())) {
                binding.thummbnail.setVisibility(View.VISIBLE);
                Picasso.get().load(step.getThumbnailURL()).placeholder(R.drawable.ingredients).into(binding.thummbnail);
            }
            binding.noVideoMessage.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.GONE);

        }
        binding.shortDescription.setText(step.getShortDescription());
        binding.longDescription.setText(step.getDescription());
    }


    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            String uAgent = Util.getUserAgent(this, "Baking App");
            MediaSource mediaSource = null;
            mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(this, uAgent), new DefaultExtractorsFactory(), null, null);
            if (expoPlayerPosition != 0) {
                simpleExoPlayer.seekTo(expoPlayerPosition);
            }

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(exoPlayerState);
        }
    }

    void setPortraitOrLandscape() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;
            simpleExoPlayerView.setLayoutParams(params);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !step.getVideoURL().equals("")) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;
            simpleExoPlayerView.setLayoutParams(params);

        }


    }


    private void releasePlayer() {
        if (!step.getVideoURL().equals("") && simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            expoPlayerPosition = simpleExoPlayer.getContentPosition();
            exoPlayerState = simpleExoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!step.getVideoURL().equals("") && step.getVideoURL() != null) {
            initializePlayer(Uri.parse(step.getVideoURL()));
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_KEY, step);
        outState.putBoolean(SIMPLE_EXPOPLAYER_STTE, exoPlayerState);
        outState.putLong(SIMPLE_EXOPLAYER_POSITION, expoPlayerPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (step != null) {
            releasePlayer();
        }
    }
}
