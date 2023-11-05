package com.example.shattle.dropoff

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shattle.data.models.CurrentLine
import com.example.shattle.ui.dropoff.CurrentLineUseCase
import com.example.shattle.ui.dropoff.DropoffUIState
import com.example.shattle.ui.dropoff.DropoffViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

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

    private lateinit var viewModel: DropoffViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = DropoffViewModel().apply {
            getUIState().observeForever(uiStateObserver)
            getToastMessage().observeForever(toastMessageObserver)
        }
    }

    @Test
    fun getDataWithInvalidResponse() {
        // Arrange
        val currentLine_prev = CurrentLine(1, 2, 3, "")
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
        val previousLine = CurrentLine(1, 2, 3, "")
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
        val currentLine = CurrentLine(1, 2, 3, "")
        `when`(mockCurrentLineUseCase.isValidResponse()).thenReturn(true)
        `when`(mockCurrentLineUseCase.getCurrentLine()).thenReturn(currentLine)

        // Act
        viewModel.getData(mockCurrentLineUseCase)

        // Assert
        verify(uiStateObserver).onChanged(DropoffUIState(currentLine))
    }

    @Test
    fun notifyRefreshTest() {
        // Arrange

        // Act
        viewModel.notifyRefresh(mockCurrentLineUseCase)

        // Assert
        verify(mockCurrentLineUseCase).refreshData()
    }
}