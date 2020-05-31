package com.kulloveth.bakingApp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.FragmentStepBinding;
import com.kulloveth.bakingApp.model.Step;
import com.kulloveth.bakingApp.ui.activities.StepDetailActivity;
import com.kulloveth.bakingApp.ui.adapters.StepAdapter;

import java.util.ArrayList;

import static com.kulloveth.bakingApp.ui.fragments.StepDetailFragment.STEP_KEY;
import static com.kulloveth.bakingApp.ui.widget.WidgetService.STEPS_LIST_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements StepAdapter.StepItemClickListener {


    private StepAdapter stepAdapter;
    private FragmentStepBinding binding;
    private boolean isTablet;
    private ArrayList<Step> stepList = new ArrayList<>();

    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStepBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }


    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            stepList = savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
        }
        isTablet = getResources().getBoolean(R.bool.isTablet);
        stepAdapter = new StepAdapter();
        stepAdapter.setClickListener(this);
        RecyclerView stepRecyclerView = binding.stepRv;
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        stepRecyclerView.setHasFixedSize(true);
        stepRecyclerView.setAdapter(stepAdapter);
        getStep();
    }

    @Override
    public void stepItemClicked(Step step) {
        if (isTablet) {
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setStep(step);
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(R.id.step_detail_graph, fragment).commit();
            }

            Log.d("step", "stepItemClicked: " + step.getDescription());
        } else {
            Intent intent = new Intent(requireActivity(), StepDetailActivity.class);
            intent.putExtra(STEP_KEY, step);
            startActivity(intent);
        }
    }

    private void getStep() {
        stepAdapter.submitList(stepList);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEPS_LIST_KEY, stepList);
    }
}
