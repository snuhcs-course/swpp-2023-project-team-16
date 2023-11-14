package com.example.shattle.ui.circular

import com.example.shattle.data.models.RunningBuses
data class CircularUIState(
    val runningBuses: RunningBuses,
) {
    val numBusesRunning = runningBuses.numBusesRunning
    val buses = runningBuses.buses
    val updatedTime = runningBuses.updatedTime
}