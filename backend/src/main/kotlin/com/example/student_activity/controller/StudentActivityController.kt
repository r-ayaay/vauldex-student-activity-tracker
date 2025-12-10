package com.example.student_activity.controller

import com.example.student_activity.dto.StudentActivityRequest
import com.example.student_activity.dto.StudentActivityResponse
import com.example.student_activity.entity.User
import com.example.student_activity.repository.UserRepository
import com.example.student_activity.service.StudentActivityService
import com.example.student_activity.util.JwtUtil
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime

@RestController
@RequestMapping("/activities")
class StudentActivityController(
    private val studentActivityService: StudentActivityService, // <- inject the interface
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository,
) {

    // Replace this with actual JWT-based current user retrieval
    private fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.principal as? User
            ?: throw IllegalStateException("No authenticated user found")
    }


    @PostMapping
    fun addActivity(@RequestBody request: StudentActivityRequest): ResponseEntity<StudentActivityResponse> {
        val user = getCurrentUser()
        val response = studentActivityService.addActivity(user, request)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateActivity(
        @PathVariable id: Long,
        @RequestBody request: StudentActivityRequest
    ): ResponseEntity<StudentActivityResponse> {
        val user = getCurrentUser()
        val response = studentActivityService.updateActivity(id, request, user)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteActivity(@PathVariable id: Long): ResponseEntity<Void> {
        val user = getCurrentUser()
        studentActivityService.deleteActivity(id, user)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getActivities(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
    ): ResponseEntity<Page<StudentActivityResponse>> {
        val user = getCurrentUser()
        val start = startDate?.let { LocalDateTime.parse(it) }
        val end = endDate?.let { LocalDateTime.parse(it) }

        val response = studentActivityService.getStudentActivities(user, page, size, type, start, end)
        return ResponseEntity.ok(response)
    }
}
