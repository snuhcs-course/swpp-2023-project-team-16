package com.example.shattle.dropoff

import android.util.Log
import com.example.shattle.data.models.CurrentLine
import com.example.shattle.network.ApiService
import com.example.shattle.network.NetworkCallback
import com.example.shattle.network.ServiceCreator
import com.example.shattle.ui.dropoff.CurrentLineDataSource
import com.example.shattle.ui.dropoff.CurrentLineRepository
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
import org.mockito.kotlin.anyOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class CurrentLineRepositoryTest {

    @Mock
    private lateinit var mockCurrentLineDataSource: CurrentLineDataSource

    @Mock
    private lateinit var mockApiService: ApiService

    @Mock
    private lateinit var mockCall: Call<CurrentLine>

    @Captor
    private lateinit var callbackCaptor: ArgumentCaptor<Callback<CurrentLine>>


    private lateinit var currentLineRepository: CurrentLineRepository

    val currentLine_default = CurrentLine(true, -2)
    val currentLine_1 = CurrentLine(true, 1)
    val currentLine_2 = CurrentLine(true, 2)

    val mockNetworkCallback: NetworkCallback = mock(NetworkCallback::class.java)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        ServiceCreator.setTestApiService(mockApiService)
        `when`(mockApiService.getCurrentLine()).thenReturn(mockCall)
        currentLineRepository = CurrentLineRepository(mockCurrentLineDataSource)

        currentLineRepository.currentLine = currentLine_1
        currentLineRepository.currentLine_prev = currentLine_default
    }

    var cnt = 0
    @Test
    fun refreshCurrentLineWithSuccessfulResponse() {

        // Arrange
        val mockResponse = Response.success(currentLine_2)
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback = invocation.getArgument(0, Callback::class.java) as Callback<CurrentLine>
            callback.onResponse(mockCall, mockResponse)
        }

        // Act
        currentLineRepository.refreshCurrentLine(mockNetworkCallback)

        // Assert
        verify(mockCurrentLineDataSource).storeCurrentLine(mockResponse.body()!!)
        verify(mockNetworkCallback).onCompleted()
    }

    @Test
    fun refreshCurrentLineWithFailedResponse() {

        // Arrange
        val failedResponse: Response<CurrentLine> = Response.error(404, okhttp3.ResponseBody.create(null, ""))
        `when`(mockCall.enqueue(callbackCaptor.capture())).then {
            callbackCaptor.value.onResponse(mockCall, failedResponse)
        }

        // Act
        currentLineRepository.refreshCurrentLine(mockNetworkCallback)

        // Assert
        verify(mockCurrentLineDataSource).storeCurrentLine(currentLineRepository.ERROR_RESPONSE_IS_NOT_SUCCESSFUL)
        verify(mockNetworkCallback).onCompleted()
    }

    @Test
    fun refreshCurrentLineWithOnFailure() {

        // Arrange
        val throwable = Throwable("Network failure")
        `when`(mockCall.enqueue(callbackCaptor.capture())).then {
            callbackCaptor.value.onFailure(mockCall, throwable)
        }

        // Act
        currentLineRepository.refreshCurrentLine(mockNetworkCallback)

        // Assert
        verify(mockCurrentLineDataSource).storeCurrentLine(currentLineRepository.ERROR_ON_FAILURE)
        verify(mockNetworkCallback).onFailure(throwable)
    }

}