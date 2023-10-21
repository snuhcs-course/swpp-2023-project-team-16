package com.example.driverapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Handler
import android.widget.Button
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var startStopButton: Button
    private lateinit var gpsTracker: GPSTracker
    private var isTracking: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startStopButton = findViewById(R.id.startStopButton)
        startStopButton.setOnClickListener { toggleTracking() }

        // Request location permissions if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        gpsTracker = GPSTracker(this)
    }

    private fun toggleTracking() {
        if (isTracking) {
            // Stop GPS data submission
            isTracking = false
            startStopButton.text = "Start"
        } else {
            // Start GPS data submission
            isTracking = true
            startStopButton.text = "Stop"
            startGPSDataSubmission()
        }
    }

    private fun startGPSDataSubmission() {
        val handler = Handler()
        val delay = 5000L  // 5 seconds

        handler.post(object : Runnable {
            override fun run() {
                if (isTracking) {
                    val location: Location? = gpsTracker.getLocation()
                    if (location != null) {
                        // Construct a GPSData object
                        val gpsData = GPSData(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            created_at = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                            updated_at = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                        )

                        // Send the GPSData object to the endpoint
                        sendDataToEndpoint(gpsData)

                        // Schedule the next submission
                        handler.postDelayed(this, delay)
                    }
                }
            }
        })
    }




    private fun sendDataToEndpoint(data: GPSData) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://endpoint.com/") // Replace with your server's base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.submitGPSData(data)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Handle a successful response from the server
                } else {
                    // Handle errors or unsuccessful responses
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle network failures or request exceptions
            }
        })
    }


}

