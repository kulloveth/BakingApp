package com.kulloveth.bakingApp.ui.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.kulloveth.bakingApp.databinding.ActivityDetailBinding;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.ui.fragments.DetailFragment;
import com.kulloveth.bakingApp.ui.main.MainActivityViewModel;


public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    MainActivityViewModel viewModel;
    RecipeDetailListener detailListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    public void setDetailListener(RecipeDetailListener detailListener) {
        this.detailListener = detailListener;
    }

    public interface RecipeDetailListener {
        void recipeDetail(Recipe recipe);
    }
}
