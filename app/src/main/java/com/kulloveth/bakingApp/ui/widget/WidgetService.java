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

public class WidgetService extends IntentService {

    public static final String INGREDIENTS_KEY = "ingredients_key";
    public static final String RECIPE_NAME_KEY = "recipe_name_key";
    public static final String STEPS_LIST_KEY = "steps_list_key";
    public static final String IS_INTENTFROMWDGET_KEY = "isintentfrom_key";
    public static final String ACTOIN_UPDATE_KEY = "actionupdate_key";

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
