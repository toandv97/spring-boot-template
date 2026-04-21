package com.example.demo.controller

import com.example.demo.dto.UserResponse
import com.example.demo.dto.AuthResponse
import com.example.demo.dto.LoginRequest
import com.example.demo.dto.RegisterRequest
import com.example.demo.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "API for user authentication")
class AuthController(private val authService: AuthService) {

    @Operation(security = [])
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> =
            ResponseEntity.status(201).body(authService.register(request))

    @Operation(security = [])
    @PostMapping("/login", produces = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> =
            ResponseEntity.ok(authService.login(request))
}
