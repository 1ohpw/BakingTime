package com.wolf.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {
    public static final String RECIPE_NAME = "Nutella Pie";
    public static final String STEP_DESCRIPTION = "Recipe Introduction";
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void walkthrough() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recipe_card_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(RECIPE_NAME)));

        onView(withId(R.id.steps_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.step_description_textview))
                .check(matches(withText(STEP_DESCRIPTION)));
    }


}