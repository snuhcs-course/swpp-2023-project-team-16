package com.nd.shattle.ui.dropoff

import com.nd.shattle.data.models.CurrentLine
import com.nd.shattle.network.NetworkCallback
import com.nd.shattle.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentLineRepository(val currentLineDataSource: CurrentLineDataSource) {

    val ERROR_BODY_IS_NULL = CurrentLine(true, -3)
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = CurrentLine(true, -4)
    val ERROR_ON_FAILURE = CurrentLine(true, -5)

    var currentLine = currentLineDataSource.getCurrentLine()
    var currentLine_prev = currentLineDataSource.getCurrentLine_prev()
    fun refreshCurrentLine(callback: NetworkCallback) {
        // 서버로부터 data call
        // 응답받은 데이터 currentLineDataSource 에 저장 (sharedPref)

        // currentLine_prev 에는 항상 유효한 값만 저장됨 (화면 업데이트용)
        if (currentLine.waitingTime >= 0) {
            currentLine_prev = currentLine
        }

        val call: Call<CurrentLine> = ServiceCreator.apiService.getCurrentLine()

        call.enqueue(object : Callback<CurrentLine> {
            override fun onResponse(
                call: Call<CurrentLine>,
                response: Response<CurrentLine>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        currentLine = body
                    } else {
                        currentLine = ERROR_BODY_IS_NULL
                    }
                } else {
                    currentLine = ERROR_RESPONSE_IS_NOT_SUCCESSFUL
                }
                currentLineDataSource.storeCurrentLine(currentLine)
                currentLineDataSource.storeCurrentLine_prev(currentLine_prev)

                callback.onCompleted()
            }

            override fun onFailure(call: Call<CurrentLine>, t: Throwable) {
                currentLine = ERROR_ON_FAILURE
                currentLineDataSource.storeCurrentLine(currentLine)
                currentLineDataSource.storeCurrentLine_prev(currentLine_prev)

                callback.onFailure(t)
            }
        })
    }



}