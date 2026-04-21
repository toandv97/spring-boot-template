package com.example.demo.service.jwt

import com.example.demo.service.UserService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

import org.springframework.security.core.userdetails.UserDetails

import com.example.demo.util.JwtUtil

@Service
@ConditionalOnProperty(prefix = "firebase.service-account", name = ["enabled"], havingValue = "false")
class SelfJwtTokenVerifier(private val jwtUtil: JwtUtil, private val userService: UserService) : TokenVerifier {
    override fun verify(token: String): UserDetails? {
        val username = jwtUtil.extractUsername(token)
        val email = jwtUtil.extractEmail(token)

        return userService.findByUsernameOrEmail(username, email ?: "")
    }
}