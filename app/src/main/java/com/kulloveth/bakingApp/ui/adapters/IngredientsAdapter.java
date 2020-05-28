package com.kulloveth.bakingApp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kulloveth.bakingApp.databinding.IngridientsItemBinding;
import com.kulloveth.bakingApp.model.Ingredient;

public class IngredientsAdapter extends ListAdapter<Ingredient, IngredientsAdapter.IngredientViewHolder> {


    public IngredientsAdapter() {
        super(sCallback);
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IngridientsItemBinding binding = IngridientsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = getItem(position);
        holder.ingredientName.setText(ingredient.getIngredient());
        holder.quantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.measure.setText(ingredient.getMeasure());


    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientName;
        private TextView quantity;
        private TextView measure;

        public IngredientViewHolder(IngridientsItemBinding binding) {
            super(binding.getRoot());
            ingredientName = binding.name;
            quantity = binding.quantity;
            measure = binding.measure;
        }
    }

    static DiffUtil.ItemCallback<Ingredient> sCallback = new DiffUtil.ItemCallback<Ingredient>() {
        @Override
        public boolean areItemsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
            return oldItem.getId() == (newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
            return oldItem.equals(newItem);
        }
    };
}
