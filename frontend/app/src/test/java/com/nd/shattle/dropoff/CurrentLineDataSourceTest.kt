package com.nd.shattle.dropoff

import android.content.Context
import android.content.SharedPreferences
import com.nd.shattle.data.models.CurrentLine
import com.nd.shattle.ui.dropoff.CurrentLineDataSource
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrentLineDataSourceTest {

    // Mock the context and shared preferences
    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPrefs: SharedPreferences

    @Mock
    private lateinit var sharedPrefsEditor: SharedPreferences.Editor

    private lateinit var currentLineDataSource: CurrentLineDataSource

    val currentLine_default = CurrentLine(true, -2)
    val currentLine_1 = CurrentLine(1, 1, 1, "")

    @Before
    fun setup() {
        // Initialize the CurrentLineDataSource with mocked context and shared preferences
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs)
        `when`(sharedPrefs.edit()).thenReturn(sharedPrefsEditor)

        currentLineDataSource = CurrentLineDataSource(context)
    }

    @Test
    fun getCurrentLineWithNoData() {
        // Arrange
        // When there is no data in shared preferences, getCurrentLine should return the default value
        `when`(sharedPrefs.getString(eq("CurrentLine"), any())).thenReturn(null)

        // Act
        val result = currentLineDataSource.getCurrentLine()

        // Assert
        assert(result == currentLine_default)
    }

    @Test
    fun storeCurrentLineTest() {
        // Arrange
        // When storing a CurrentLine, it should be saved to shared preferences
        val currentLineToStore = currentLine_1

        // Act
        currentLineDataSource.storeCurrentLine(currentLineToStore)

        // Assert
        // Verify that the data was saved to shared preferences
        verify(sharedPrefsEditor).putString(eq("CurrentLine"), any())
        verify(sharedPrefsEditor).apply()
    }

    @Test
    fun getCurrentLineTest() {
        // Arrange
        // When there is data in shared preferences, getCurrentLine should return the stored data
        val gson = Gson()
        val storedData = gson.toJson(currentLine_1)
        `when`(sharedPrefs.getString(eq("CurrentLine"), any())).thenReturn(storedData)

        // Act
        val result = currentLineDataSource.getCurrentLine()

        // Assert
        assert(result == currentLine_1)
    }

    @Test
    fun getCurrentLine_prevWithNoData() {
        // Arrange
        // When there is no data in shared preferences, getCurrentLine should return the default value
        `when`(sharedPrefs.getString(eq("CurrentLine_prev"), any())).thenReturn(null)

        // Act
        val result = currentLineDataSource.getCurrentLine_prev()

        // Assert
        assert(result == currentLine_default)
    }

    @Test
    fun storeCurrentLine_prevTest() {
        // Arrange
        // When storing a CurrentLine, it should be saved to shared preferences
        val currentLineToStore = currentLine_1

        // Act
        currentLineDataSource.storeCurrentLine_prev(currentLineToStore)

        // Assert
        // Verify that the data was saved to shared preferences
        verify(sharedPrefsEditor).putString(eq("CurrentLine_prev"), any())
        verify(sharedPrefsEditor).apply()
    }

    @Test
    fun getCurrentLine_prevTest() {
        // Arrange
        // When there is data in shared preferences, getCurrentLine should return the stored data
        val gson = Gson()
        val storedData = gson.toJson(currentLine_1)
        `when`(sharedPrefs.getString(eq("CurrentLine_prev"), any())).thenReturn(storedData)

        // Act
        val result = currentLineDataSource.getCurrentLine_prev()

        // Assert
        assert(result == currentLine_1)
    }
}