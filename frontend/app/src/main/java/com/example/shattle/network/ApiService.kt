package com.example.shattle.network

import com.example.shattle.data.models.RunningBuses
import com.example.shattle.data.models.Bus
import com.example.shattle.data.models.CurrentLine

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("dropoff/waiting-time")
    fun getCurrentLine(): Call<CurrentLine>
    @GET("circular/location")
    fun getRunningBuses(): Call<RunningBuses>
}