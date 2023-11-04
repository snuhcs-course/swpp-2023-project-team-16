package com.example.shattle.ui.dropoff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shattle.data.models.CurrentLine


class DropoffViewModel : ViewModel() {

    private val DEFAULT_VALUE = CurrentLine(-2, -2, -2)

    private val uiState: MutableLiveData<DropoffUIState?> =
        MutableLiveData<DropoffUIState?>(DropoffUIState(DEFAULT_VALUE))

    private val toastMessage = MutableLiveData<String>()

    fun getUIState(): MutableLiveData<DropoffUIState?> {
        return uiState
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
        }
    }

    fun notifyRefresh(currentLineUseCase: CurrentLineUseCase) {
        currentLineUseCase.refreshData()
    }

    // Fragment 에서 Toast 를 띄워주기 위한 함수들
    fun getToastMessage(): LiveData<String> {
        return toastMessage
    }
    fun showToastMessage(message: String) {
        toastMessage.value = message
    }

}