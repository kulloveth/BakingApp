package com.kulloveth.bakingApp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kulloveth.bakingApp.databinding.StepItemBinding;
import com.kulloveth.bakingApp.model.Ingredient;
import com.kulloveth.bakingApp.model.Step;

public class StepAdapter extends ListAdapter<Step, StepAdapter.StepViewHolder> {


    public StepAdapter() {
        super(sCallback);
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StepItemBinding binding = StepItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StepViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = getItem(position);
        holder.stepDescription.setText(step.getShortDescription());


    }

    class StepViewHolder extends RecyclerView.ViewHolder {
        private TextView stepDescription;


        public StepViewHolder(StepItemBinding binding) {
            super(binding.getRoot());
            stepDescription = binding.shortDescription;
        }
    }

    static DiffUtil.ItemCallback<Step> sCallback = new DiffUtil.ItemCallback<Step>() {
        @Override
        public boolean areItemsTheSame(@NonNull Step oldItem, @NonNull Step newItem) {
            return oldItem.getId() == (newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Step oldItem, @NonNull Step newItem) {
            return oldItem.equals(newItem);
        }
    };
}
