package com.example.student_activity.entity

import jakarta.persistence.*

@Table(name = "activities")
@Entity
data class Activity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 50)
    val type: String,

    @Column(nullable = true, length = 255)
    val description: String? = null
)
