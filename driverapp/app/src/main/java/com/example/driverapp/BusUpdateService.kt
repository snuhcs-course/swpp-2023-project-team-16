package com.example.driverapp
import com.example.driverapp.request.RequestBusIsRunning
import com.example.driverapp.request.RequestBusLocation
import com.example.driverapp.response.ResponseBusIsRunning
import com.example.driverapp.response.ResponseBusLocation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface BusUpdateService {
    @PUT("driverapp/bus/location")
    fun submitGPSData(
        @Body requestBody: RequestBusLocation,
    ): Call<ResponseBusLocation>

    @PUT("driverapp/bus/is_running")
    fun updateBusIsRunning(
        @Body requestBody: RequestBusIsRunning
    ): Call<ResponseBusIsRunning>
}
