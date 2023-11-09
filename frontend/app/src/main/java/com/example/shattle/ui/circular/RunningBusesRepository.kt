package com.example.shattle.ui.circular

import android.util.Log
import com.example.shattle.data.models.RunningBuses
import com.example.shattle.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RunningBusesRepository(val runningBusesDataSource: RunningBusesDataSource) {

    val ERROR_BODY_IS_NULL = RunningBuses(true, -3)
    val ERROR_RESPONSE_IS_NOT_SUCCESSFUL = RunningBuses(true, -4)
    val ERROR_ON_FAILURE = RunningBuses(true, -5)

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
}