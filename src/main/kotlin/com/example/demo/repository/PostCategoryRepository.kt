package com.example.demo.repository

import com.example.demo.model.PostCategory
import com.example.demo.model.PostCategoryStatus

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostCategoryRepository : JpaRepository<PostCategory, Long> {
    fun findBySlug(slug: String): PostCategory?
    fun findByNameContainingIgnoreCase(name: String): List<PostCategory>?
    fun findByStatus(status: PostCategoryStatus): List<PostCategory>?
    fun existsBySlug(slug: String): Boolean
    fun existsBySlugAndIdNot(slug: String, id: Long): Boolean
}
