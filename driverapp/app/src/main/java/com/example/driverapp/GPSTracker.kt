package com.example.driverapp

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager

class GPSTracker(private val context: Context) : LocationListener {

    private var locationManager: LocationManager? = null
    private var location: Location? = null

    init {
        startTracking()
    }

    private fun startTracking() {
        // Check if the ACCESS_FINE_LOCATION permission is granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        } else {
            // Handle the situation when permission is not granted (e.g., show a message to the user)
        }
    }

    fun getLocation(): Location? {
        return location
    }

    override fun onLocationChanged(newLocation: Location) {
        location = newLocation
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }
}
