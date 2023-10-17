package com.example.shattle.data.models

import java.util.UUID
import java.time.LocalTime
import java.time.LocalDateTime

data class ResponseCongestion(
    val id: UUID,
    val day: Int,
    val timeSlotStart: LocalTime,
    val timeSlotEnd: LocalTime,
    val averagePeopleWaiting: Int,
    val updatedAt: LocalDateTime
)
