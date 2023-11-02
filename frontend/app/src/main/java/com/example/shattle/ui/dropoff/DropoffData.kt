package com.example.shattle.ui.dropoff

import android.util.Log
import com.example.shattle.data.models.CurrentLine
import com.example.shattle.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DropoffData(val isCreated: Boolean) {

    var numberOfPeopleWaitingLine: Int? = null
    var numberOfNeededBus: Int? = null
    var waitingTimeInMin: Int? = null
    var dateTimeString: String? = "2023-01-23T12:34:56Z"
    var isSuccessCall = false

    fun refreshCurrentLineData() {
        // 서버의 currentLine Data call 후 전역변수에 저장
        // 만약 call 이 실패하면 전역변수에 null 저장
        Log.e("MyLogChecker", "@@ start refreshWaitingTimeData()")

        val call: Call<CurrentLine> = ServiceCreator.apiService.getCurrentLine()
        Log.e(
            "MyLogChecker",
            "@@ val call: Call<ResponseWaitingLine> = ServiceCreator.apiService.getWaitingLine()"
        )

        Log.e("MyLogChecker", "# start call.enqueue():")

        call.enqueue(object : Callback<CurrentLine> {
            override fun onResponse(
                call: Call<CurrentLine>,
                response: Response<CurrentLine>
            ) {
                Log.e("MyLogChecker", "## start onResponse():")

                if (response.isSuccessful) {
                    Log.e("MyLogChecker", "### response is successful")
                    val body = response.body()
                    numberOfPeopleWaitingLine = body?.numberOfPeopleWaiting
                    numberOfNeededBus = body?.numberOfNeededBuses
                    waitingTimeInMin = body?.waitingTimeInMin
                    //dateTimeString = body?.dateTimeString
                    isSuccessCall = true
                } else {
                    Log.e(
                        "MyLogChecker",
                        "### response is not successful\n\tresponse.code(): ${
                            response.code().toString()
                        }"
                    )
                    isSuccessCall = false
                }
                Log.e("MyLogChecker", "## end onResponse():")
            }

            override fun onFailure(call: Call<CurrentLine>, t: Throwable) {
                Log.e("MyLogChecker", "## start onFailure)")
                isSuccessCall = false
                Log.e("MyLogChecker", "## end onFailure\n\t$t")
            }
        })
    }
}