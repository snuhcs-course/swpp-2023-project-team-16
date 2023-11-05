package com.example.shattle;

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4::class)
class CircularTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.navigation_circular))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.navigation_circular))
            .check(ViewAssertions.matches(ViewMatchers.isSelected()))
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun initView() {

    }

    @Test
    fun refreshView() {

    }
}
