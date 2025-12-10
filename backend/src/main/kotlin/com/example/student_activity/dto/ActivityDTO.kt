package com.example.student_activity.dto

data class ActivityDTO(
    val id: Long? = null,
    val type: String? = null,        // make nullable
    val description: String? = null
)
