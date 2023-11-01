package com.example.shattle.network

import com.example.shattle.data.models.CircularBus
import com.example.shattle.data.models.CurrentLine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("dropoff/waiting-time")
    fun getCurrentLine(): Call<CurrentLine>
    @GET("circular/location")
    fun getCircularLocation(): Call<List<CircularBus>>
}