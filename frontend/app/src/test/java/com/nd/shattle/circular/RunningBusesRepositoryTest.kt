package com.nd.shattle.dropoff

import android.util.Log
import com.nd.shattle.data.models.CurrentLine
import com.nd.shattle.data.models.RunningBuses
import com.nd.shattle.network.ApiService
import com.nd.shattle.network.NetworkCallback
import com.nd.shattle.network.ServiceCreator
import com.nd.shattle.ui.circular.RunningBusesDataSource
import com.nd.shattle.ui.circular.RunningBusesRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class RunningBusesRepositoryTest {

    @Mock
    private lateinit var mockRunningBusesDataSource: RunningBusesDataSource

    @Mock
    private lateinit var mockApiService: ApiService

    @Mock
    private lateinit var mockCall: Call<RunningBuses>

    @Captor
    private lateinit var callbackCaptor: ArgumentCaptor<Callback<RunningBuses>>

    private lateinit var runningBusesRepository: RunningBusesRepository

    val runningBuses_default = RunningBuses(true, -2)
    val runningBuses_1 = RunningBuses(true, 1)
    val runningBuses_2 = RunningBuses(true, 2)

    val mockNetworkCallback: NetworkCallback = mock(NetworkCallback::class.java)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        ServiceCreator.setTestApiService(mockApiService)
        `when`(mockApiService.getRunningBuses()).thenReturn(mockCall)

        runningBusesRepository = RunningBusesRepository(mockRunningBusesDataSource)

        runningBusesRepository.runningBuses = runningBuses_1
        runningBusesRepository.runningBuses_prev = runningBuses_default

    }

    @Test
    fun refreshRunningBusesWithSuccessfulResponse() {
        // Arrange
        val mockResponse = Response.success(runningBuses_2)
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback = invocation.getArgument(0, Callback::class.java) as Callback<RunningBuses>
            callback.onResponse(mockCall, mockResponse)
        }

        // Act
        runningBusesRepository.refreshRunningBuses(mockNetworkCallback)

        // Assert
        verify(mockRunningBusesDataSource).storeRunningBuses(mockResponse.body()!!)
        verify(mockNetworkCallback).onCompleted()
    }

    @Test
    fun refreshRunningBusesWithFailedResponse() {
        // Arrange
        val failedResponse: Response<RunningBuses> = Response.error(404, okhttp3.ResponseBody.create(null, ""))
        `when`(mockCall.enqueue(callbackCaptor.capture())).then {
            callbackCaptor.value.onResponse(mockCall, failedResponse)
        }

        // Act
        runningBusesRepository.refreshRunningBuses(mockNetworkCallback)

        // Assert
        verify(mockRunningBusesDataSource).storeRunningBuses(runningBusesRepository.ERROR_RESPONSE_IS_NOT_SUCCESSFUL)
        verify(mockNetworkCallback).onCompleted()
    }

    @Test
    fun refreshRunningBusesWithOnFailure() {
        // Arrange
        val throwable = Throwable("Network failure")
        `when`(mockCall.enqueue(callbackCaptor.capture())).then {
            callbackCaptor.value.onFailure(mockCall, throwable)
        }

        // Act
        runningBusesRepository.refreshRunningBuses(mockNetworkCallback)

        // Assert
        verify(mockRunningBusesDataSource).storeRunningBuses(runningBusesRepository.ERROR_ON_FAILURE)
        verify(mockNetworkCallback).onFailure(throwable)
    }

}