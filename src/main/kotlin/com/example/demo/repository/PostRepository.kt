package com.example.demo.repository

import com.example.demo.model.Post
import com.example.demo.model.PostStatus

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findBySlug(slug: String): Post?
    fun findByTitleContainingIgnoreCase(title: String): List<Post>?
    fun findByTitleContainingIgnoreCaseAndStatus(title: String, status: PostStatus): List<Post>?
    fun findByTitleContainingIgnoreCaseAndStatusOrderByUpdatedAtDesc(title: String, status: PostStatus): List<Post>?
    fun findByStatus(status: PostStatus, pageable: Pageable): Page<Post>
    fun existsBySlug(slug: String): Boolean
    fun existsBySlugAndIdNot(slug: String, id: Long): Boolean

	// @Query với JPQL (dùng tên Entity/field, không phải tên bảng)
    @Query("SELECT p FROM Post p WHERE p.category = :categoryId ORDER BY p.createdAt DESC")
    fun find(@Param("categoryId") categoryId: Long): List<Post>

    // @Query với native SQL (dùng tên bảng thật)
    @Query("SELECT * FROM posts WHERE status = 'ACTIVE' LIMIT :limit", nativeQuery = true)
    fun findActiveRaw(@Param("limit") limit: Int): List<Post>

    // Modifying query (UPDATE / DELETE)
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.status = :status WHERE p.id = :id")
    fun updateStatus(@Param("id") id: Long, @Param("status") status: PostStatus): Int
}
