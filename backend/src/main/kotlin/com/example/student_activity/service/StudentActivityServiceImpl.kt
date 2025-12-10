package com.example.student_activity.service

import com.example.student_activity.dto.ActivityDTO
import com.example.student_activity.dto.StudentActivityRequest
import com.example.student_activity.dto.StudentActivityResponse
import com.example.student_activity.entity.StudentActivity
import com.example.student_activity.entity.User
import com.example.student_activity.repository.ActivityRepository
import com.example.student_activity.repository.StudentActivityRepository
import com.example.student_activity.service.StudentActivityService
import jakarta.persistence.criteria.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class StudentActivityServiceImpl(
    private val studentActivityRepository: StudentActivityRepository,
    private val activityRepository: ActivityRepository
) : StudentActivityService {

    override fun addActivity(student: User, request: StudentActivityRequest): StudentActivityResponse {
        val activity = activityRepository.findById(request.activityId)
            .orElseThrow { IllegalArgumentException("Activity not found") }

        val studentActivity = StudentActivity(
            student = student,
            activity = activity,
        )
        val saved = studentActivityRepository.save(studentActivity)
        return saved.toResponse()
    }

    override fun updateActivity(id: Long, request: StudentActivityRequest, student: User): StudentActivityResponse {
        val studentActivity = studentActivityRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Activity log not found") }

        if (studentActivity.student.id != student.id) {
            throw IllegalAccessException("Cannot update another user's activity")
        }

        val activity = activityRepository.findById(request.activityId)
            .orElseThrow { IllegalArgumentException("Activity not found") }

        val updated = studentActivity.copy(
            activity = activity
        )
        return studentActivityRepository.save(updated).toResponse()
    }


    override fun deleteActivity(id: Long, student: User) {
        val studentActivity = studentActivityRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Activity log not found") }

        if (studentActivity.student.id != student.id) {
            throw IllegalAccessException("Cannot delete another user's activity")
        }

        studentActivityRepository.delete(studentActivity)
    }

    override fun getStudentActivities(
        student: User,
        page: Int,
        size: Int,
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Page<StudentActivityResponse> {

        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())

        val spec = Specification<StudentActivity> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(cb.equal(root.get<User>("student"), student))

            if (!type.isNullOrBlank()) {
                predicates.add(cb.equal(root.get<StudentActivity>("activity").get<String>("type"), type))
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate))
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate))
            }

            cb.and(*predicates.toTypedArray())
        }

        val pageResult = studentActivityRepository.findAll(spec, pageable)
        return pageResult.map { it.toResponse() }
    }

}

// Extension function to map entity to DTO
fun StudentActivity.toResponse() = StudentActivityResponse(
    id = this.id,
    activity = ActivityDTO(
        id = this.activity.id,
        type = this.activity.type,
        description = this.activity.description,
    ),
    createdAt = this.createdAt.toString()
)
