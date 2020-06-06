package com.kulloveth.bakingApp.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.kulloveth.bakingApp.model.Ingredient;
import com.kulloveth.bakingApp.model.Step;

import java.util.ArrayList;

import static com.kulloveth.bakingApp.utils.Constants.ACTOIN_UPDATE_KEY;
import static com.kulloveth.bakingApp.utils.Constants.INGREDIENTS_KEY;
import static com.kulloveth.bakingApp.utils.Constants.RECIPE_NAME_KEY;
import static com.kulloveth.bakingApp.utils.Constants.STEPS_LIST_KEY;

public class WidgetService extends IntentService {


    public WidgetService() {
        super("WidgetService");
    }

    public static void actionUpdateWidget(Context context, ArrayList<Ingredient> ingredients, String recipeName, ArrayList<Step> steps) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTOIN_UPDATE_KEY);
        intent.putParcelableArrayListExtra(INGREDIENTS_KEY, ingredients);
        intent.putParcelableArrayListExtra(STEPS_LIST_KEY, steps);
        intent.putExtra(RECIPE_NAME_KEY, recipeName);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTOIN_UPDATE_KEY.equals(action)) {
                ArrayList<Ingredient> ingredients = intent.getParcelableArrayListExtra(INGREDIENTS_KEY);
                ArrayList<Step> steps = intent.getParcelableArrayListExtra(STEPS_LIST_KEY);
                String recipeName = intent.getStringExtra(RECIPE_NAME_KEY);
                handleActionUpdate(ingredients, recipeName, steps);
            }
        }
    }

    private void handleActionUpdate(ArrayList<Ingredient> ingredients, String recipeName, ArrayList<Step> steps) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        WidgetProvider.updateIngredientWidget(this, appWidgetManager, ingredients, recipeName, steps, widgetIds);
    }
}
