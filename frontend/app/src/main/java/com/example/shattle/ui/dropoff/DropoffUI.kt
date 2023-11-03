package com.example.shattle.ui.dropoff

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.gms.maps.GoogleMap

class DropoffUI(
    val tv_numPeople: TextView,
    val tv_numBus: TextView,
    val tv_numTime: TextView,
    val tv_updatedTime: TextView,
    val imgv_man: ImageView,
    val layout_visualView: ConstraintLayout,
    val bt_refreshButton: Button
) {

    fun updateUI(dropoffUIState: DropoffUIState){
        changeTextView(dropoffUIState)
        changeVisualView(dropoffUIState)
        changeUpdatedTime(dropoffUIState)
    }

    fun changeTextView(dropoffUIState: DropoffUIState) {

        val boldStyle = StyleSpan(Typeface.BOLD)

        val numPeopleText = "현재 ${dropoffUIState.numPeople}명 대기 중입니다."
        val numBusText = "다음 ${dropoffUIState.numBus}번째 버스 탑승 가능합니다."
        val numTimeText = "예상 대기시간: ${dropoffUIState.numTime}분"

        val spannableNumPeople = SpannableString(numPeopleText)
        val spannableNumBus = SpannableString(numBusText)
        val spannableNumTime = SpannableString(numTimeText)

        val startIndexPeople = numPeopleText.indexOf(dropoffUIState.numPeople.toString())
        val endIndexPeople = startIndexPeople + dropoffUIState.numPeople.toString().length

        val startIndexBus = numBusText.indexOf(dropoffUIState.numBus.toString())
        val endIndexBus = startIndexBus + dropoffUIState.numBus.toString().length

        val startIndexTime = numTimeText.indexOf(dropoffUIState.numTime.toString())
        val endIndexTime = startIndexTime + dropoffUIState.numTime.toString().length

        spannableNumPeople.setSpan(boldStyle, startIndexPeople, endIndexPeople, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableNumBus.setSpan(boldStyle, startIndexBus, endIndexBus, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableNumTime.setSpan(boldStyle, startIndexTime, endIndexTime, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tv_numPeople.text = spannableNumPeople
        tv_numBus.text = spannableNumBus
        tv_numTime.text = spannableNumTime

    }

    fun changeVisualView(dropoffUIState: DropoffUIState) {
        val constraintLayout = layout_visualView

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        // Change the bias values as desired.
        var bias = (dropoffUIState.numPeople / 120.0).toFloat()
        // 정류장 이미지랑 겹쳐서 최소 bias 0.15로 설정
        if(bias < 0.15F)
            bias = 0.15F
        constraintSet.setHorizontalBias(imgv_man.id, (bias))
        // Apply the updated constraints to the ConstraintLayout.
        constraintSet.applyTo(constraintLayout)
    }

    fun changeUpdatedTime(dropoffUIState: DropoffUIState){
        // TODO
        var dateTimeString = "2023-01-23T12:34:56Z"
        tv_updatedTime
//        var inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
//        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
//        val dateTime = inputFormat.parse(dateTimeString)
//
//        val outputFormat = SimpleDateFormat("MM.dd HH:mm", Locale.getDefault())
//        binding.updatedTimeTextView.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"
    }

}