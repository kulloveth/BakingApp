package com.kulloveth.bakingApp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kulloveth.bakingApp.AppUtils;
import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.ActivityRecipeBinding;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.ui.RecipeActivityViewModel;
import com.kulloveth.bakingApp.ui.adapters.MainAdapter;
import com.kulloveth.bakingApp.utils.ProgressListener;

import static com.kulloveth.bakingApp.ui.fragments.IngredientFragment.RECIPE_KEY;
import static com.kulloveth.bakingApp.utils.Constants.INGREDIENTS_KEY;
import static com.kulloveth.bakingApp.utils.Constants.RECIPE_NAME_KEY;
import static com.kulloveth.bakingApp.utils.Constants.STEPS_LIST_KEY;


public class RecipeActivity extends AppCompatActivity implements ProgressListener, MainAdapter.RecipeItemClickListener {


    private static final String TAG = RecipeActivity.class.getSimpleName();
    private MainAdapter adapter;
    private RecipeActivityViewModel viewModel;
    ActivityRecipeBinding binding;
    RecyclerView recyclerView;
    boolean isTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isTablet = getResources().getBoolean(R.bool.isTablet);
        binding.recipeToolbar.recipeToolbar.setTitle("Recipes");

        viewModel = new ViewModelProvider(this).get(RecipeActivityViewModel.class);
        viewModel.setProgressListener(this);
        adapter = new MainAdapter();
        adapter.setClickListener(this);
        recyclerView = binding.recipeRv;
        setLayoutManager();
        recyclerView.setAdapter(adapter);
        getRecipe();
    }


    private void getRecipe() {
        viewModel.getRecipe().observe(this, recipes -> {
            if (!recipes.isEmpty()) {
                adapter.submitList(recipes);
                Log.d(TAG, "onChanged: " + recipes);
            }
        });
    }

    private void setLayoutManager() {

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

    @Override
    public void recipeItemClicked(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_KEY, recipe);
        intent.putExtra(RECIPE_NAME_KEY, recipe.getName());
        intent.putParcelableArrayListExtra(STEPS_LIST_KEY, recipe.getSteps());
        intent.putParcelableArrayListExtra(INGREDIENTS_KEY, recipe.getIngredients());
        startActivity(intent);

    }
}
