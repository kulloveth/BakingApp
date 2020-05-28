package com.kulloveth.bakingApp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kulloveth.bakingApp.AppUtils;
import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.FragmentRecipeBinding;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.ui.adapters.MainAdapter;
import com.kulloveth.bakingApp.ui.main.MainActivityViewModel;
import com.kulloveth.bakingApp.utils.ProgressListener;

import java.util.List;


public class RecipeFragment extends Fragment implements ProgressListener, MainAdapter.RecipeItemClickListener {


    private MainAdapter adapter;
    private MainActivityViewModel viewModel;
    FragmentRecipeBinding binding;
    RecyclerView recyclerView;
    Navigation navigation;


    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecipeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.recipeToolbar.recipeToolbar.setTitle("Recipes");
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        viewModel.setProgressListener(this);
        adapter = new MainAdapter();
        adapter.setClickListener(this);
        recyclerView = binding.recipeRv;
        setLayoutManager();
        // recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        getRecipe();
    }

    private void getRecipe() {
        viewModel.getRecipe().observe(requireActivity(), recipes -> {
            if (!recipes.isEmpty()) {
                adapter.submitList(recipes);
                Log.d("frag", "onChanged: " + recipes);
            }
        });
    }

    private void setLayoutManager() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
        }
    }

    @Override
    public void showLoading() {
        if (AppUtils.isConnected(requireActivity())) {
            binding.recipeProgressbar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            promptUserForNetwork();
        }
    }

    @Override
    public void showRecipes() {
        binding.recipeProgressbar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoInternet() {
        promptUserForNetwork();
    }

    void promptUserForNetwork() {
        binding.recipeProgressbar.setVisibility(View.INVISIBLE);
        Snackbar.make(requireView(), getString(R.string.no_internet_message), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void recipeItemClicked(Recipe recipe) {
        viewModel.setRecipeLivedata(recipe);
        Navigation.findNavController(requireView()).navigate(RecipeFragmentDirections.actionRecipeFragmentToDetailFragment());

    }
}
