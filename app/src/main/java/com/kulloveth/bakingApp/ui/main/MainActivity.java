package com.kulloveth.bakingApp.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.ActivityMainBinding;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.ui.adapters.MainAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        adapter = new MainAdapter();
        recyclerView = binding.recipeRv;
        recyclerView.setAdapter(adapter);
       setLayoutManager();
        //recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_spans)));
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
        }
        if(isTablet){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

}
