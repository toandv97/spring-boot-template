package com.example.demo.util

import org.springframework.stereotype.Component

import org.springframework.security.core.context.SecurityContextHolder
import jakarta.persistence.EntityManager

import com.example.demo.model.User
import com.example.demo.model.BaseEntity

import com.example.demo.exception.AuthorizationException

@Component("su")
class SecurityUtil (private val entityManager: EntityManager) {

	fun getCurrentUser(): User? {
		val authentication = SecurityContextHolder.getContext().authentication
		return if (authentication != null && authentication.isAuthenticated) {
			authentication.principal as? User
		} else {
			null
		}
	}

	fun isOwner(entity: BaseEntity): Boolean {
		val user = getCurrentUser()
		return user != null && entity.createdBy?.id == user.id
    }

	/**
     * Hàm check owner dùng chung cho bất kỳ Entity nào kế thừa BaseEntity
     * @param id: ID của entity cần check
     * @param entityClass: Tên class (ví dụ: 'PostCategory')
     */
    fun isOwner(id: Long, entityClass: String): Boolean {
        val user = getCurrentUser() ?: return false

        // Sử dụng EntityManager để tìm entity theo tên class một cách dynamic
        val entity = try {
            val clazz = Class.forName("com.example.demo.model.$entityClass")
            entityManager.find(clazz, id) as? BaseEntity
        } catch (e: Exception) {
            null
        }

        return entity?.createdBy?.id == user.id
    }

	fun validateOwnership(entity: BaseEntity) {
        if (!isOwner(entity)) {
            throw AuthorizationException("")
        }
    }
}