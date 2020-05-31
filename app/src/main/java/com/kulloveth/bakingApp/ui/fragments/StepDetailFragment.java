package com.kulloveth.bakingApp.ui.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.kulloveth.bakingApp.databinding.FragmentStepsBinding;
import com.kulloveth.bakingApp.model.Step;
import com.kulloveth.bakingApp.ui.RecipeActivityViewModel;
import com.squareup.picasso.Picasso;

import java.net.URLConnection;

import static com.kulloveth.bakingApp.ui.activities.RecipeDetailActivity.IS_TABLET_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {
    public static final String STEP_KEY = "step-key";
    public static final String SIMPLE_EXPOPLAYER_STTE = "simple_exoplayerstate";
    public static final String SIMPLE_EXOPLAYER_POSITION = "simple_exo_playerpositon";

    private FragmentStepsBinding binding;
    private RecipeActivityViewModel recipeActivityViewModel;
    private SimpleExoPlayer simpleExoPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;
    private TextView noVideoMessage;
    private Step step;
    private long expoPlayerPosition;
    private boolean exoPlayerState;
    private boolean isTablet;

    private static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStepsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        simpleExoPlayerView = binding.mediaPlayer;
        noVideoMessage = binding.noVideoMessage;
        expoPlayerPosition = 0;
        exoPlayerState = true;
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(STEP_KEY);
            exoPlayerState = savedInstanceState.getBoolean(SIMPLE_EXPOPLAYER_STTE);
            expoPlayerPosition = savedInstanceState.getLong(SIMPLE_EXOPLAYER_POSITION);
            isTablet = savedInstanceState.getBoolean(IS_TABLET_KEY);
        }

        setPortraitOrLandscape();

        recipeActivityViewModel = new ViewModelProvider(requireActivity()).get(RecipeActivityViewModel.class);


        if (!step.getVideoURL().equals("") && step.getVideoURL() != null) {
            if (AppUtils.isConnected(requireActivity())) {
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
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            String uAgent = Util.getUserAgent(requireContext(), "Baking App");
            MediaSource mediaSource = null;
            if (requireActivity() != null)
                mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(requireActivity(), uAgent), new DefaultExtractorsFactory(), null, null);
            if (expoPlayerPosition != 0) {
                simpleExoPlayer.seekTo(expoPlayerPosition);
            }

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(exoPlayerState);
        }
    }

    private void setPortraitOrLandscape() {

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
        outState.putBoolean(IS_TABLET_KEY, isTablet);
    }

    public void setStep(Step step) {
        this.step = step;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (step != null) {
            releasePlayer();
        }
    }

}
