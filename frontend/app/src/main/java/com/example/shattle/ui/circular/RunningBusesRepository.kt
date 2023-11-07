package com.example.shattle.ui.circular

import android.util.Log
import com.example.shattle.data.models.Bus
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.network.ServiceCreator
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RunningBusesRepository(val runningBusesDataSource: RunningBusesDataSource) {

    val ERROR_BODY_IS_NULL = RunningBuses(-3, emptyList())
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = RunningBuses(-4, emptyList())
    val ERROR_ON_FAILURE = RunningBuses(-5, emptyList())

    var runningBuses = runningBusesDataSource.getRunningBuses()
    var runningBuses_prev = runningBusesDataSource.getRunningBuses_prev()

    fun refreshRunningBuses() {
        // 서버로부터 data call
        // 응답받은 데이터 runningBusesDataSource 에 저장 (sharedPref)

        // currentLine_prev 에는 항상 유효한 값만 저장됨 (화면 업데이트용)
        if (runningBuses.numBusesRunning >= 0)
            runningBuses_prev = runningBuses

        Log.e("MyLogChecker", "@@ start refreshRunningBuses()")

        val call: Call<RunningBuses> = ServiceCreator.apiService.getRunningBuses()
        Log.e(
            "MyLogChecker",
            "@@ val call: Call<ResponseRunningBuses> = ServiceCreator.apiService.getRunningBuses()"
        )

        Log.e("MyLogChecker", "# start call.enqueue():")

        call.enqueue(object : Callback<RunningBuses> {
            override fun onResponse(
                call: Call<RunningBuses>,
                response: Response<RunningBuses>
            ) {
                Log.e("MyLogChecker", "## start onResponse():")

                if (response.isSuccessful) {
                    Log.e("MyLogChecker", "### response is successful")
                    val body = response.body()
                    if (body != null) {
                        runningBuses = body
                    } else {
                        runningBuses = ERROR_BODY_IS_NULL
                    }
                } else {
                    Log.e(
                        "MyLogChecker",
                        "### response is not successful\n\tresponse.code(): ${
                            response.code()
                        }"
                    )
                    runningBuses = ERROR_RESPONSE_IS_NOT_SUCCESSFUL
                }
                runningBusesDataSource.storeRunningBuses(runningBuses)
                runningBusesDataSource.storeRunningBuses_prev(runningBuses_prev)
                Log.e("MyLogChecker", "## end onResponse():")
            }

            override fun onFailure(call: Call<RunningBuses>, t: Throwable) {
                Log.e("MyLogChecker", "## start onFailure)")
                runningBuses = ERROR_ON_FAILURE
                runningBusesDataSource.storeRunningBuses(runningBuses)
                runningBusesDataSource.storeRunningBuses_prev(runningBuses_prev)
                Log.e("MyLogChecker", "## end onFailure\n\t$t")
            }
        })

    }

    var cnt = 0
    fun refreshRunningBuses2() {

        if (runningBuses.numBusesRunning >= 0) {
            runningBuses_prev = runningBuses
        }
        runningBuses = dummy[cnt]

        runningBusesDataSource.storeRunningBuses(runningBuses)
        runningBusesDataSource.storeRunningBuses_prev(runningBuses_prev)
        cnt++
        if (cnt >= dummy.size)
            cnt--
    }

    val dummy = listOf(
        RunningBuses(
            2, listOf(
                Bus(0, LatLng(37.46577, 126.9484)),
                Bus(1, LatLng(37.45158, 126.9526)),
            )
        ),
        RunningBuses(
            2, listOf(
                Bus(0, LatLng(37.46306, 126.9490)),
                Bus(1, LatLng(37.45408, 126.9539)),
            )
        ),
        RunningBuses(
            2, listOf(
                Bus(0, LatLng(37.46046, 126.9490)),
                Bus(1, LatLng(37.45612, 126.9554)),
            )
        ),
        RunningBuses(true, -3),
        RunningBuses(0, emptyList()),
        RunningBuses(true, -3),
        RunningBuses(0, emptyList()),
        RunningBuses(
            2, listOf(
                Bus(0, LatLng(37.46577, 126.9484)),
                Bus(1, LatLng(37.45158, 126.9526)),
            )
        ),
    )
}