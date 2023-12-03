package com.example.shattle

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import okhttp3.mockwebserver.MockWebServer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shattle.network.ApiService
import com.example.shattle.network.ServiceCreator
import com.example.shattle.ui.dropoff.DropoffFragment
import com.example.shattle.ui.dropoff.DropoffUIState
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.Description
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class DropoffTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var mockWebServer: MockWebServer
    private val idlingResource = CountingIdlingResource("DataLoader")

    private val gsonBody = """
                {
                    "num_waiting_people": 60,
                    "num_needed_bus": 2,
                    "waiting_time": 10,
                    "updated_at": "2023-01-02 03:04:05.678900+00:00"
                }
            """.trimIndent()

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.navigation_dropoff))
            .perform(click())

        IdlingRegistry.getInstance().register(idlingResource)

        mockWebServer = MockWebServer()
        mockWebServer.start()

        val mockedResponse = MockResponse()
            .setResponseCode(200)
            .setBody(gsonBody)
        mockWebServer.enqueue(mockedResponse)

        val testRetrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        ServiceCreator.setTestApiService(testRetrofit.create(ApiService::class.java))
    }

    @After
    fun tearDown() {
        scenario.close()
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun refreshViewTest() {

        onView(withId(R.id.refreshButton))
            .perform(click())

//            {
//                "num_waiting_people": 60,
//                "num_needed_bus": 2,
//                "waiting_time": 10,
//                "updated_at": "2023-01-02 03:04:05.678900+00:00"
//            }

        onView(withId(R.id.numPeopleTextView))
            .check(matches(withText("대기인원 60 명 ")))

        onView(withId(R.id.numTimeTextView))
            .check(matches(withText("예상 대기시간 10 분 ")))

        onView(withId(R.id.numBusTextView))
            .check(matches(withText("기다려야 하는 버스 2 대 ")))

        onView(withId(R.id.manImageView))
            .check(matches(withHorizontalBias(0.5f)))

        onView(withId(R.id.updatedTimeTextView))
            .check(matches(withText("최종 업데이트 - 01.02 03:04:05")))
    }

    private fun withHorizontalBias(expectedBias: Float) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("with horizontal bias: $expectedBias")
        }

        override fun matchesSafely(item: View): Boolean {
            val layoutParams = item.layoutParams as ConstraintLayout.LayoutParams
            return layoutParams.horizontalBias == expectedBias
        }
    }
}