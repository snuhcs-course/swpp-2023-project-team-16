package com.nd.shattle.ui.dropoff

import com.nd.shattle.data.models.CurrentLine
import com.nd.shattle.network.NetworkCallback

class CurrentLineUseCase(val currentLineRepository: CurrentLineRepository) {

    val ERROR_BODY_IS_NULL = CurrentLine(true, -3)
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = CurrentLine(true, -4)
    val ERROR_ON_FAILURE = CurrentLine(true, -5)


    fun refreshData(callback: NetworkCallback) {
        currentLineRepository.refreshCurrentLine(callback)
    }

    fun getCurrentLine(): CurrentLine {
        return currentLineRepository.currentLineDataSource.getCurrentLine()
    }

    fun getCurrentLine_prev(): CurrentLine {
        return currentLineRepository.currentLineDataSource.getCurrentLine_prev()
    }

    fun isValidResponse(): Boolean {
        // 서버 호출이 실패했는지 확인
        val currentLine = getCurrentLine()
        if (currentLine == ERROR_BODY_IS_NULL
            || currentLine == ERROR_RESPONSE_IS_NOT_SUCCESSFUL
            || currentLine == ERROR_ON_FAILURE
        ) {
            return false
        }
        return true
    }

    fun isNoShuttle(): Boolean {
        // 서버 호출은 성공했으나 셔틀 운행시간이 아닌지 확인 (NoShuttleException)
        val currentLine = getCurrentLine()
        if (currentLine.numNeededBus == -1 || currentLine.waitingTime == -1) {
            return true
        }
        return false
    }

}