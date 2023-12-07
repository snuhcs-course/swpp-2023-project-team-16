package com.nd.shattle.ui.dropoff

import com.nd.shattle.data.models.CurrentLine

data class DropoffUIState(
    val currentLine: CurrentLine
) {
    val numPeople = currentLine.numWaitingPeople
    val numBus = currentLine.numNeededBus
    val numTime = currentLine.waitingTime
    val updatedTime = currentLine.updatedTime
}