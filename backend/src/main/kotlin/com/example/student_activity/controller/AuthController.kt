package com.example.student_activity.controller

import com.example.student_activity.dto.RegisterRequest
import com.example.student_activity.dto.LoginRequest
import com.example.student_activity.dto.UserResponse
import com.example.student_activity.repository.UserRepository
import com.example.student_activity.service.UserService
import com.example.student_activity.util.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {
        val userResponse = userService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse)
    }


    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Map<String, Any>> {
        val user = userRepository.findByEmail(request.email)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("error" to "Invalid credentials"))

        if (!passwordEncoder.matches(request.password, user.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("error" to "Invalid credentials"))
        }

        val token = jwtUtil.generateToken(user.email) // <-- use email
        return ResponseEntity.ok(
            mapOf(
                "token" to token,
                "email" to user.email,
                "user_id" to user.id
            )
        )
    }

}
