package com.example.shattle.ui.dropoff

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.shattle.R
import com.google.android.gms.maps.GoogleMap
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DropoffUI(
    val tv_numPeople: TextView,
    val tv_numBus: TextView,
    val tv_numTime: TextView,
    val tv_updatedTime: TextView,
    val imgv_man: ImageView,
    val layout_visualView: ConstraintLayout,
    val bt_refresh: Button,
    val imgv_bus1: ImageView,
    val imgv_bus2: ImageView,
    val imgv_bus3: ImageView,
    val imgv_bus4: ImageView,
) {

    fun updateUI(dropoffUIState: DropoffUIState) {
        changeTextView(dropoffUIState)
        changeVisualView(dropoffUIState)
        changeUpdatedTime(dropoffUIState)
    }

    fun changeTextView(dropoffUIState: DropoffUIState) {
        // 숫자 bold 적용, 크기 30sp 로 변경 (기본: 20sp)

        val numPeopleText = "현재 ${dropoffUIState.numPeople}명 대기 중입니다."
        val numBusText = "다음 ${dropoffUIState.numBus}번째 버스 탑승 가능합니다."
        val numTimeText = "예상 대기시간: ${dropoffUIState.numTime}분 "

        tv_numPeople.text = applySpanAtNumber(
            numPeopleText,
            numPeopleText.indexOf(dropoffUIState.numPeople.toString()),
            dropoffUIState.numPeople.toString().length + 1
        )
        tv_numBus.text = applySpanAtNumber(
            numBusText,
            numBusText.indexOf(dropoffUIState.numBus.toString()),
            dropoffUIState.numBus.toString().length + 2
        )
        tv_numTime.text = applySpanAtNumber(
            numTimeText,
            numTimeText.indexOf(dropoffUIState.numTime.toString()),
            dropoffUIState.numTime.toString().length + 1
        )
    }

    fun applySpanAtNumber(str: String, startIndex: Int, targetLength: Int): SpannableString {
        // 해당 문자열에서 숫자 부분만 스타일 변경 (bold, 크기, 색)

        val spannableString = SpannableString(str)

        val endIndex = startIndex + targetLength

        // bold 로 변경
        val boldStyle = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(
            boldStyle,
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // 크기 변경
        val sizeSpan = AbsoluteSizeSpan(30, true)
        spannableString.setSpan(
            sizeSpan,
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // 색 변경
        val colorSpan = ForegroundColorSpan(Color.parseColor("#000000"))
        spannableString.setSpan(
            colorSpan,
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
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

        // Change the bias values as desired.
        var bias = (dropoffUIState.numPeople / 120.0).toFloat()
        // 정류장 이미지랑 겹쳐서 최소 bias 0.0625 로 설정
        if (bias < 0.0625F)
            bias = 0.0625F
        constraintSet.setHorizontalBias(imgv_man.id, (bias))
        // Apply the updated constraints to the ConstraintLayout.
        constraintSet.applyTo(constraintLayout)
    }

    val busImages = listOf(imgv_bus1, imgv_bus2, imgv_bus3, imgv_bus4)
    fun changeColorOfBus(dropoffUIState: DropoffUIState) {
        val numBus = dropoffUIState.numBus

        for (i: Int in 1..4) {
            var busImage = busImages[i - 1]
            if (i <= numBus) {
                busImage.setColorFilter(Color.parseColor("#000000"))
            } else {
                busImage.setColorFilter(Color.parseColor("#AAAAAA"))
            }
        }

    }

    //val animatorList: MutableList<ValueAnimator> = mutableListOf()
    var animator: ValueAnimator? = null
    fun startAnimation(dropoffUIState: DropoffUIState) {
        val img = imgv_man

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                val color = if (animatedValue > 0.5f) Color.BLACK else Color.WHITE
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