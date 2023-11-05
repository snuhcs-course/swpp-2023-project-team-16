package com.example.shattle.circular

import com.example.shattle.data.models.Bus
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.ui.circular.RunningBusesDataSource
import com.example.shattle.ui.circular.RunningBusesRepository
import com.example.shattle.ui.circular.RunningBusesUseCase
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

    val ERROR_BODY_IS_NULL = RunningBuses(-3, emptyList())
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = RunningBuses(-4, emptyList())
    val ERROR_ON_FAILURE = RunningBuses(-5, emptyList())
    val DEFAULT_VALUE = RunningBuses(-2, emptyList())

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
        val expectedRunningBuses = RunningBuses(1, listOf(Bus(0, "a", 0.0, 0.0, true, true)))
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
        val expectedRunningBuses = RunningBuses(1, listOf(Bus(0, "a", 0.0, 0.0, true, true)))
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