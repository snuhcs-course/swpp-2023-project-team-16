package com.nd.shattle.data.models

import com.google.gson.annotations.SerializedName

data class CurrentLine(
    @SerializedName("num_waiting_people")
    val numWaitingPeople: Int,
    @SerializedName("num_needed_bus")
    val numNeededBus: Int,
    @SerializedName("waiting_time")
    val waitingTime: Int,
    @SerializedName("updated_at")
    val updatedTime: String
) {
    constructor(isErrorClass: Boolean, errorCode: Int) : this(errorCode, errorCode, errorCode, "2000-01-01 00:00:00.000000000")

}
