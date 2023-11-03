package com.example.shattle.ui.circular

import com.example.shattle.data.models.RunningBuses

class RunningBusesUseCase(val runningBusesRepository: RunningBusesRepository) {

    fun refreshData(){
        runningBusesRepository.refreshRunningBuses()
    }

    fun getRunningBuses(): RunningBuses{
        return runningBusesRepository.runningBusesDataSource.getRunningBuses()
    }

    fun getRunningBuses_prev(): RunningBuses{
        return runningBusesRepository.runningBusesDataSource.getRunningBuses_prev()
    }

    fun getErrorId(): Int {
        // 서버 호출이 실패했는지 확인
        return getRunningBuses().numBusesRunning
    }

}