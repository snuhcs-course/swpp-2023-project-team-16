package com.example.shattle.ui.station

data class StationData(
    private var numPeople: Int,
    private var timeWait: Int,
    private var numBusPass: Int) {

    fun updateData() {
        numPeople.plus(10)
        timeWait.plus(100)
        numBusPass.plus(1)
    }

}