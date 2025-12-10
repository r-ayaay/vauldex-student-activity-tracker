package com.example.student_activity.service

import com.example.student_activity.dto.ActivityDTO
import com.example.student_activity.entity.Activity
import com.example.student_activity.repository.ActivityRepository
import org.springframework.stereotype.Service

@Service
class ActivityServiceImpl(
    private val activityRepository: ActivityRepository
) : ActivityService {

    override fun getAllActivities(): List<ActivityDTO> {
        return activityRepository.findAll().map { it.toDTO() }
    }

    override fun addActivity(dto: ActivityDTO): ActivityDTO {
        val activity = Activity(type = dto.type, description = dto.description)
        val saved = activityRepository.save(activity)
        return saved.toDTO()
    }

    override fun updateActivity(id: Long, dto: ActivityDTO): ActivityDTO {
        val activity = activityRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Activity not found") }
        val updated = activity.copy(type = dto.type)
        return activityRepository.save(updated).toDTO()
    }

    override fun deleteActivity(id: Long) {
        val activity = activityRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Activity not found") }
        activityRepository.delete(activity)
    }

    override fun getActivityById(id: Long): ActivityDTO {
        val activity = activityRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Activity not found") }
        return activity.toDTO()
    }
}

// Extension function
fun Activity.toDTO() = ActivityDTO(
    id = this.id,
    type = this.type,
    description = this.description
)
