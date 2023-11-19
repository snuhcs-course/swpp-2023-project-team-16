package com.example.driverapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator { // 서버 호출 시마다 Retrofit 객체 만들지 않도록 싱글톤(Object)으로
    private const val BASE_URL = "http://54.180.118.50:8000"

    val retrofit = Retrofit.Builder() // retrofit 객체 생성
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val busUpdateService = retrofit.create(BusUpdateService::class.java)
    val busGetService = retrofit.create(BusGetService::class.java)
}