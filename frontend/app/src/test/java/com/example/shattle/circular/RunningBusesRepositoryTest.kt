package com.example.shattle.dropoff

import android.util.Log
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.network.ApiService
import com.example.shattle.network.ServiceCreator
import com.example.shattle.ui.circular.RunningBusesDataSource
import com.example.shattle.ui.circular.RunningBusesRepository
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.hamcrest.core.AnyOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.lenient
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class RunningBusesRepositoryTest {
    // TODO: call function test
    // 일단 verify() 없이 coverage 100% 되게 짜기
    // verify() 추가

    @Mock
    private lateinit var mockRunningBusesDataSource: RunningBusesDataSource

    @Mock
    private lateinit var mockApiService: ApiService

    @Mock
    private lateinit var mockCall: Call<RunningBuses>

    private lateinit var runningBusesRepository: RunningBusesRepository

    @Before
    fun setUp() {
        lenient().`when`(mockApiService.getRunningBuses()).thenReturn(mockCall)
        runningBusesRepository = RunningBusesRepository(mockRunningBusesDataSource)

    }

    @Test
    fun refreshRunningBusesWithSuccessfulResponse() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    fun refreshRunningBusesWithFailedResponse() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    fun refreshRunningBusesWithOnFailure() {
        // Arrange

        // Act

        // Assert
    }

}