package com.example.shattle.ui.circular;

import android.content.Context
import android.content.SharedPreferences;
import com.example.shattle.data.models.RunningBuses
import com.google.gson.Gson

class RunningBusesDataSource(context: Context) {

    private val keyFragment = "Circular"
    private var sharedPref: SharedPreferences? =
            context.getSharedPreferences(keyFragment, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor? = sharedPref?.edit()

    private val keyRunningBuses = "RunningBuses"
    private val keyRunningBuses_prev = "RunningBuses_prev"
    private val DEFAULT_VALUE = RunningBuses(-2, emptyList())

    fun getRunningBuses(): RunningBuses {
        // sharedPref 에서 Running Buses Data 반환

        val lambdaSerialized = sharedPref?.getString(keyRunningBuses, null)

        return if (lambdaSerialized != null) {
            // 이전에 저장된 데이터가 있는 경우 reverse serialize 해서 반환
            val gson = Gson()
            gson.fromJson(lambdaSerialized, RunningBuses::class.java)
        } else {
            // 저장된 데이터가 없는 경우 기본값 반환
            DEFAULT_VALUE
        }
    }

    fun storeRunningBuses(runningBuses: RunningBuses) {
        // sharedPref 에 Running Buses Data 저장

        val gson = Gson()
        val lambdaSerialized = gson.toJson(runningBuses)

        // 데이터를 SharedPreferences에 저장
        editor?.putString(keyRunningBuses, lambdaSerialized)
        editor?.apply()
    }

    fun getRunningBuses_prev(): RunningBuses {
        // sharedPref 에서 Running Buses Data 반환

        val lambdaSerialized = sharedPref?.getString(keyRunningBuses_prev, null)

        return if (lambdaSerialized != null) {
            // 이전에 저장된 데이터가 있는 경우 reverse serialize 해서 반환
            val gson = Gson()
            gson.fromJson(lambdaSerialized, RunningBuses::class.java)
        } else {
            // 저장된 데이터가 없는 경우 기본값 반환
            DEFAULT_VALUE
        }
    }

    fun storeRunningBuses_prev(runningBuses: RunningBuses) {
        // sharedPref 에 Running Buses Data 저장

        val gson = Gson()
        val lambdaSerialized = gson.toJson(runningBuses)

        // 데이터를 SharedPreferences에 저장
        editor?.putString(keyRunningBuses_prev, lambdaSerialized)
        editor?.apply()
    }

}
