package com.example.shattle.data.models

import com.google.gson.annotations.SerializedName

data class CurrentLine (
    @SerializedName("num_waiting_people")
    val numWaitingPeople: Int,
    @SerializedName("num_needed_bus")
    val numNeededBus: Int,
    @SerializedName("waiting_time")
    val waitingTime: Int) {

    override fun equals(other: Any?): Boolean {
        //if (other?.javaClass != javaClass) return false
        other as CurrentLine
        return ((numWaitingPeople == other.numWaitingPeople)
                && (numNeededBus == other.numNeededBus)
                && (waitingTime == other.waitingTime))
    }
}
