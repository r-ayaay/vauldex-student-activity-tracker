package com.example.student_activity.service

import com.example.student_activity.dto.RegisterRequest
import com.example.student_activity.dto.UserResponse
import com.example.student_activity.entity.User
import com.example.student_activity.repository.UserRepository
import jakarta.validation.Valid
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(@Valid @RequestBody request: RegisterRequest): UserResponse {
        if (userRepository.findByEmail(request.email) != null) {
            throw IllegalArgumentException("Email already taken")
        }

        val hashedPassword = passwordEncoder.encode(request.password)

        val user = userRepository.save(
            User(
                email = request.email,
                name = request.name,
                password = hashedPassword
            )
        )

        // Return UserResponse so controller can access id, email, name
        return UserResponse(
            id = user.id,
            email = user.email,
            name = user.name
        )
    }
}
