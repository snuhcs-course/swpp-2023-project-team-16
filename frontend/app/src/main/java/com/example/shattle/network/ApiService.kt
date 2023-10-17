package com.example.shattle.network

import com.example.shattle.data.models.ResponseCongestion
import com.example.shattle.data.models.ResponseWaitingLine

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("dropoff/waiting-line")
    fun getWaitingLine(): Call<ResponseWaitingLine>
    @GET("dropoff/congestion")
    fun getCongestionData(@Query("day") day: Int): Call<List<ResponseCongestion>>
}