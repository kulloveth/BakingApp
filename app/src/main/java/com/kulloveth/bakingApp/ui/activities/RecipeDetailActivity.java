package com.kulloveth.bakingApp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.ActivityRecipeDetailBinding;
import com.kulloveth.bakingApp.model.Ingredient;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.model.Step;
import com.kulloveth.bakingApp.ui.fragments.IngredientFragment;
import com.kulloveth.bakingApp.ui.fragments.StepDetailFragment;
import com.kulloveth.bakingApp.ui.fragments.StepFragment;
import com.kulloveth.bakingApp.ui.widget.WidgetService;

import java.util.ArrayList;

import static com.kulloveth.bakingApp.ui.fragments.IngredientFragment.RECIPE_KEY;
import static com.kulloveth.bakingApp.utils.Constants.INGREDIENTS_KEY;
import static com.kulloveth.bakingApp.utils.Constants.IS_INTENTFROMWDGET_KEY;
import static com.kulloveth.bakingApp.utils.Constants.RECIPE_NAME_KEY;
import static com.kulloveth.bakingApp.utils.Constants.STEPS_LIST_KEY;

public class RecipeDetailActivity extends AppCompatActivity {

    ActivityRecipeDetailBinding binding;
    Recipe recipe;
    ArrayList<Step> stepList = new ArrayList<>();
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    String recipeName;
    boolean isTablet;
    public static final String IS_TABLET_KEY = "is_tablet_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if (binding.recipeToolbar != null) {
            setSupportActionBar(binding.recipeToolbar.recipeToolbar);
        }

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            recipe = intent.getParcelableExtra(RECIPE_KEY);
            stepList = intent.getParcelableArrayListExtra(STEPS_LIST_KEY);
            ingredients = intent.getParcelableArrayListExtra(INGREDIENTS_KEY);
            recipeName = intent.getStringExtra(RECIPE_NAME_KEY);

            FragmentManager manager = getSupportFragmentManager();
            IngredientFragment fragment = new IngredientFragment();
            fragment.setIngredients(ingredients);
            manager.beginTransaction().add(R.id.ingredient_container, fragment).commit();
            Log.e("reci", "onCreate: " + recipe);

            StepFragment stepFragment = new StepFragment();
            stepFragment.setStepList(stepList);
            manager.beginTransaction().add(R.id.step_container, stepFragment).commit();

            if (isTablet) {
                StepDetailFragment stepsFragment = new StepDetailFragment();
                stepsFragment.setStep(stepList.get(0));
                manager.beginTransaction().add(R.id.step_detail_graph, stepsFragment).commit();
            }
        } else {
            recipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
            ingredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_KEY);
            stepList = savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            isTablet = savedInstanceState.getBoolean(IS_TABLET_KEY);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipeName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(INGREDIENTS_KEY, ingredients);
        outState.putParcelableArrayList(STEPS_LIST_KEY, stepList);
        outState.putString(RECIPE_NAME_KEY, recipeName);
        outState.putBoolean(IS_TABLET_KEY, isTablet);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean isIntentFromAppWidget = getIntent().getBooleanExtra(IS_INTENTFROMWDGET_KEY, false);
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                if (isIntentFromAppWidget) {
                    Intent intent = new Intent(this, RecipeActivity.class);
                    startActivity(intent);
                } else {
                    onBackPressed();
                }
                return true;

            case R.id.update_widget:
                WidgetService.actionUpdateWidget(this, ingredients, recipeName, stepList);
                Snackbar.make(getWindow().getDecorView(), "Widget Updated", Snackbar.LENGTH_LONG).show();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
