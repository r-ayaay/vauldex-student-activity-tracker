package com.example.student_activity.service

import com.example.student_activity.dto.StudentActivityRequest
import com.example.student_activity.dto.StudentActivityResponse
import com.example.student_activity.entity.User
import org.springframework.data.domain.Page
import java.time.LocalDateTime

interface StudentActivityService {
    fun addActivity(student: User, request: StudentActivityRequest): StudentActivityResponse
    fun updateActivity(id: Long, request: StudentActivityRequest, student: User): StudentActivityResponse
    fun deleteActivity(id: Long, student: User)
    fun getStudentActivities(
        student: User,
        page: Int,
        size: Int,
        type: String? = null,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): Page<StudentActivityResponse>
}
