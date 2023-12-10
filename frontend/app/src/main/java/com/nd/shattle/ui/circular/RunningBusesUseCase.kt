package com.nd.shattle.ui.circular

import com.nd.shattle.data.models.RunningBuses
import com.nd.shattle.network.NetworkCallback

class RunningBusesUseCase(val runningBusesRepository: RunningBusesRepository) {

    fun refreshData(callback: NetworkCallback) {
        runningBusesRepository.refreshRunningBuses(callback)
    }

    fun getRunningBuses(): RunningBuses {
        return runningBusesRepository.runningBusesDataSource.getRunningBuses()
    }

    fun getRunningBuses_prev(): RunningBuses {
        return runningBusesRepository.runningBusesDataSource.getRunningBuses_prev()
    }

    fun getErrorId(): Int {
        // 서버 호출이 실패했는지 확인
        return getRunningBuses().numBusesRunning
    }

}