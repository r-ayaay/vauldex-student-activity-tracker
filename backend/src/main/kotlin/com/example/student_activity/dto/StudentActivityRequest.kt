package com.example.student_activity.dto

data class StudentActivityRequest(
    val activityId: Long?,       // optional for when updating timestamp only
    val createdAt: String?       // ISO 8601 string: "2025-12-10T15:30"
)


