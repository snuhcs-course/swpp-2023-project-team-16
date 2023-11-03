package com.example.shattle.ui.circular;

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.ui.dropoff.DropoffUIState
import com.google.android.gms.maps.GoogleMap


class CircularViewModel : ViewModel() {


    // GoogleMap LiveData
    private val _googleMap = MutableLiveData<GoogleMap?>()
    val googleMap: LiveData<GoogleMap?> get() = _googleMap

    private val DEFAULT_VALUE = RunningBuses(-2, emptyList())
    private val uiState: MutableLiveData<CircularUIState?> =
        MutableLiveData<CircularUIState?>(CircularUIState(googleMap.value, DEFAULT_VALUE))

    fun setGoogleMap(googleMap: GoogleMap) {
        _googleMap.value = googleMap
    }

    fun getUIState(): MutableLiveData<CircularUIState?> {
        return uiState
    }

    val ERROR_BODY_IS_NULL = RunningBuses(-3, emptyList())
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = RunningBuses(-4, emptyList())
    val ERROR_ON_FAILURE = RunningBuses(-5, emptyList())
    fun getData(googleMap: GoogleMap?, runningBusesUseCase: RunningBusesUseCase) {
        // (useCase 의) repository 에서 RunningBuses 데이터를 uiState 에 저장
        // runningBuses 가 유효하지 않을 경우 이전 값을 적용 (업데이트 X)
        // 유효하지 않은 이유 Toast로 띄워줌
        val error_value = runningBusesUseCase.getErrorId()

        if (error_value == ERROR_BODY_IS_NULL.numBusesRunning
            || error_value == ERROR_RESPONSE_IS_NOT_SUCCESSFUL.numBusesRunning
            || error_value == ERROR_ON_FAILURE.numBusesRunning
        ) {
            uiState.value = CircularUIState(googleMap, runningBusesUseCase.getRunningBuses_prev())
            showToastMessage("업데이트 중 오류가 발생했습니다. 잠시 후 다시 시도하세요2.") //TODO change
        } else if (error_value == 0) {
            uiState.value = CircularUIState(googleMap, runningBusesUseCase.getRunningBuses_prev())
            showToastMessage("현재 운행중인 셔틀이 없습니다.")
        } else {
            uiState.value = CircularUIState(googleMap, runningBusesUseCase.getRunningBuses())
        }
    }

    fun notifyRefresh(runningBusesUseCase: RunningBusesUseCase) {
        runningBusesUseCase.refreshData()
    }

    // Fragment 에서 Toast 를 띄워주기 위한 함수들
    private val toastMessage = MutableLiveData<String>()
    fun getToastMessage(): LiveData<String> {
        return toastMessage
    }

    fun showToastMessage(message: String) {
        toastMessage.value = message
    }


}
