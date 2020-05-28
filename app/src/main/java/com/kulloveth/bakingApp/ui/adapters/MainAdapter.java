package com.kulloveth.bakingApp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.databinding.RecipeItemBinding;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.model.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapter extends ListAdapter<Recipe, MainAdapter.MainViewholder> {


    public MainAdapter() {
        super(sCallback);
    }

    @NonNull
    @Override
    public MainViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeItemBinding binding = RecipeItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewholder holder, int position) {
        Recipe recipe = getItem(position);
        holder.recipeName.setText(recipe.getName());
        List<Step> steps = recipe.getSteps();
        holder.steps.setText(holder.itemView.getContext().getString(R.string.steps) + steps.size());
        if (!recipe.getImage().equals("")) {
            Picasso.get().load(recipe.getImage()).placeholder(R.drawable.snack)
                    .into(holder.recipeImage);
        } else {
            Picasso.get().load(R.drawable.snack).placeholder(R.drawable.snack)
                    .into(holder.recipeImage);
        }


    }

    class MainViewholder extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private ImageView recipeImage;
        private TextView steps;

        public MainViewholder(RecipeItemBinding binding) {
            super(binding.getRoot());
            recipeName = binding.recipeName;
            recipeImage = binding.recipeIv;
            steps = binding.steps;
        }
    }

    static DiffUtil.ItemCallback<Recipe> sCallback = new DiffUtil.ItemCallback<Recipe>() {
        @Override
        public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.getId() == (newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.equals(newItem);
        }
    };
}
