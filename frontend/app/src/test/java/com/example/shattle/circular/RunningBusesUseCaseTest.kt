package com.example.shattle.circular

import com.example.shattle.data.models.Bus
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.ui.circular.RunningBusesDataSource
import com.example.shattle.ui.circular.RunningBusesRepository
import com.example.shattle.ui.circular.RunningBusesUseCase
import com.google.android.gms.maps.model.LatLng
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class RunningBusesUseCaseTest {

    @Mock
    private lateinit var mockRunningBusesRepository: RunningBusesRepository

    @Mock
    private lateinit var mockRunningBusesDataSource: RunningBusesDataSource

    private lateinit var runningBusesUseCase: RunningBusesUseCase

    val ERROR_BODY_IS_NULL = RunningBuses(true, -3)
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = RunningBuses(true, -4)
    val ERROR_ON_FAILURE = RunningBuses(true, -5)
    val DEFAULT_VALUE = RunningBuses(true, -2)

    val runningBuses_1 = RunningBuses(1, listOf(Bus(0, LatLng(0.0,0.0))))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(mockRunningBusesRepository.runningBusesDataSource).thenReturn(
            mockRunningBusesDataSource
        )
        runningBusesUseCase = RunningBusesUseCase(mockRunningBusesRepository)
    }

    @Test
    fun refreshDataTest() {
        // Arrange
        // No arrangement needed for this test

        // Act
        runningBusesUseCase.refreshData()

        // Assert
        // Verify that the refreshRunningBuses method is called on the repository
        verify(mockRunningBusesRepository).refreshRunningBuses()
    }

    @Test
    fun getRunningBusesTest() {
        // Arrange
        val expectedRunningBuses = runningBuses_1
        `when`(mockRunningBusesRepository.runningBusesDataSource.getRunningBuses()).thenReturn(
            expectedRunningBuses
        )

        // Act
        val result = runningBusesUseCase.getRunningBuses()

        // Assert
        assertEquals(expectedRunningBuses, result)
    }

    @Test
    fun getRunningBuses_prevTest() {
        // Arrange
        val expectedRunningBuses = runningBuses_1
        `when`(mockRunningBusesRepository.runningBusesDataSource.getRunningBuses_prev()).thenReturn(
            expectedRunningBuses
        )

        // Act
        val result = runningBusesUseCase.getRunningBuses_prev()

        // Assert
        assertEquals(expectedRunningBuses, result)
    }

    @Test
    fun getErrorIdTest() {
        // Arrange
        val expectedNumBusesRunning = 5
        val runningBuses = RunningBuses(expectedNumBusesRunning, emptyList())
        `when`(mockRunningBusesRepository.runningBusesDataSource.getRunningBuses()).thenReturn(runningBuses)

        // Act
        val errorId = runningBusesUseCase.getErrorId()

        // Assert
        assert(errorId == expectedNumBusesRunning)
    }
}