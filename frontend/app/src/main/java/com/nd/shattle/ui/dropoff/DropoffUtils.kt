package com.nd.shattle.ui.dropoff

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

object DropoffUtils {

    class SpannableStringBuilder(str: String) {
        private val spannableString = SpannableString(str)

        fun build(): SpannableString {
            return spannableString
        }

        fun applyBold(): SpannableStringBuilder {
            val boldStyle = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(
                boldStyle,
                0,
                spannableString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        fun applyBold(startIndex: Int, length: Int): SpannableStringBuilder {
            val boldStyle = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(
                boldStyle,
                startIndex,
                startIndex + length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        fun applySize(size: Int): SpannableStringBuilder {
            val sizeSpan = AbsoluteSizeSpan(size, true)
            spannableString.setSpan(
                sizeSpan,
                0,
                spannableString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        fun applySize(size: Int, startIndex: Int, length: Int): SpannableStringBuilder {
            val sizeSpan = AbsoluteSizeSpan(size, true)
            spannableString.setSpan(
                sizeSpan,
                startIndex,
                startIndex + length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        fun applyColor(color: Int): SpannableStringBuilder {
            val colorSpan = ForegroundColorSpan(color)
            spannableString.setSpan(
                colorSpan,
                0,
                spannableString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        fun applyColor(color: Int, startIndex: Int, length: Int): SpannableStringBuilder {
            val colorSpan = ForegroundColorSpan(color)
            spannableString.setSpan(
                colorSpan,
                startIndex,
                startIndex + length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }
    }

}