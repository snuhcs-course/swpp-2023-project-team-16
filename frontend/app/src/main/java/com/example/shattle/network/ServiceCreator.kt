package com.example.shattle.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private const val BASE_URL = "http://54.180.118.50:8000/"

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS) // 읽기 시간 초과 시간
        .connectTimeout(5, TimeUnit.SECONDS) // 연결 시간 초과 시간
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}