package com.example.shattle.ui.dropoff

import com.example.shattle.data.models.CurrentLine

class DropoffUIState(
    val currentLine: CurrentLine
) {
    val numPeople = currentLine.numWaitingPeople
    val numBus = currentLine.numNeededBus
    val numTime = currentLine.waitingTime

}