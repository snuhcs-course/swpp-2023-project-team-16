package com.example.driverapp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("buses/submit") // Replace with your actual endpoint
    fun submitGPSData(@Body gpsData: GPSData): Call<Void>
}
