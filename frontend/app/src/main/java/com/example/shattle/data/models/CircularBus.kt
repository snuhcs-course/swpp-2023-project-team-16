package com.example.shattle.data.models

import com.google.android.gms.maps.model.LatLng
import java.util.UUID

data class CircularBus(
    val id: UUID,
    val location: LatLng
)
