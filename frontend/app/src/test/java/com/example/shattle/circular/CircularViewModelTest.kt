package com.example.shattle.circular

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.ui.circular.CircularUIState
import com.example.shattle.ui.circular.CircularViewModel
import com.example.shattle.ui.circular.RunningBusesUseCase
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
class CircularViewModelTest {
    // TODO: getData Test Assert 다시 확인하기

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRunningBusesUseCase: RunningBusesUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<CircularUIState?>

    @Mock
    private lateinit var toastMessageObserver: Observer<String>

    private lateinit var viewModel: CircularViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = CircularViewModel().apply {
            getUIState().observeForever(uiStateObserver)
            getToastMessage().observeForever(toastMessageObserver)
        }
    }

    @Test
    fun getDataWithInvalidResponse() {
        // Arrange
        val runningBuses_prev = RunningBuses(true, 2)
        `when`(mockRunningBusesUseCase.getErrorId()).thenReturn(-3)
        `when`(mockRunningBusesUseCase.getRunningBuses_prev()).thenReturn(runningBuses_prev)

        // Act
        viewModel.getData(mockRunningBusesUseCase)

        // Assert
        verify(toastMessageObserver).onChanged("업데이트 중 오류가 발생했습니다. 잠시 후 다시 시도하세요.")
        //verify(uiStateObserver).onChanged(CircularUIState(runningBuses_prev))
    }

    @Test
    fun getDataWithSuccessfulResponse() {
        // Arrange
        val runningBuses = RunningBuses(true, 2)
        `when`(mockRunningBusesUseCase.getRunningBuses()).thenReturn(runningBuses)

        // Act
        viewModel.getData(mockRunningBusesUseCase)

        // Assert
        //verify(uiStateObserver).onChanged(CircularUIState(runningBuses))
    }

    @Test
    fun getDataWithNoRunningBuses() {
        // Arrange
        val runningBuses = RunningBuses(true, 0)
        `when`(mockRunningBusesUseCase.getErrorId()).thenReturn(runningBuses.numBusesRunning)
        `when`(mockRunningBusesUseCase.getRunningBuses()).thenReturn(runningBuses)

        // Act
        viewModel.getData(mockRunningBusesUseCase)

        // Assert
        verify(toastMessageObserver).onChanged("현재 운행중인 셔틀이 없습니다.")
        //verify(uiStateObserver).onChanged(CircularUIState(runningBuses))
    }

    @Test
    fun notifyRefreshTest() {
        // Arrange

        // Act
        viewModel.notifyRefresh(mockRunningBusesUseCase)

        // Assert
        verify(mockRunningBusesUseCase).refreshData()
    }

}