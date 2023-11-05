package com.example.shattle.ui.circular

import com.example.shattle.data.models.RunningBuses

class CircularUIState(
    val runningBuses: RunningBuses,
) {
    val numBusesRunning = runningBuses.numBusesRunning
    val buses = runningBuses.buses
}