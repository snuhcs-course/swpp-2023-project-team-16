package com.example.shattle.dropoff

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shattle.data.models.CurrentLine
import com.example.shattle.network.NetworkCallback
import com.example.shattle.ui.dropoff.CurrentLineUseCase
import com.example.shattle.ui.dropoff.DropoffUIState
import com.example.shattle.ui.dropoff.DropoffViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull

@RunWith(MockitoJUnitRunner::class)
class DropoffViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockCurrentLineUseCase: CurrentLineUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<DropoffUIState?>

    @Mock
    private lateinit var toastMessageObserver: Observer<String>

    @Mock
    private lateinit var networkRequestStatusObserver: Observer<Boolean?>

    private lateinit var viewModel: DropoffViewModel

    val currentLine_123 = CurrentLine(1,2,3,"")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = DropoffViewModel().apply {
            getUIState().observeForever(uiStateObserver)
            getToastMessage().observeForever(toastMessageObserver)
            getNetworkRequestStatus().observeForever(networkRequestStatusObserver)
        }
    }

    @Test
    fun getDataWithInvalidResponse() {
        // Arrange
        val currentLine_prev = currentLine_123
        `when`(mockCurrentLineUseCase.isValidResponse()).thenReturn(false) // response 가 -3 or -4 or -5
        `when`(mockCurrentLineUseCase.getCurrentLine_prev()).thenReturn(currentLine_prev)

        // Act
        viewModel.getData(mockCurrentLineUseCase)

        // Assert
        verify(toastMessageObserver).onChanged("업데이트 중 오류가 발생했습니다. 잠시 후 다시 시도하세요.")
        verify(uiStateObserver).onChanged(DropoffUIState(currentLine_prev))
    }

    @Test
    fun getDataWithNoShuttle() {
        // Arrange
        val previousLine = currentLine_123
        `when`(mockCurrentLineUseCase.isValidResponse()).thenReturn(true) // response 는 성공적
        `when`(mockCurrentLineUseCase.isNoShuttle()).thenReturn(true)
        `when`(mockCurrentLineUseCase.getCurrentLine_prev()).thenReturn(previousLine)

        // Act
        viewModel.getData(mockCurrentLineUseCase)

        // Assert
        verify(toastMessageObserver).onChanged("현재 셔틀 운행 시간이 아닙니다.")
        verify(uiStateObserver).onChanged(DropoffUIState(previousLine))
    }

    @Test
    fun getDataWithSuccessfulResponse() {
        // Arrange
        val currentLine = currentLine_123
        `when`(mockCurrentLineUseCase.isValidResponse()).thenReturn(true)
        `when`(mockCurrentLineUseCase.getCurrentLine()).thenReturn(currentLine)

        // Act
        viewModel.getData(mockCurrentLineUseCase)

        // Assert
        verify(toastMessageObserver).onChanged("업데이트 성공!")
        verify(uiStateObserver).onChanged(DropoffUIState(currentLine))
    }

    @Test
    fun notifyRefreshTest() {
        // Arrange
        doAnswer { invocation ->
            val callback = invocation.getArgument(0) as NetworkCallback
            callback.onCompleted()
            null
        }.`when`(mockCurrentLineUseCase).refreshData(anyOrNull())

        // Act
        viewModel.notifyRefresh(mockCurrentLineUseCase)

        // Assert
        verify(networkRequestStatusObserver).onChanged(true)
    }
}