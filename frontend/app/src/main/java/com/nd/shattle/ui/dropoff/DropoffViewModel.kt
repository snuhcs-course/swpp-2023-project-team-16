package com.nd.shattle.ui.dropoff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nd.shattle.data.models.CurrentLine
import com.nd.shattle.network.NetworkCallback


class DropoffViewModel : ViewModel() {

    private val DEFAULT_VALUE = CurrentLine(true, -2)

    private val uiState: MutableLiveData<DropoffUIState?> =
        MutableLiveData<DropoffUIState?>(DropoffUIState(DEFAULT_VALUE))

    private val networkRequestStatus = MutableLiveData<Boolean>()

    private val toastMessage = MutableLiveData<String>()

    fun getUIState(): MutableLiveData<DropoffUIState?> {
        return uiState
    }

    fun getNetworkRequestStatus(): LiveData<Boolean?> {
        return networkRequestStatus
    }

    fun getToastMessage(): LiveData<String> {
        return toastMessage
    }

    fun notifyRefresh(currentLineUseCase: CurrentLineUseCase) {
        networkRequestStatus.value = false
        currentLineUseCase.refreshData(object : NetworkCallback {
            override fun onCompleted() {
                networkRequestStatus.postValue(true)
            }
            override fun onFailure(t: Throwable) {
                networkRequestStatus.postValue(true)
            }
        })
    }

    fun getData(currentLineUseCase: CurrentLineUseCase) {
        // (useCase 의) repository 에서 CurrentLine 데이터를 uiState 에 저장
        // currentLine 이 유효하지 않을 경우 이전 값을 적용 (업데이트 X)
        // 유효하지 않은 이유 Toast로 띄워줌
        if (!currentLineUseCase.isValidResponse()) {
            uiState.value = DropoffUIState(currentLineUseCase.getCurrentLine_prev())
            showToastMessage("업데이트 중 오류가 발생했습니다. 잠시 후 다시 시도하세요.")
        } else if (currentLineUseCase.isNoShuttle()) {
            uiState.value = DropoffUIState(currentLineUseCase.getCurrentLine_prev())
            showToastMessage("현재 셔틀 운행 시간이 아닙니다.")
        } else {
            uiState.value = DropoffUIState(currentLineUseCase.getCurrentLine())
            showToastMessage("업데이트 성공!")
        }
    }

    fun showToastMessage(message: String) {
        toastMessage.value = message
    }

}