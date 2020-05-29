package com.kulloveth.bakingApp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.model.Ingredient;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.ui.main.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class WidgetService extends RemoteViewsService {
    MainActivityViewModel viewModel;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsService(this.getApplicationContext(), intent);
    }

    class WidgetRemoteViewsService implements RemoteViewsFactory {
        private List<Ingredient> ingredients = new ArrayList<>();
        private Context context;

        public WidgetRemoteViewsService(Context context, Intent intent) {
            this.context = context;

        }

        @Override
        public void onCreate() {
            viewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(MainActivityViewModel.class);
        }

        @Override
        public void onDataSetChanged() {
            ingredients.clear();
            viewModel.getRecipeLivedata().observe((LifecycleOwner) context.getApplicationContext(), new Observer<Recipe>() {
                @Override
                public void onChanged(Recipe recipe) {
                    ingredients = recipe.getIngredients();
                }
            });

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            Ingredient ingredient = ingredients.get(position);
            remoteViews.setTextViewText(R.id.ingredient_naame, ingredient.getIngredient());
            remoteViews.setTextViewText(R.id.quantity, ingredient.getMeasure() + " " + ingredient.getQuantity());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
