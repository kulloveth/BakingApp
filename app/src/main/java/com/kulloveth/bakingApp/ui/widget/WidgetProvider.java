package com.kulloveth.bakingApp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.kulloveth.bakingApp.R;
import com.kulloveth.bakingApp.model.Ingredient;
import com.kulloveth.bakingApp.model.Step;
import com.kulloveth.bakingApp.ui.activities.RecipeDetailActivity;

import java.util.ArrayList;

import static com.kulloveth.bakingApp.ui.widget.WidgetService.INGREDIENTS_KEY;
import static com.kulloveth.bakingApp.ui.widget.WidgetService.IS_INTENTFROMWDGET_KEY;
import static com.kulloveth.bakingApp.ui.widget.WidgetService.RECIPE_NAME_KEY;
import static com.kulloveth.bakingApp.ui.widget.WidgetService.STEPS_LIST_KEY;

public class WidgetProvider extends AppWidgetProvider {

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             ArrayList<Ingredient> ingredients, String recipeName, ArrayList<Step> steps, int widgetId) {

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.putParcelableArrayListExtra(INGREDIENTS_KEY, ingredients);
        intent.putParcelableArrayListExtra(STEPS_LIST_KEY, steps);
        intent.putExtra(RECIPE_NAME_KEY, recipeName);
        intent.putExtra(IS_INTENTFROMWDGET_KEY, true);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
        remoteViews.removeAllViews(R.id.widget_ingredient_list);
        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
        remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        for (Ingredient ingredient : ingredients) {
            String quantity = String.valueOf(ingredient.getQuantity());
            String measure = ingredient.getMeasure();
            String quantityMeasure = quantity + " " + measure;
            String ingredientName = ingredient.getIngredient();
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            views.setTextViewText(R.id.ingredient_naame, ingredientName);
            views.setTextViewText(R.id.quantity, quantityMeasure);

            remoteViews.addView(R.id.widget_ingredient_list, views);
        }


        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

    public static void updateIngredientWidget(Context context, AppWidgetManager appWidgetManager, ArrayList<Ingredient> ingredients, String recipeName, ArrayList<Step> steps, int[] widgetIds) {
        for (int widgetId : widgetIds) {
            updateWidget(context, appWidgetManager, ingredients, recipeName, steps, widgetId);
        }
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

}
