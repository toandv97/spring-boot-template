package com.example.demo.repository

import com.example.demo.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByUsernameOrEmail(username: String, email: String): User?
    fun findByFirebaseUid(firebaseUid: String): User?
}
