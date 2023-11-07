package com.example.shattle.data.models

import com.google.android.gms.maps.model.LatLng
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
    val isTracked: Boolean,
    @SerializedName("updated_at")
    val updatedTime: String

) {
    constructor(id: Int, location: LatLng) : this(
        id,
        "",
        location.latitude,
        location.longitude,
        true,
        true,
        ""
    )
}