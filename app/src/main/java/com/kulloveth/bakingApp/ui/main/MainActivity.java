package com.kulloveth.bakingApp.ui.main;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kulloveth.bakingApp.AppUtils;
import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.ActivityMainBinding;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.ui.adapters.MainAdapter;
import com.kulloveth.bakingApp.utils.ProgressListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ProgressListener {

    private MainAdapter adapter;
    private MainActivityViewModel viewModel;
    ActivityMainBinding binding;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recipeToolbar.setTitle("Recipes");
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.setProgressListener(this);
        adapter = new MainAdapter();
        recyclerView = binding.recipeRv;
        recyclerView.setAdapter(adapter);
        setLayoutManager();
        getRecipe();


    }

    void getRecipe() {
        viewModel.getRecipe().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (!recipes.isEmpty()) {
                    adapter.submitList(recipes);
                }
            }
        });
    }

    private void setLayoutManager() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

    @Override
    public void showLoading() {
        if (AppUtils.isConnected(this)) {
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
        Snackbar.make(getWindow().getDecorView(), getString(R.string.no_internet_message), Snackbar.LENGTH_SHORT).show();
    }
}
