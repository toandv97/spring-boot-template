package com.example.demo.service.jwt

import com.example.demo.service.UserService
import com.google.firebase.auth.FirebaseAuth
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

import org.springframework.security.core.userdetails.UserDetails

@Service
@ConditionalOnProperty(prefix = "firebase.service-account", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class FirebaseTokenVerifier(
    private val userService: UserService
) : TokenVerifier {
    override fun verify(token: String): UserDetails? {
        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
        return userService.getOrCreateUser(
            decodedToken.uid,
            decodedToken.email,
            decodedToken.name,
            decodedToken.claims["phone_number"] as? String
        )
    }
}
