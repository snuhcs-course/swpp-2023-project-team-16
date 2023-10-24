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

    data class waitingData(val p: Int, val b: Int, val t: Int)

    private var cnt = 0
    private val dummy = listOf(
        waitingData(10, 1, 1),
        waitingData(20, 1, 2),
        waitingData(30, 2, 3),
        waitingData(40, 2, 4),
        waitingData(50, 3, 5),
        waitingData(60, 3, 6),
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
                    numberOfPeopleWaitingLine = null
                    numberOfNeededBus = null
                    waitingTimeInMin = null
                }
            }

            override fun onFailure(call: Call<ResponseWaitingLine>, t: Throwable) {
                Log.e("MyLogChecker", "error: $t")
                numberOfPeopleWaitingLine = null
                numberOfNeededBus = null
                waitingTimeInMin = null
            }
        })
    }
}