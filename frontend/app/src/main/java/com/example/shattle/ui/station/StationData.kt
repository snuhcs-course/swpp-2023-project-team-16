package com.example.shattle.ui.station

import android.util.Log
import com.example.shattle.data.models.ResponseWaitingLine
import com.example.shattle.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class StationData(val isCreated: Boolean) {

    var numberOfPeopleWaitingLine: Int? = null
    var numberOfNeededBus: Int? = null
    var waitingTimeInMin: Int? = null

    var dateTimeString = "2023-01-23T12:34:56Z"

    data class WaitingData(val p: Int, val b: Int, val t: Int)

    private var cnt = 0
    private val dummy = listOf(
        WaitingData(10, 1, 1),
        WaitingData(20, 1, 1),
        WaitingData(30, 1, 2),
        WaitingData(40, 2, 6),
        WaitingData(50, 2, 6),
        WaitingData(60, 2, 6),
        WaitingData(70, 2, 6),
        WaitingData(80, 3, 11),
        WaitingData(90, 3, 11),
        WaitingData(100, 3, 11),
        WaitingData(110, 3, 11),
        WaitingData(0, 0, 0),
        WaitingData(0, 0, 0),
        WaitingData(0, 0, 0),
        WaitingData(0, 0, 0),
        WaitingData(0, 0, 0),
        WaitingData(0, 0, 0),
        WaitingData(0, 0, 0),
        WaitingData(0, 0, 0),
        WaitingData(0, 0, 0),
    )

    fun refreshWaitingTimeData() {
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

    fun refreshWaitingTimeData2() {
        val call: Call<ResponseWaitingLine> = ServiceCreator.apiService.getWaitingLine()

        call.enqueue(object : Callback<ResponseWaitingLine> {
            override fun onResponse(
                call: Call<ResponseWaitingLine>,
                response: Response<ResponseWaitingLine>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    numberOfPeopleWaitingLine = body?.numberOfPeopleWaiting
                    numberOfNeededBus = body?.numberOfNeededBuses
                    waitingTimeInMin = body?.waitingTimeInMin
                } else {
                    Log.e("MyLogChecker", "error: response is not successful")
                    numberOfPeopleWaitingLine = null
                    numberOfNeededBus = null
                    waitingTimeInMin = null
                }
            }

            override fun onFailure(call: Call<ResponseWaitingLine>, t: Throwable) {
                Log.e("MyLogChecker", "error: onFailure\n\t$t")
                numberOfPeopleWaitingLine = null
                numberOfNeededBus = null
                waitingTimeInMin = null
            }
        })
    }
}