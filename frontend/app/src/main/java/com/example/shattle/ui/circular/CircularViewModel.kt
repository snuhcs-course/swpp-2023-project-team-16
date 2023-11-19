package com.example.shattle.ui.circular;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.network.NetworkCallback


class CircularViewModel : ViewModel() {


    private val DEFAULT_VALUE = RunningBuses(true, -2)

    private val uiState: MutableLiveData<CircularUIState?> =
        MutableLiveData<CircularUIState?>(CircularUIState(DEFAULT_VALUE))

    private val networkRequestStatus = MutableLiveData<Boolean>()

    private val gpsTrackingStatus = MutableLiveData<Boolean>()

    private val toastMessage = MutableLiveData<String>()

    fun getUIState(): MutableLiveData<CircularUIState?> {
        return uiState
    }

    fun getNetworkRequestStatus(): LiveData<Boolean?> {
        return networkRequestStatus
    }

    fun getGpsTrackingStatus(): LiveData<Boolean?> {
        return gpsTrackingStatus
    }

    fun getToastMessage(): LiveData<String> {
        return toastMessage
    }

    val ERROR_BODY_IS_NULL = RunningBuses(true, -3)
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = RunningBuses(true, -4)
    val ERROR_ON_FAILURE = RunningBuses(true, -5)

    fun notifyRefresh(runningBusesUseCase: RunningBusesUseCase) {
        networkRequestStatus.value = false
        runningBusesUseCase.refreshData(object : NetworkCallback {
            override fun onCompleted() {
                networkRequestStatus.postValue(true)
            }

            override fun onFailure(t: Throwable) {
                networkRequestStatus.postValue(true)
            }
        })
    }

    fun getData(runningBusesUseCase: RunningBusesUseCase) {
        // (useCase 의) repository 에서 RunningBuses 데이터를 uiState 에 저장
        // runningBuses 가 유효하지 않을 경우 이전 값을 적용 (업데이트 X)
        // 유효하지 않은 이유 Toast로 띄워줌
        val error_value = runningBusesUseCase.getErrorId()

        if ((error_value == ERROR_BODY_IS_NULL.numBusesRunning)
            || (error_value == ERROR_RESPONSE_IS_NOT_SUCCESSFUL.numBusesRunning)
            || (error_value == ERROR_ON_FAILURE.numBusesRunning)
        ) {
            uiState.value = CircularUIState(runningBusesUseCase.getRunningBuses_prev())
            showToastMessage("업데이트 중 오류가 발생했습니다. 잠시 후 다시 시도하세요.")
        } else {
            uiState.value = CircularUIState(runningBusesUseCase.getRunningBuses())
            if (error_value == 0) {
                showToastMessage("현재 운행중인 셔틀이 없습니다.")
            } else {
                showToastMessage("업데이트 성공!")
            }

        }
    }

    fun showToastMessage(message: String) {
        toastMessage.value = message
    }

    fun toggleTrackingStatus() {
        gpsTrackingStatus.value = (gpsTrackingStatus.value != true)
    }

}
