package com.example.shattle.data.models

import com.google.gson.annotations.SerializedName

data class ResponseWaitingLine (
    @SerializedName("num_people_waiting")
    val numberOfPeopleWaiting: Int,
    @SerializedName("num_needed_bus")
    val numberOfNeededBuses: Int,
    @SerializedName("waiting_min")
    val waitingTimeInMin: Int
)