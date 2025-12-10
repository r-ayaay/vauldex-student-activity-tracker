package com.example.student_activity.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "student_activities")
@Entity
data class StudentActivity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    val student: User,

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    val activity: Activity,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)


