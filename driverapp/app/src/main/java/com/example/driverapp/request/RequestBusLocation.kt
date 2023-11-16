package com.example.driverapp.request

import com.google.gson.annotations.SerializedName

data class RequestBusLocation(
    @SerializedName("licensePlate")
    val licensePlate: String,
    val latitude: Double,
    val longitude: Double,
)