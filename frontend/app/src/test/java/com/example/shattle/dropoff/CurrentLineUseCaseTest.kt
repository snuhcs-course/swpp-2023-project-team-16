package com.example.shattle.dropoff

import com.example.shattle.data.models.CurrentLine
import com.example.shattle.ui.dropoff.CurrentLineDataSource
import com.example.shattle.ui.dropoff.CurrentLineRepository
import com.example.shattle.ui.dropoff.CurrentLineUseCase
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
class CurrentLineUseCaseTest {

    @Mock
    private lateinit var mockCurrentLineRepository: CurrentLineRepository

    @Mock
    private lateinit var mockCurrentLineDataSource: CurrentLineDataSource

    private lateinit var currentLineUseCase: CurrentLineUseCase

    val ERROR_BODY_IS_NULL = CurrentLine(-3, -3, -3, "")
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = CurrentLine(-4, -4, -4, "")
    val ERROR_ON_FAILURE = CurrentLine(-5, -5, -5, "")
    val DEFAULT_VALUE = CurrentLine(-2, -2, -2, "")

    val currentLine_123 = CurrentLine(1,2,3,"")

    val currentLine_noShuttle = CurrentLine(70, -1, -1, "")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(mockCurrentLineRepository.currentLineDataSource).thenReturn(mockCurrentLineDataSource)
        currentLineUseCase = CurrentLineUseCase(mockCurrentLineRepository)
    }

    @Test
    fun refreshDataTest() {
        // Arrange
        // No arrangement needed for this test

        // Act
        currentLineUseCase.refreshData()

        // Assert
        // Verify that the refreshCurrentLine method is called on the repository
        verify(mockCurrentLineRepository).refreshCurrentLine()
    }

    @Test
    fun getCurrentLineTest() {
        // Arrange
        val expectedCurrentLine = currentLine_123
        `when`(mockCurrentLineRepository.currentLineDataSource.getCurrentLine()).thenReturn(
            expectedCurrentLine
        )

        // Act
        val result = currentLineUseCase.getCurrentLine()

        // Assert
        assertEquals(expectedCurrentLine, result)
    }

    @Test
    fun getCurrentLine_prevTest() {
        // Arrange
        val expectedCurrentLine = currentLine_123
        `when`(mockCurrentLineRepository.currentLineDataSource.getCurrentLine_prev()).thenReturn(
            expectedCurrentLine
        )

        // Act
        val result = currentLineUseCase.getCurrentLine_prev()

        // Assert
        assertEquals(expectedCurrentLine, result)
    }

    @Test
    fun isValidResponseWithInvalidValue1() {
        // Arrange
        `when`(mockCurrentLineRepository.currentLineDataSource.getCurrentLine()).thenReturn(
            ERROR_BODY_IS_NULL
        )

        // Act
        val result = currentLineUseCase.isValidResponse()

        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidResponseWithInvalidValue2() {
        // Arrange
        `when`(mockCurrentLineRepository.currentLineDataSource.getCurrentLine()).thenReturn(
            ERROR_RESPONSE_IS_NOT_SUCCESSFUL
        )

        // Act
        val result = currentLineUseCase.isValidResponse()

        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidResponseWithInvalidValue3() {
        // Arrange
        `when`(mockCurrentLineRepository.currentLineDataSource.getCurrentLine()).thenReturn(
            ERROR_ON_FAILURE
        )

        // Act
        val result = currentLineUseCase.isValidResponse()

        // Assert
        assertFalse(result)
    }

    @Test
    fun isValidResponseWithVailidValue() {
        // Arrange
        `when`(mockCurrentLineRepository.currentLineDataSource.getCurrentLine()).thenReturn(
            currentLine_123
        )

        // Act
        val result = currentLineUseCase.isValidResponse()

        // Assert
        assertTrue(result)
    }

    @Test
    fun isNoShuttleWithInvalidValue() {
        // Arrange
        `when`(mockCurrentLineRepository.currentLineDataSource.getCurrentLine()).thenReturn(
            currentLine_noShuttle
        )

        // Act
        val result = currentLineUseCase.isNoShuttle()

        // Assert
        assertTrue(result)
    }

    @Test
    fun isNoShuttleWithValidValue() {
        // Arrange
        `when`(mockCurrentLineRepository.currentLineDataSource.getCurrentLine()).thenReturn(
            currentLine_123
        )

        // Act
        val result = currentLineUseCase.isNoShuttle()

        // Assert
        assertFalse(result)
    }
}