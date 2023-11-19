package com.example.driverapp.response

import com.google.gson.annotations.SerializedName

data class ResponseBusLocation (
    val id: Int,
    @SerializedName("license_plate")
    val licensePlate: String,
    @SerializedName("location_id")
    val locationId: Int,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("is_running")
    val isRunning: Boolean,
    @SerializedName("is_tracked")
    val isTracked: Boolean,
    @SerializedName("location_updated_at")
    val locationUpdatedAt: String,
)

