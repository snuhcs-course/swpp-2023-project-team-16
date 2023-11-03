package com.example.shattle.data.models

import com.google.gson.annotations.SerializedName

data class RunningBuses(
    @SerializedName("num_buses_running")
    val numBusesRunning: Int,
    @SerializedName("buses")
    val buses: List<Bus>
) {

}