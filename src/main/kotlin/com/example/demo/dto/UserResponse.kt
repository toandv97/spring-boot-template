package com.example.demo.dto

data class UserResponse(
    val id: Long,
    val name: String,
    val username: String,
    val email: String? = null,
    val phone: String? = null,
    val role: String
)
