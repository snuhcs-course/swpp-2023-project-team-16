package com.example.shattle.circular

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.network.NetworkCallback
import com.example.shattle.ui.circular.CircularUIState
import com.example.shattle.ui.circular.CircularViewModel
import com.example.shattle.ui.circular.RunningBusesUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull

@RunWith(MockitoJUnitRunner::class)
class CircularViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRunningBusesUseCase: RunningBusesUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<CircularUIState?>

    @Mock
    private lateinit var toastMessageObserver: Observer<String>

    @Mock
    private lateinit var networkRequestStatusObserver: Observer<Boolean?>

    @Mock
    private lateinit var gpsTrackingStatusObserver: Observer<Boolean?>

    private lateinit var viewModel: CircularViewModel

    val runningBuses_1 = RunningBuses(true, 1)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = CircularViewModel().apply {
            getUIState().observeForever(uiStateObserver)
            getToastMessage().observeForever(toastMessageObserver)
            getNetworkRequestStatus().observeForever(networkRequestStatusObserver)
            getGpsTrackingStatus().observeForever(gpsTrackingStatusObserver)
        }
    }

    @Test
    fun getDataWithInvalidResponse() {
        // Arrange
        val runningBuses_prev = runningBuses_1
        `when`(mockRunningBusesUseCase.getErrorId()).thenReturn(-3)
        `when`(mockRunningBusesUseCase.getRunningBuses_prev()).thenReturn(runningBuses_prev)

        // Act
        viewModel.getData(mockRunningBusesUseCase)

        // Assert
        verify(toastMessageObserver).onChanged("업데이트 중 오류가 발생했습니다. 잠시 후 다시 시도하세요.")
        verify(uiStateObserver).onChanged(CircularUIState(runningBuses_prev))
    }

    @Test
    fun getDataWithSuccessfulResponse() {
        // Arrange
        val runningBuses = runningBuses_1
        `when`(mockRunningBusesUseCase.getErrorId()).thenReturn(runningBuses.numBusesRunning)
        `when`(mockRunningBusesUseCase.getRunningBuses()).thenReturn(runningBuses)

        // Act
        viewModel.getData(mockRunningBusesUseCase)

        // Assert
        verify(toastMessageObserver).onChanged("업데이트 성공!")
        verify(uiStateObserver).onChanged(CircularUIState(runningBuses))
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
        verify(uiStateObserver).onChanged(CircularUIState(runningBuses))
    }

    @Test
    fun notifyRefreshTest() {
        // Arrange
        doAnswer { invocation ->
            val callback = invocation.getArgument(0) as NetworkCallback
            callback.onCompleted()
            null
        }.`when`(mockRunningBusesUseCase).refreshData(anyOrNull())

        // Act
        viewModel.notifyRefresh(mockRunningBusesUseCase)

        // Assert
        verify(networkRequestStatusObserver).onChanged(true)
    }

    @Test
    fun notifyRefreshTest2() {
        // Arrange
        doAnswer { invocation ->
            val callback = invocation.getArgument(0) as NetworkCallback
            callback.onFailure(RuntimeException("네트워크 오류"))
            null
        }.`when`(mockRunningBusesUseCase).refreshData(anyOrNull())

        // Act
        viewModel.notifyRefresh(mockRunningBusesUseCase)

        // Assert
        verify(networkRequestStatusObserver).onChanged(true)
    }

}