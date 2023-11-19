package com.example.driverapp.request

import com.google.gson.annotations.SerializedName

data class RequestBusIsRunning(
    @SerializedName("license_plate")
    val licensePlate: String,
    @SerializedName("is_running")
    val isRunning: Boolean,
)