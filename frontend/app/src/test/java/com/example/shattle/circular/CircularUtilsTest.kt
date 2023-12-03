package com.example.shattle.circular

import com.example.shattle.ui.circular.CircularUtils
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CircularUtilsTest {

    @Test
    fun bearingBetweenLocationsTest1() {
        // Assign
        val start = LatLng(0.0, 0.0)
        val end = LatLng(1.0, 1.0)
        val expectedBearing = 45.0f

        //Act
        val result = CircularUtils.bearingBetweenLocations(start, end)

        // Assert
        assertEquals("", expectedBearing, result, 0.1f)
    }

    @Test
    fun bearingBetweenLocationsTest2() {
        // Assign
        val start = LatLng(0.0, 0.0)
        val end = LatLng(1.0, 0.0)
        val expectedBearing = 0.0f

        //Act
        val result = CircularUtils.bearingBetweenLocations(start, end)

        // Assert
        assertEquals("", expectedBearing, result, 0.1f)
    }

    @Test
    fun bearingBetweenLocationsTest3() {
        // Assign
        val start = LatLng(0.0, 0.0)
        val end = LatLng(0.0, 1.0)
        val expectedBearing = 90.0f

        //Act
        val result = CircularUtils.bearingBetweenLocations(start, end)

        // Assert
        assertEquals("", expectedBearing, result, 0.1f)
    }
}