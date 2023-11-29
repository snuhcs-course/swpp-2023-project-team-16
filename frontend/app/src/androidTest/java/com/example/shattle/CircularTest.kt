package com.example.shattle;

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.shattle.network.ApiService
import com.example.shattle.network.ServiceCreator
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith;
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class CircularTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var mockWebServer: MockWebServer
    private val idlingResource = CountingIdlingResource("DataLoader")

    private val gsonBody = """
                {
                    "num_buses_running": 2,
                    "buses": [],
                    "latest_location_updated_at": "2023-01-02 03:04:05.678900+00:00"
                }
            """.trimIndent()

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.navigation_circular))
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


        onView(withId(R.id.updatedTimeTextView))
            .check(matches(ViewMatchers.withText("최종 업데이트 - 01.02 03:04:05")))
    }
}
