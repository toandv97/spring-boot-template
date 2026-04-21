package com.example.demo.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.core.Authentication
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Operation

import com.example.demo.util.SecurityUtil
import com.example.demo.dto.UserResponse

@RestController
@RequestMapping("/api")
@Tag(name = "Home API", description = "API for homepage")
class HomeController(private val su: SecurityUtil) {

    @Operation(security = [])
    @GetMapping("/hello")
    fun hello(@RequestParam name: String?): ResponseEntity<String> {
        if (name.isNullOrBlank()) {
            return ResponseEntity.ok("Hello, World!")
        }
        return ResponseEntity.ok("Hello, $name!")
    }


    @Operation(security = [])
    @GetMapping("/verify")
    fun verify(): ResponseEntity<UserResponse> {
        val currentUser = su.getCurrentUser()
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        return ResponseEntity.ok(UserResponse(
            id = currentUser.id,
            name = currentUser.name,
            email = currentUser.email,
            phone = currentUser.phone,
            username = currentUser.username,
            role = currentUser.role
        ))
    }
}
