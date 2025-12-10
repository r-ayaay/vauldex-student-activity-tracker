package com.example.student_activity.dto

data class StudentActivityResponse(
    val id: Long,
    val activity: ActivityDTO,
    val createdAt: String
)
