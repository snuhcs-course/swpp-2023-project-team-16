package com.example.shattle.ui.dropoff

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shattle.data.models.CurrentLine
import com.example.shattle.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DropoffViewModel: ViewModel() {

    // 처음 클래스 만들어질 때 한 번 call
    init {
        refreshCurrentLineData()
    }

    private val _numberOfPeopleWaitingLine = MutableLiveData<Int?>()
    private val _numberOfNeededBus = MutableLiveData<Int?>()
    private val _waitingTimeInMin = MutableLiveData<Int?>()
    private val _dateTimeString = MutableLiveData<String?>().apply {
        value = "2000-01-01T00:00:00Z"
    }

    val numberOfPeopleWaitingLine: LiveData<Int?> get() = _numberOfPeopleWaitingLine
    val numberOfNeededBus: LiveData<Int?> get() = _numberOfNeededBus
    val waitingTimeInMin: LiveData<Int?> get() = _waitingTimeInMin
    val dateTimeString: LiveData<String?> get() = _dateTimeString

    var isSuccessCall = false

    fun refreshCurrentLineData() {
        // 서버의 currentLine Data call 후 전역변수에 저장
        // call 성공 or 실패 여부 isSuccessCall 에 저장
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
                    _numberOfPeopleWaitingLine.value = body?.numberOfPeopleWaiting
                    _numberOfNeededBus.value = body?.numberOfNeededBuses
                    _waitingTimeInMin.value = body?.waitingTimeInMin
                    //_dateTimeString.value = body?.dateTimeString
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