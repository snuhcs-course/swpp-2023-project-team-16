package com.example.shattle.ui.dropoff

import android.util.Log
import com.example.shattle.data.models.CurrentLine
import com.example.shattle.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class DropoffData(val isCreated: Boolean) {

    var numberOfPeopleWaitingLine: Int? = null
    var numberOfNeededBus: Int? = null
    var waitingTimeInMin: Int? = null

    var dateTimeString = "2023-01-23T12:34:56Z"

    data class WaitingData(val p: Int, val b: Int, val t: Int)

    private var cnt = 0
    private val dummy = listOf(
        WaitingData(30, 1, 4),
        WaitingData(70, 2, 8),
        WaitingData(70, 2, 8),
        WaitingData(70, 2, 8),
        WaitingData(70, 2, 8),
        WaitingData(70, 2, 8),
        WaitingData(70, 2, 8),
    )

    fun refreshWaitingTimeData2() {
        if (cnt >= dummy.size) {
            numberOfPeopleWaitingLine = null
            numberOfNeededBus = null
            waitingTimeInMin = null
            return
        }
        val data = dummy.get(cnt++)
        numberOfPeopleWaitingLine = data.p
        numberOfNeededBus = data.b
        waitingTimeInMin = data.t
    }

    fun refreshWaitingTimeData() {
        Log.e("MyLogChecker", "@@ start refreshWaitingTimeData()")

        val call: Call<CurrentLine> = ServiceCreator.apiService.getCurrentLine()
        Log.e("MyLogChecker", "@@ val call: Call<ResponseWaitingLine> = ServiceCreator.apiService.getWaitingLine()")

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
                } else {
                    Log.e("MyLogChecker", "### response is not successful\n\tresponse.code(): ${response.code().toString()}")
                    numberOfPeopleWaitingLine = null
                    numberOfNeededBus = null
                    waitingTimeInMin = null
                }
                Log.e("MyLogChecker", "## end onResponse():")
            }

            override fun onFailure(call: Call<CurrentLine>, t: Throwable) {
                Log.e("MyLogChecker", "## start onFailure)")
                numberOfPeopleWaitingLine = null
                numberOfNeededBus = null
                waitingTimeInMin = null
                Log.e("MyLogChecker", "## end onFailure\n\t$t")
            }
        })
    }
}