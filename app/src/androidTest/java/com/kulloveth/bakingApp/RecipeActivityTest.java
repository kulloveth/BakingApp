package com.kulloveth.bakingApp;


import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.kulloveth.bakingApp.ui.activities.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(JUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void onClickRecyclerViewItemNutellaPie_OpensItsDetail() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(ViewMatchers.withId(R.id.recipe_rv))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Nutella Pie")), click()));
    }

    @Test
    public void onClickRecyclerViewItemBrownies_OpensItsDetail() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(ViewMatchers.withId(R.id.recipe_rv))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Brownies")), click()));
    }

    @Test
    public void onClickRecyclerViewItemYellowCake_OpensItsDetail() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(ViewMatchers.withId(R.id.recipe_rv))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Yellow Cake")), click()));
    }

    @Test
    public void onClickRecyclerViewItemCheeseCake_OpensItsDetail() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(ViewMatchers.withId(R.id.recipe_rv))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Cheesecake")), click()));
    }
}
