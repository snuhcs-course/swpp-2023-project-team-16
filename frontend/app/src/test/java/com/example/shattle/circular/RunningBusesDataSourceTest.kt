package com.example.shattle.circular

import android.content.Context
import android.content.SharedPreferences
import com.example.shattle.data.models.Bus
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.ui.circular.RunningBusesDataSource
import com.google.android.gms.maps.model.LatLng
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
class RunningBusesDataSourceTest {

    // Mock the context and shared preferences
    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPrefs: SharedPreferences

    @Mock
    private lateinit var sharedPrefsEditor: SharedPreferences.Editor

    private lateinit var runningBusesDataSource: RunningBusesDataSource

    val runningBuses_default = RunningBuses(true, -2)
    val runningBuses_1 = RunningBuses(1, listOf(Bus(0, LatLng(0.0, 0.0))))

    @Before
    fun setup() {
        // Initialize the CurrentLineDataSource with mocked context and shared preferences
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs)
        `when`(sharedPrefs.edit()).thenReturn(sharedPrefsEditor)

        runningBusesDataSource = RunningBusesDataSource(context)
    }

    @Test
    fun getRunningBusesWithNoData() {
        // Arrange
        // When there is no data in shared preferences, getCurrentLine should return the default value
        `when`(sharedPrefs.getString(eq("RunningBuses"), any())).thenReturn(null)

        // Act
        val result = runningBusesDataSource.getRunningBuses()

        // Assert
        assert(result == runningBuses_default)
    }

    @Test
    fun storeRunningBusesTest() {
        // Arrange
        // When storing a CurrentLine, it should be saved to shared preferences
        val runningBusesToStore = runningBuses_1

        // Act
        runningBusesDataSource.storeRunningBuses(runningBusesToStore)

        // Assert
        // Verify that the data was saved to shared preferences
        verify(sharedPrefsEditor).putString(eq("RunningBuses"), any())
        verify(sharedPrefsEditor).apply()
    }

    @Test
    fun getRunningBusesTest() {
        // Arrange
        // When there is data in shared preferences, getCurrentLine should return the stored data
        val gson = Gson()
        val storedData = gson.toJson(runningBuses_1)
        `when`(sharedPrefs.getString(eq("RunningBuses"), any())).thenReturn(storedData)

        // Act
        val result = runningBusesDataSource.getRunningBuses()

        // Assert
        assert(result == runningBuses_1)
    }

    @Test
    fun getRunningBuses_prevWithNoData() {
        // Arrange
        // When there is no data in shared preferences, getCurrentLine should return the default value
        `when`(sharedPrefs.getString(eq("RunningBuses_prev"), any())).thenReturn(null)

        // Act
        val result = runningBusesDataSource.getRunningBuses_prev()

        // Assert
        assert(result == runningBuses_default)
    }

    @Test
    fun storeRunningBuses_prevTest() {
        // Arrange
        // When storing a CurrentLine, it should be saved to shared preferences
        val runningBusesToStore = runningBuses_1

        // Act
        runningBusesDataSource.storeRunningBuses_prev(runningBusesToStore)

        // Assert
        // Verify that the data was saved to shared preferences
        verify(sharedPrefsEditor).putString(eq("RunningBuses_prev"), any())
        verify(sharedPrefsEditor).apply()
    }

    @Test
    fun getRunningBuses_prevTest() {
        // Arrange
        // When there is data in shared preferences, getCurrentLine should return the stored data
        val gson = Gson()
        val storedData = gson.toJson(runningBuses_1)
        `when`(sharedPrefs.getString(eq("RunningBuses_prev"), any())).thenReturn(storedData)

        // Act
        val result = runningBusesDataSource.getRunningBuses_prev()

        // Assert
        assert(result == runningBuses_1)
    }
}