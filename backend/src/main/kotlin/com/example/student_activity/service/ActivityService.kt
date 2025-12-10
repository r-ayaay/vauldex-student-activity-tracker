package com.example.student_activity.service

import com.example.student_activity.dto.ActivityDTO
import com.example.student_activity.entity.Activity

interface ActivityService {
    fun getAllActivities(): List<ActivityDTO>
    fun addActivity(dto: ActivityDTO): ActivityDTO
    fun updateActivity(id: Long, dto: ActivityDTO): ActivityDTO
    fun deleteActivity(id: Long)
    fun getActivityById(id: Long): ActivityDTO
}
