package com.kulloveth.bakingApp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.FragmentDetailBinding;
import com.kulloveth.bakingApp.ui.adapters.IngredientsAdapter;
import com.kulloveth.bakingApp.ui.adapters.StepAdapter;
import com.kulloveth.bakingApp.ui.main.MainActivityViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    FragmentDetailBinding binding;
    IngredientsAdapter ingredientsAdapter;
    MainActivityViewModel viewModel;
    RecyclerView ingredientsRecyclerView;
    RecyclerView stepRecyclerView;
    StepAdapter stepAdapter;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recipeToolbar.recipeToolbar.setTitle(requireActivity().getResources().getString(R.string.recipe_detail));

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        ingredientsAdapter = new IngredientsAdapter();
        ingredientsRecyclerView = binding.ingredientsRv;
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        stepAdapter = new StepAdapter();
        stepRecyclerView = binding.stepRv;
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        stepRecyclerView.setHasFixedSize(true);
        stepRecyclerView.setAdapter(stepAdapter);
        getIngredients();
        getStep();


    }

    private void getIngredients() {
        viewModel.getRecipeLivedata().observe(requireActivity(), recipe -> {
            ingredientsAdapter.submitList(recipe.getIngredients());
        });
    }

    private void getStep() {
        viewModel.getRecipeLivedata().observe(requireActivity(), recipe -> {
            stepAdapter.submitList(recipe.getSteps());
        });
    }
}
