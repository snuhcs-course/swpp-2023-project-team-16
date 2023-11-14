package com.example.shattle.network

interface NetworkCallback {
    fun onCompleted()
    fun onFailure(t: Throwable)
}