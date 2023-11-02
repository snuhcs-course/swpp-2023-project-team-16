package com.example.driverapp
import retrofit2.Call
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @PUT("driverapp/update") // Replace with your actual endpoint
    fun submitGPSData(
        @Query("license_plate") licensePlate: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<Void>
}
