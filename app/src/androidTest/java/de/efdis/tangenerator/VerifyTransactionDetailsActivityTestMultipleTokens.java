/*
 * Copyright (c) 2019 EFDIS AG Bankensoftware, Freising <info@efdis.de>.
 *
 * This file is part of the activeTAN app for Android.
 *
 * The activeTAN app is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The activeTAN app is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the activeTAN app.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.efdis.tangenerator;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.efdis.tangenerator.gui.VerifyTransactionDetailsActivity;
import de.efdis.tangenerator.persistence.database.InMemoryDatabaseRule;

/**
 * Test TAN generation with multiple tokens, i. e., the user has to select a token.
 */
@RunWith(AndroidJUnit4.class)
public class VerifyTransactionDetailsActivityTestMultipleTokens {

    @Rule
    public InMemoryDatabaseRule mockDatabaseRule
            = InMemoryDatabaseRule.withMultipleProtectedTanGenerators();

    @Rule
    public ActivityScenarioRule<VerifyTransactionDetailsActivity> activityScenarioRule
            = new ActivityScenarioRule<>(VerifyTransactionDetailsActivityScreenshots.getIntentWithTestData());

    @Test
    public void selectTokenAndComputeTan() {
        Espresso.onView(ViewMatchers.withId(R.id.button))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(R.string.choose_token_title))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withText(android.R.string.ok))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.generatedTanContainer))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void cancelTokenSelection() {
        Espresso.onView(ViewMatchers.withId(R.id.button))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(R.string.choose_token_title))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withText(android.R.string.cancel))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.generatedTanContainer))
                .check(new ViewAssertion() {
                    @Override
                    public void check(View view, NoMatchingViewException noViewFoundException) {
                        if (view.isShown()) {
                            throw new AssertionError("no TAN must have been computed");
                        }
                    }
                });

        Espresso.onView(ViewMatchers.withId(R.id.button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
