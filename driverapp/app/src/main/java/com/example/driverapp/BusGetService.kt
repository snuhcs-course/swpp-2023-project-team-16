package com.example.driverapp
import com.example.driverapp.response.ResponseBusLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BusGetService {
    @GET("driverapp/bus")
    fun getBusLocation(
        @Query("license_plate") licensePlate: String,
    ): Call<ResponseBusLocation>

}