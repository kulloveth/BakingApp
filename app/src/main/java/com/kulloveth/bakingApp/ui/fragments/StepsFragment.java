package com.kulloveth.bakingApp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.FragmentStepsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    FragmentStepsBinding binding;

    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStepsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
