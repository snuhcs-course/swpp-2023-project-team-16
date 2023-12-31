package com.nd.shattle.data.models

import com.google.gson.annotations.SerializedName

data class RunningBuses(
    @SerializedName("num_buses_running")
    val numBusesRunning: Int,
    @SerializedName("buses")
    val buses: List<Bus>,
    @SerializedName("latest_location_updated_at")
    val updatedTime: String
) {
    constructor(isErrorClass: Boolean, errorCode: Int) : this(errorCode, emptyList(), "2000-01-01 00:00:00.000000000")
}