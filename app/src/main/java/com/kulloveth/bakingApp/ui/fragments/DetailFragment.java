package com.kulloveth.bakingApp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.FragmentDetailBinding;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.model.Step;
import com.kulloveth.bakingApp.ui.adapters.IngredientsAdapter;
import com.kulloveth.bakingApp.ui.adapters.StepAdapter;
import com.kulloveth.bakingApp.ui.main.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements StepAdapter.StepItemClickListener {

    public static final String RECIPE_KEY = "steps-key";
    FragmentDetailBinding binding;
    IngredientsAdapter ingredientsAdapter;
    MainActivityViewModel viewModel;
    RecyclerView ingredientsRecyclerView;
    RecyclerView stepRecyclerView;
    StepAdapter stepAdapter;
    boolean isTablet;
    Recipe recipe;
    List<Step> stepList = new ArrayList<>();

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.recipeToolbar.recipeToolbar.setTitle(requireActivity().getResources().getString(R.string.recipe_detail));
        binding.recipeToolbar.recipeToolbar.setNavigationIcon();
        isTablet = getResources().getBoolean(R.bool.isTablet);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        Bundle bundle = getArguments();
        if (bundle != null) {
            recipe = bundle.getParcelable(RECIPE_KEY);
        }

        if (savedInstanceState != null){
            recipe = savedInstanceState.getParcelable(RECIPE_KEY);
        }

        ingredientsAdapter = new IngredientsAdapter();
        ingredientsRecyclerView = binding.ingredientsRv;
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        stepAdapter = new StepAdapter();
        stepAdapter.setClickListener(this);
        stepRecyclerView = binding.stepRv;
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        stepRecyclerView.setHasFixedSize(true);
        stepRecyclerView.setAdapter(stepAdapter);
        getIngredients();
        getStep();

        stepList = recipe.getSteps();

        if(isTablet) {
            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setStep(stepList.get(0));
            FragmentManager manager = getChildFragmentManager();
            if (manager != null) {
                manager.beginTransaction().replace(R.id.step_detail_graph, stepsFragment).commit();
            }
        }

    }

    private void getIngredients() {
        viewModel.getRecipeLivedata().observe(requireActivity(),recipes -> {
            ingredientsAdapter.submitList(recipes.getIngredients());
        });

    }

    private void getStep() {
        viewModel.getRecipeLivedata().observe(requireActivity(),recipes->{
            stepAdapter.submitList(recipes.getSteps());
        });

    }


    @Override
    public void stepItemClicked(Step step) {
        if (isTablet) {
            StepsFragment fragment = StepsFragment.newInstance(step);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.step_detail_graph, fragment);
            transaction.commit();
            Log.d("step", "stepItemClicked: " + step.getDescription());
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable("step-key", step);
            Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_stepsFragment, bundle);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY,recipe);

    }
}
