package com.example.demo.service

import org.springframework.stereotype.Service

import org.springframework.security.crypto.password.PasswordEncoder

import com.example.demo.repository.UserRepository
import com.example.demo.model.User

import com.example.demo.dto.RegisterRequest
import com.example.demo.dto.LoginRequest
import com.example.demo.dto.AuthResponse
import com.example.demo.dto.UserResponse

import com.example.demo.exception.ConflictException
import com.example.demo.exception.NotFoundException
import com.example.demo.exception.AuthenticationException

import com.example.demo.util.JwtUtil

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun register(request: RegisterRequest): UserResponse {
        if (userRepository.findByUsernameOrEmail(request.username, request.email) != null) {
            throw ConflictException("Username or email already exists")
        }
        val user = User(
            name = request.name,
            username = request.username,
            email = request.email,
            phone = request.phone,
            password = passwordEncoder.encode(request.password)!!
        )
        userRepository.save(user)
        return UserResponse(user.id, user.name, user.username, user.email, user.phone, user.role)
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByUsernameOrEmail(request.username, request.username)
            ?: throw AuthenticationException("Username or password is invalid!")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw AuthenticationException("Username or password is invalid!")
        }
        val token = jwtUtil.generateToken(user)
        return AuthResponse(token, UserResponse(
            user.id,
            user.name,
            user.username,
            user.email,
            user.phone,
            user.role
        ))
    }
}
