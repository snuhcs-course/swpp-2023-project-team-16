package com.nd.shattle.ui.circular

import com.nd.shattle.data.models.RunningBuses
data class CircularUIState(
    val runningBuses: RunningBuses,
) {
    val numBusesRunning = runningBuses.numBusesRunning
    val buses = runningBuses.buses
    val updatedTime = runningBuses.updatedTime
}