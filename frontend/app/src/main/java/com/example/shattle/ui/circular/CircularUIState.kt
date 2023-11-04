package com.example.shattle.ui.circular

import com.example.shattle.data.models.RunningBuses
import com.google.android.gms.maps.GoogleMap

class CircularUIState(
    val googleMap: GoogleMap?,
    val runningBuses: RunningBuses,
) {
    val numBusesRunning = runningBuses.numBusesRunning
    val buses = runningBuses.buses
}