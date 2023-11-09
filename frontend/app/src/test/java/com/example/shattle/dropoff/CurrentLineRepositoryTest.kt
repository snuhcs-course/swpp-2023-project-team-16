package com.example.shattle.dropoff

import android.util.Log
import com.example.shattle.data.models.CurrentLine
import com.example.shattle.network.ApiService
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class CurrentLineRepositoryTest {
    // TODO: call function test
    // 일단 verify() 없이 coverage 100% 되게 짜기 (call method 각각의 결과 반영되게)
    // verify() 추가

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
    val currentLine_1 = CurrentLine(1, 1, 1, "")

    @Before
    fun setUp() {
        lenient().`when`(mockApiService.getCurrentLine()).thenReturn(mockCall)

        currentLineRepository = CurrentLineRepository(mockCurrentLineDataSource)

        currentLineRepository.currentLine = currentLine_default
        currentLineRepository.currentLine_prev = currentLine_default
    }

    var cnt = 0
    @Test
    fun refreshCurrentLineWithSuccessfulResponse() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    fun refreshCurrentLineWithFailedResponse() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    fun refreshCurrentLineWithOnFailure() {
        // Arrange

        // Act

        // Assert

    }

}