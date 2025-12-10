package com.example.student_activity.dto

data class StudentActivityRequest(
    val activityId: Long,
    val description: String? = null // optional description
)

