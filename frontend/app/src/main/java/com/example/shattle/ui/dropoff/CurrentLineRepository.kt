package com.example.shattle.ui.dropoff

import android.util.Log
import com.example.shattle.data.models.CurrentLine
import com.example.shattle.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentLineRepository(val currentLineDataSource: CurrentLineDataSource) {

    val ERROR_BODY_IS_NULL = CurrentLine(-3, -3, -3, "")
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = CurrentLine(-4, -4, -4, "")
    val ERROR_ON_FAILURE = CurrentLine(-5, -5, -5, "")

    var currentLine = currentLineDataSource.getCurrentLine()
    var currentLine_prev = currentLineDataSource.getCurrentLine_prev()
    fun refreshCurrentLine() {
        // 서버로부터 data call
        // 응답받은 데이터 currentLineDataSource 에 저장 (sharedPref)


        if (currentLine.waitingTime >= 0) {
            currentLine_prev = currentLine
        }

        //Log.e("MyLogChecker", "@@ start refreshCurrentLine()")

        val call: Call<CurrentLine> = ServiceCreator.apiService.getCurrentLine()
        //Log.e(            "MyLogChecker",            "@@ val call: Call<CurrnetLine> = ServiceCreator.apiService.getCurrentLine()"        )

        //Log.e("MyLogChecker", "# start call.enqueue():")

        call.enqueue(object : Callback<CurrentLine> {
            override fun onResponse(
                call: Call<CurrentLine>,
                response: Response<CurrentLine>
            ) {
                //Log.e("MyLogChecker", "## start onResponse():")

                if (response.isSuccessful) {
                    //Log.e("MyLogChecker", "### response is successful")
                    val body = response.body()
                    if (body != null) {
                        currentLine = body
                    } else {
                        currentLine = ERROR_BODY_IS_NULL
                    }
                } else {
                    //Log.e( "MyLogChecker", "### response is not successful\n\tresponse.code(): ${  response.code()  }" )
                    currentLine = ERROR_RESPONSE_IS_NOT_SUCCESSFUL
                }
                currentLineDataSource.storeCurrentLine(currentLine)
                currentLineDataSource.storeCurrentLine_prev(currentLine_prev)
                //Log.e("MyLogChecker", "## end onResponse():")
            }

            override fun onFailure(call: Call<CurrentLine>, t: Throwable) {
                //Log.e("MyLogChecker", "## start onFailure)")
                currentLine = ERROR_ON_FAILURE
                currentLineDataSource.storeCurrentLine(currentLine)
                currentLineDataSource.storeCurrentLine_prev(currentLine_prev)
                //Log.e("MyLogChecker", "## end onFailure\n\t$t")
            }
        })

    }

}