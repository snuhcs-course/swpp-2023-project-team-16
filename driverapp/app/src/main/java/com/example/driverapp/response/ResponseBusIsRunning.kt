package com.example.driverapp.response

import com.google.gson.annotations.SerializedName

data class ResponseBusIsRunning (
    val id: Int,
    @SerializedName("license_plate")
    val licensePlate: String,
    @SerializedName("is_running")
    val isRunning: Boolean,
    @SerializedName("is_tracked")
    val isTracked: Boolean,
)
