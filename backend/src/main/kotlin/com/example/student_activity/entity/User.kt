package com.example.student_activity.entity

import jakarta.persistence.*

@Table(name = "users")
@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false, length = 50)
    val email: String,

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(nullable = false)
    val password: String,
)

