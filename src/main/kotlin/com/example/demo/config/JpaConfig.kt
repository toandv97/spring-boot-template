package com.example.demo.config

import com.example.demo.model.User
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class JpaConfig

@Component("auditorProvider")
class SecurityAuditorAware : AuditorAware<User> {
    override fun getCurrentAuditor(): Optional<User> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth == null || !auth.isAuthenticated || auth.principal == "anonymousUser") {
            return Optional.empty()
        }

        val user = auth.principal as? User

        return Optional.ofNullable(user)
    }
}