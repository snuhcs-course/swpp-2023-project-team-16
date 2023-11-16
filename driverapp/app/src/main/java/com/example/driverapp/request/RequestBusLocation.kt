package com.example.driverapp.request

import com.google.gson.annotations.SerializedName

data class RequestBusLocation(
    @SerializedName("license_plate")
    val licensePlate: String,
    val latitude: Double,
    val longitude: Double,
)