package com.example.shattle

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testActivityLaunch() {
        onView(withId(R.id.nav_view))
            .check(matches(isDisplayed()))
    }


    @Test
    fun testBottomNavigationItemClick() {

        // dropoff menu Button
        onView(withId(R.id.navigation_dropoff))
            .perform(click())

        onView(withId(R.id.navigation_dropoff))
            .check(matches(isSelected()))

        // circular menu Button
        onView(withId(R.id.navigation_circular))
            .perform(click())

        onView(withId(R.id.navigation_circular))
            .check(matches(isSelected()))

        // timetable menu button
        onView(withId(R.id.navigation_timetable))
            .perform(click())

        onView(withId(R.id.navigation_timetable))
            .check(matches(isSelected()))
    }

    @Test
    fun testInfoDialogDisplay() {

        // Act
        onView(withId(R.id.infoImageButton))
            .perform(click())

        // Assert
        onView(withId(R.id.infoContainer))
            .check(matches(isDisplayed()))
    }
}