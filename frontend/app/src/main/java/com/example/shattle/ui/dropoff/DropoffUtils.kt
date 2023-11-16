package com.example.shattle.ui.dropoff

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

class DropoffUtils {

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
        val sizeSpan = AbsoluteSizeSpan(22, true)
        spannableString.setSpan(
            sizeSpan,
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // 색 변경
        val colorSpan = ForegroundColorSpan(Color.parseColor("#057FEE"))
        spannableString.setSpan(
            colorSpan,
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }
}