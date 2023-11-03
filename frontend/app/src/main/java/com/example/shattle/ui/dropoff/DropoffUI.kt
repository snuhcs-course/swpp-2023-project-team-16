package com.example.shattle.ui.dropoff

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.shattle.data.models.CurrentLine

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
    }

    fun changeTextView(dropoffUIState: DropoffUIState) {

        tv_numPeople.text = "현재 ${dropoffUIState.numPeople}명 대기 중입니다."
        tv_numBus.text = "다음 ${dropoffUIState.numBus}번째 버스 탑승 가능합니다."
        tv_numTime.text = "예상 대기시간: ${dropoffUIState.numTime}분"

//        var inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
//        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
//        val dateTime = inputFormat.parse(dateTimeString)
//
//        val outputFormat = SimpleDateFormat("MM.dd HH:mm", Locale.getDefault())
//        binding.updatedTimeTextView.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"

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

}