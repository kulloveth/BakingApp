package com.kulloveth.bakingApp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.IngredientDetailBinding;
import com.kulloveth.bakingApp.model.Ingredient;
import com.kulloveth.bakingApp.ui.adapters.IngredientsAdapter;

import java.util.ArrayList;

import static com.kulloveth.bakingApp.utils.Constants.INGREDIENTS_KEY;
import static com.kulloveth.bakingApp.utils.Constants.RECIPE_NAME_KEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientFragment extends Fragment {

    public static final String RECIPE_KEY = "steps-key";
    private IngredientDetailBinding binding;
    private IngredientsAdapter ingredientsAdapter;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private String recipeName;

    public IngredientFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = IngredientDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (savedInstanceState != null) {
            ingredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_KEY);
        }

        ingredientsAdapter = new IngredientsAdapter();
        RecyclerView ingredientsRecyclerView = binding.ingredientsRv;
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        getIngredients();
    }

    private void getIngredients() {
        ingredientsAdapter.submitList(ingredients);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RECIPE_NAME_KEY, recipeName);
        outState.putParcelableArrayList(INGREDIENTS_KEY, ingredients);


    }
}
