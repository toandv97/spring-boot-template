package com.example.demo.dto

data class AuthResponse(
    val token: String,
    val user: UserResponse
    // val refreshToken: String,
    // val tokenType: String = "Bearer",
    // val expiresAt: Long,
    // val refreshTokenExpiresAt: Long,
)
