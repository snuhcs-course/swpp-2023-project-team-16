package com.example.shattle.dropoff

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.text.getSpans
import com.example.shattle.ui.dropoff.DropoffUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DropoffUtilsTest {

    private lateinit var inputString: String
    private lateinit var spannableStringBuilder: DropoffUtils.SpannableStringBuilder

    @Before
    fun setUp() {
        inputString = "Test String"
        spannableStringBuilder = DropoffUtils.SpannableStringBuilder(inputString)
    }

    @Test
    fun buildTest() {
        // Act
        val result = spannableStringBuilder.build()

        // Assert
        assertEquals("결과 문자열이 원본과 동일", inputString, result.toString())
    }

    @Test
    fun applyBoldTest1() {
        // Act
        val result = spannableStringBuilder.applyBold().build()

        // Assert
        val spans = result.getSpans(0, result.length, StyleSpan::class.java)
        assertTrue("모든 부분에 bold 적용", spans.any { it.style == Typeface.BOLD })
    }

    @Test
    fun applyBoldTest2() {
        // Arrange
        val startIndex = 5
        val length = 3 // "Str"

        // Act
        val result = spannableStringBuilder.applyBold(startIndex, length).build()

        // Assert
        val spans = result.getSpans(startIndex, startIndex + length, StyleSpan::class.java)
        assertTrue("특정 부분에 bold 적용", spans.any { it.style == Typeface.BOLD })
    }

    @Test
    fun applySizeTest1() {
        // Arrange
        val size = 24

        // Act
        val result = spannableStringBuilder.applySize(size).build()

        // Assert
        val spans = result.getSpans(0, result.length, AbsoluteSizeSpan::class.java)
        assertTrue("모든 부분에 크기 변경 적용", spans.any { it.size == size })
    }

    @Test
    fun applySizeTest2() {
        // Arrange
        val size = 24
        val startIndex = 5
        val length = 3

        // Act
        val result = spannableStringBuilder.applySize(size, startIndex, length).build()

        // Assert
        val spans = result.getSpans(startIndex, startIndex + length, AbsoluteSizeSpan::class.java)
        assertTrue("특정 부분에 크기 변경 적용", spans.any { it.size == size })
    }

    @Test
    fun applyColorTest1() {
        // Arrange
        val color = Color.RED

        // Act
        val result = spannableStringBuilder.applyColor(color).build()

        // Assert
        val spans = result.getSpans(0, result.length, ForegroundColorSpan::class.java)
        assertTrue("모든 부분에 색상 적용", spans.any { it.foregroundColor == color })
    }

    @Test
    fun applyColorTest2() {
        // Arrange
        val color = Color.RED
        val startIndex = 5
        val length = 3

        // Act
        val result = spannableStringBuilder.applyColor(color, startIndex, length).build()

        // Assert
        val spans =
            result.getSpans(startIndex, startIndex + length, ForegroundColorSpan::class.java)
        assertTrue("특정 부분에 색상 적용", spans.any { it.foregroundColor == color })
    }
}