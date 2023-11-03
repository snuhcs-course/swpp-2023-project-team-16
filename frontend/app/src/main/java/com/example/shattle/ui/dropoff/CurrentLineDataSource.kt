package com.example.shattle.ui.dropoff

import android.content.Context
import android.content.SharedPreferences
import com.example.shattle.data.models.CurrentLine
import com.google.gson.Gson

class CurrentLineDataSource(context: Context) {

    private val keyFragment = "Dropoff"
    private var sharedPref: SharedPreferences? = context.getSharedPreferences(keyFragment, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor? = sharedPref?.edit()

    private val keyCurrentLine = "CurrentLine"
    private val keyCurrentLine_prev = "CurrentLine_prev"
    private val DEFAULT_VALUE = CurrentLine(-2, -2, -2)


    fun getCurrentLine(): CurrentLine {
        val lambdaSerialized = sharedPref?.getString(keyCurrentLine, null)

        return if (lambdaSerialized != null) {
            // 이전에 저장된 데이터가 있는 경우 역직렬화하여 반환
            val gson = Gson()
            gson.fromJson(lambdaSerialized, CurrentLine::class.java)
        } else {
            // 저장된 데이터가 없는 경우 기본값 반환
            DEFAULT_VALUE
        }
    }

    fun storeCurrentLine(currentLine: CurrentLine) {
        val gson = Gson()
        val lambdaSerialized = gson.toJson(currentLine)

        // 데이터를 SharedPreferences에 저장
        editor?.putString(keyCurrentLine, lambdaSerialized)
        editor?.apply()
    }

    fun getCurrentLine_prev(): CurrentLine {
        val lambdaSerialized = sharedPref?.getString(keyCurrentLine_prev, null)

        return if (lambdaSerialized != null) {
            // 이전에 저장된 데이터가 있는 경우 역직렬화하여 반환
            val gson = Gson()
            gson.fromJson(lambdaSerialized, CurrentLine::class.java)
        } else {
            // 저장된 데이터가 없는 경우 기본값 반환
            DEFAULT_VALUE
        }
    }

    fun storeCurrentLine_prev(currentLine: CurrentLine) {
        val gson = Gson()
        val lambdaSerialized = gson.toJson(currentLine)

        // 데이터를 SharedPreferences에 저장
        editor?.putString(keyCurrentLine_prev, lambdaSerialized)
        editor?.apply()
    }

}