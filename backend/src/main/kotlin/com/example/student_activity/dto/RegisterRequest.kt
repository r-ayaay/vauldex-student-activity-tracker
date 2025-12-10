package com.example.student_activity.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank
    @field:Email
    val email: String, // changed from Email type to String

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val password: String
)
