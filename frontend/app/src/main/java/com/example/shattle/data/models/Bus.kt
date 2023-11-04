package com.example.shattle.data.models

import com.google.gson.annotations.SerializedName

data class Bus(
    @SerializedName("id")
    val id: Int,
    @SerializedName("license_plate")
    val licensePlate: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("is_running")
    val isRunning: Boolean,
    @SerializedName("is_tracked")
    val isTracked: Boolean
)