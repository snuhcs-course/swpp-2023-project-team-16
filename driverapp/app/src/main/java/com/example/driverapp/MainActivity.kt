package com.example.driverapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.PUT
import retrofit2.http.Query

private const val BASE_URL = "http://54.180.118.50:3000"
class MainActivity : AppCompatActivity() {

    private lateinit var startStopButton: Button
    private lateinit var gpsTracker: GPSTracker
    private lateinit var licensePlateText: EditText
    private lateinit var responseText: TextView
    private var isTracking: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startStopButton = findViewById(R.id.startStopButton)
        startStopButton.setOnClickListener { toggleTracking() }
        licensePlateText = findViewById(R.id.licensePlateText)
        responseText = findViewById(R.id.responseText)

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
                    val licensePlate: String = licensePlateText.text.toString()
                    if (location != null) {
                        // Construct a GPSData object
                        val gpsData = GPSData(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            licensePlate = licensePlate
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
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.submitGPSData(licensePlate = data.licensePlate,
                                            latitude = data.latitude,
                                            longitude = data.longitude)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Handle a successful response from the server
                    responseText.text = response.code().toString()
                } else {
                    // Handle errors or unsuccessful responses
                    responseText.text = response.code().toString()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle network failures or request exceptions
                responseText.text = t.message
            }
        })
    }
}

