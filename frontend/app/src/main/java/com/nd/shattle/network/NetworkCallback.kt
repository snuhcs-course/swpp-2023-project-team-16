package com.nd.shattle.network

interface NetworkCallback {
    fun onCompleted()
    fun onFailure(t: Throwable)
}