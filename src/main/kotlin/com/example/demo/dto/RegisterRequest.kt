package com.example.demo.dto

import jakarta.validation.constraints.*

data class RegisterRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    val name: String,

    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    val password: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,

    val phone: String? = null,
)
