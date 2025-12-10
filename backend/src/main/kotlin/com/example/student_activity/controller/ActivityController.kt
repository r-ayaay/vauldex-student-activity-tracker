package com.example.student_activity.controller

import com.example.student_activity.dto.ActivityDTO
import com.example.student_activity.service.ActivityService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/activity") // singular for managing activities
class ActivityController(
    private val activityService: ActivityService
) {

    @GetMapping
    fun getAllActivities(): ResponseEntity<List<ActivityDTO>> {
        val activities = activityService.getAllActivities()
        return ResponseEntity.ok(activities)
    }

    @GetMapping("/{id}")
    fun getActivity(@PathVariable id: Long): ResponseEntity<ActivityDTO> {
        val activity = activityService.getActivityById(id)
        return ResponseEntity.ok(activity)
    }

    @PostMapping
    fun addActivity(@RequestBody dto: ActivityDTO): ResponseEntity<ActivityDTO> {
        val saved = activityService.addActivity(dto)
        return ResponseEntity.ok(saved)
    }

    @PutMapping("/{id}")
    fun updateActivity(@PathVariable id: Long, @RequestBody dto: ActivityDTO): ResponseEntity<ActivityDTO> {
        val updated = activityService.updateActivity(id, dto)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteActivity(@PathVariable id: Long): ResponseEntity<Void> {
        activityService.deleteActivity(id)
        return ResponseEntity.noContent().build()
    }
}
