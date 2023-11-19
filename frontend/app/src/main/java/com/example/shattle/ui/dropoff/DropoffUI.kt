package com.example.shattle.ui.dropoff

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.example.shattle.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DropoffUI(
    val context: Context,
    val tv_numPeople: TextView,
    val tv_numTime: TextView,
    val tv_numBus: TextView,
    val tv_updatedTime: TextView,
    val imgv_man: ImageView,
    val layout_visualView: ConstraintLayout,
    val bt_refresh: Button,
    val imgv_bus1: ImageView,
    val imgv_bus2: ImageView,
    val imgv_bus3: ImageView,
    val imgv_bus4: ImageView,
) {

    val dropoffUtils = DropoffUtils()

    fun updateUI(dropoffUIState: DropoffUIState) {
        changeTextView(dropoffUIState)
        changeVisualView(dropoffUIState)
        changeUpdatedTime(dropoffUIState)
    }

    fun changeTextView(dropoffUIState: DropoffUIState) {
        // 숫자에 bold 적용, 색, 크기 변경 (기본크기: 20sp)

        val numPeopleStr = dropoffUIState.numPeople.toString()
        val numTimeStr = dropoffUIState.numTime.toString()
        val numBusStr = dropoffUIState.numBus.toString()

        val numPeopleText = "대기인원 ${numPeopleStr} 명 "
        val numTimeText = "예상 대기시간 ${numTimeStr} 분 "
        val numBusText = "다음 ${numBusStr}번째 버스 탑승 가능합니다."

        tv_numPeople.text = DropoffUtils.SpannableStringBuilder(numPeopleText)
            .applyBold(numPeopleText.indexOf(numPeopleStr), numPeopleStr.length + 2)
            .applyColor(
                ContextCompat.getColor(context, R.color.text_accent),
                numPeopleText.indexOf(numPeopleStr),
                numPeopleStr.length + 2
            )
            //.applySize(20)
            .applySize(23, numPeopleText.indexOf(numPeopleStr), numPeopleStr.length + 2)
            .build()

        tv_numTime.text = DropoffUtils.SpannableStringBuilder(numTimeText)
            .applyBold(numTimeText.indexOf(numTimeStr), numTimeStr.length + 2)
            .applyColor(
                ContextCompat.getColor(context, R.color.text_accent),
                numTimeText.indexOf(numTimeStr),
                numTimeStr.length + 2
            )
            //.applySize(20)
            .applySize(23, numTimeText.indexOf(numTimeStr), numTimeStr.length + 2)
            .build()

        tv_numBus.text = DropoffUtils.SpannableStringBuilder(numBusText)
            .applySize(18)
            .build()

    }

    fun changeVisualView(dropoffUIState: DropoffUIState) {
        changeBiasOfMan(dropoffUIState)
        changeColorOfBus(dropoffUIState)
        startAnimation(dropoffUIState)
    }

    fun changeBiasOfMan(dropoffUIState: DropoffUIState) {
        val constraintLayout = layout_visualView

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        // Bias 계산
        var bias = (dropoffUIState.numPeople / 120.0).toFloat()
        // 정류장 이미지랑 겹쳐서 최소 bias 0.0625 로 설정
        if (bias < 0.0625F)
            bias = 0.0625F
        constraintSet.setHorizontalBias(imgv_man.id, bias)
        // Apply the updated constraints to the ConstraintLayout.
        constraintSet.applyTo(constraintLayout)
    }

    val busImages = listOf(imgv_bus1, imgv_bus2, imgv_bus3, imgv_bus4)
    fun changeColorOfBus(dropoffUIState: DropoffUIState) {
        val numBus = dropoffUIState.numBus

        for (i: Int in 1..4) {
            var busImage = busImages[i - 1]
            if (i <= numBus) {
                busImage.setColorFilter(ContextCompat.getColor(context, R.color.bus_true))
            } else {
                busImage.setColorFilter(ContextCompat.getColor(context, R.color.bus_false))
            }
        }
    }

    //val animatorList: MutableList<ValueAnimator> = mutableListOf()
    var animator: ValueAnimator? = null
    fun startAnimation(dropoffUIState: DropoffUIState) {
        // 지금 실행중인 animator 중지
        animator?.cancel()

        val img = imgv_man

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                val color =
                    if (animatedValue > 0.1f) ContextCompat.getColor(context, R.color.man_true)
                    else ContextCompat.getColor(context, R.color.man_false)
                img.setColorFilter(color)
            }
        }
        animator?.start()
    }

    fun stopAnimation() {
        animator?.cancel()

        //for (animator in animatorList) animator.cancel()
    }

    fun changeUpdatedTime(dropoffUIState: DropoffUIState) {

        try {
            val dateTimeString = dropoffUIState.updatedTime
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            val dateTime = inputFormat.parse(dateTimeString)
            val outputFormat = SimpleDateFormat("MM.dd HH:mm:ss", Locale.getDefault())
            tv_updatedTime.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"

        } catch (e: Exception) {
            tv_updatedTime.text = ""
        }
    }

}