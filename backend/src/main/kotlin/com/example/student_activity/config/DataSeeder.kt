package com.example.student_activity.config

import com.example.student_activity.entity.Activity
import com.example.student_activity.entity.StudentActivity
import com.example.student_activity.entity.User
import com.example.student_activity.repository.ActivityRepository
import com.example.student_activity.repository.StudentActivityRepository
import com.example.student_activity.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DataSeeder(
    private val userRepository: UserRepository,
    private val activityRepository: ActivityRepository,
    private val studentActivityRepository: StudentActivityRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // 1️⃣ Seed some Activities
        if (activityRepository.count() == 0L) {
            val activities = listOf(
                Activity(type = "Sports", description = "Basketball, Volleyball, etc."),
                Activity(type = "Computer", description = "Computer lab tasks or coding"),
                Activity(type = "Class", description = "Attend lectures or lessons"),
                Activity(type = "Cleaning", description = "Cleaning school premises"),
                Activity(type = "Swimming", description = "Swimming practice or lessons"),
                Activity(type = "Home EC", description = "Home Economics tasks")
            )
            activityRepository.saveAll(activities)
            println("Seeded Activities")
        }

        // 2️⃣ Seed some Users
        if (userRepository.count() == 0L) {
            val users = listOf(
                User(email = "alice@example.com", name = "Alice", password = "pass123"),
                User(email = "bob@example.com", name = "Bob", password = "pass123")
            )
            userRepository.saveAll(users)
            println("Seeded Users")
        }

        // 3️⃣ Seed Student Activities with descriptions
        val allUsers = userRepository.findAll()
        val allActivities = activityRepository.findAll()

        if (studentActivityRepository.count() == 0L) {
            val studentActivities = mutableListOf<StudentActivity>()

            allUsers.forEach { user ->
                allActivities.forEach { activity ->
                    studentActivities.add(
                        StudentActivity(
                            student = user,
                            activity = activity,
                            createdAt = LocalDateTime.now().minusDays((1..5).random().toLong())
                        )
                    )
                }
            }

            studentActivityRepository.saveAll(studentActivities)
            println("Seeded Student Activities with descriptions")
        }
    }
}
