package com.example.demo.service
import com.example.demo.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

import org.springframework.transaction.annotation.Transactional
import com.example.demo.model.User


@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional
    fun getOrCreateUser(firebaseUid: String, email: String, name: String?, phone: String?): User {
        var user = userRepository.findByFirebaseUid(firebaseUid)
        if (user == null) {
            val username = email.substringBefore("@") // Hoặc bạn có thể tạo username khác dựa trên email hoặc name
            return userRepository.save(User(
                firebaseUid = firebaseUid,
                email = email,
                name = name ?: username,
                username = username,
                phone = phone,
                password = "" // Không cần password vì sẽ xác thực qua Firebase
            ))
        } else {
            // Cập nhật thông tin mới nhất từ Firebase nếu cần
            user = user.copy(
                email = email,
                name = name ?: user.name,
                phone = phone ?: user.phone
            )
            userRepository.save(user)
        }

        return user
    }

    fun findByUsernameOrEmail(username: String, email: String): User? {
        return userRepository.findByUsernameOrEmail(username, email)
    }
}
