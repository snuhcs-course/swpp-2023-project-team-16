package com.example.shattle.data.models

import com.google.android.gms.maps.model.LatLng

data class BusStop(
    val position: LatLng,
    val title: String,
    val snippet: String
)