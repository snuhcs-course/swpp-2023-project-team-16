package com.example.shattle.ui.dropoff

import com.example.shattle.data.models.CurrentLine

class CurrentLineUseCase(val currentLineRepository: CurrentLineRepository) {

    val ERROR_BODY_IS_NULL = CurrentLine(-3, -3, -3)
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = CurrentLine(-4, -4, -4)
    val ERROR_ON_FAILURE = CurrentLine(-5, -5, -5)
    val DEFAULT_VALUE = CurrentLine(-2, -2, -2)

    fun refreshData() {
        currentLineRepository.refreshCurrentLine()
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
        if (currentLine.equals(ERROR_BODY_IS_NULL)
            || currentLine.equals(ERROR_RESPONSE_IS_NOT_SUCCESSFUL)
            || currentLine.equals(ERROR_ON_FAILURE)
        ) {
            return false
        }
        return true
    }

    fun isNoShuttle(): Boolean {
        // 서버 호출은 성공했으나 셔틀 운행시간이 아닌지 확인
        // (NoShuttleException)
        val currentLine = getCurrentLine()
        if (currentLine.numNeededBus == -1 || currentLine.waitingTime == -1){
            return true
        }
        return false
    }

}