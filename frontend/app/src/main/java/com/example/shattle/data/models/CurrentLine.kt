package com.example.shattle.data.models

import com.google.gson.annotations.SerializedName

data class CurrentLine (
    @SerializedName("num_waiting_people")
    val numberOfPeopleWaiting: Int,
    @SerializedName("num_needed_bus")
    val numberOfNeededBuses: Int,
    @SerializedName("waiting_time")
    val waitingTimeInMin: Int
)
