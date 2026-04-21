package com.example.demo.service.jwt

import org.springframework.security.core.userdetails.UserDetails

interface TokenVerifier {
    fun verify(token: String): UserDetails?
}
