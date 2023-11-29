package com.example.shattle

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotFocused
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
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
        // 앱 실행 시 메뉴(바텀네비게이션)와 시작화면(하교셔틀 페이지)이 나오는지 확인

        onView(withId(R.id.nav_view))
            .check(matches(isDisplayed()))

        onView(withId(R.id.navigation_dropoff))
            .check(matches(isSelected()))
    }


    @Test
    fun testBottomNavigationItemClick() {
        // 각각 네비게이션 바로 화면이 전환되는지 확인

        // 하교셔틀
        onView(withId(R.id.navigation_dropoff))
            .perform(click())

        onView(withId(R.id.navigation_dropoff))
            .check(matches(isSelected()))

        // 순환셔틀
        onView(withId(R.id.navigation_circular))
            .perform(click())

        onView(withId(R.id.navigation_circular))
            .check(matches(isSelected()))

        // 시간표
        onView(withId(R.id.navigation_timetable))
            .perform(click())
        onView(withId(R.id.navigation_timetable))
            .check(matches(isSelected()))
    }

    @Test
    fun testInfoDialogDisplay() {

        // Info 열기
        onView(withId(R.id.infoImageButton))
            .perform(click())

        onView(withId(R.id.infoDialog))
            .check(matches(isDisplayed()))

        // Info 가 열려있을 때 메뉴 버튼 비활성화되는지 확인
        onView(withId(R.id.navigation_dropoff))
            .check(matches(isNotEnabled()))

        onView(withId(R.id.navigation_circular))
            .check(matches(isNotEnabled()))

        onView(withId(R.id.navigation_timetable))
            .check(matches(isNotEnabled()))

        // 버튼으로 Info 닫기
        onView(withId(R.id.closeImageButton))
            .perform(click())

        onView(withId(R.id.infoDialog))
            .check(doesNotExist())

        // 뒤로가기 버튼으로 닫기
        onView(withId(R.id.infoImageButton))
            .perform(click())

        onView(isRoot())
            .perform(pressBack())

        onView(withId(R.id.infoDialog))
            .check(doesNotExist())
    }
}