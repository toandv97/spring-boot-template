package com.example.demo.service

import com.example.demo.model.Post
import com.example.demo.model.PostStatus
import com.example.demo.model.PostCategory
import com.example.demo.repository.PostRepository
import com.example.demo.repository.PostCategoryRepository
import com.example.demo.dto.PostCategoryRequest
import com.example.demo.dto.PostCategoryResponse
import com.example.demo.dto.PostResponse
import com.example.demo.dto.PostRequest

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

import org.springframework.security.access.prepost.PreAuthorize

import com.example.demo.exception.NotFoundException
import com.example.demo.exception.ConflictException
import com.example.demo.exception.AuthenticationException

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepo: PostRepository,
    private val postCategoryRepo: PostCategoryRepository
) {

    fun getActivePosts(page: Int, size: Int): Page<PostResponse> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        return postRepo.findByStatus(PostStatus.ACTIVE, pageable).map { post ->
            PostResponse(
                id = post.id,
                title = post.title,
                slug = post.slug,
                content = post.content,
                categoryId = post.category.id,
                status = post.status.name,
                createdAt = post.createdAt.toString(),
                updatedAt = post.updatedAt.toString()
            )
        }
    }

    fun getPostById(id: Long): PostResponse {
        val post = postRepo.findById(id).orElseThrow { NotFoundException("Post not found") }
        return PostResponse(
            id = post.id,
            title = post.title,
            slug = post.slug,
            content = post.content,
            categoryId = post.category.id,
            status = post.status.name,
            createdAt = post.createdAt.toString(),
            updatedAt = post.updatedAt.toString()
        )
    }

    @Transactional
    fun createPost(request: PostRequest): PostResponse {
        val category = postCategoryRepo.findById(request.categoryId)
            .orElseThrow { NotFoundException("Category not found") }

        if (postRepo.existsBySlug(request.slug)) {
            throw ConflictException("Slug ${request.slug} already exists")
        }

        val post = postRepo.save(
            Post(
                title = request.title,
                slug = request.slug,
                content = request.content,
                category = category
            )
        )
        return PostResponse(
            id = post.id,
            title = post.title,
            slug = post.slug,
            content = post.content,
            categoryId = post.category.id,
            status = post.status.name,
            createdAt = post.createdAt.toString(),
            updatedAt = post.updatedAt.toString()
        )
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @su.isOwner(#id, 'Post')")
    fun updatePost(id: Long, request: PostRequest): PostResponse {
        val post = postRepo.findById(id).orElseThrow { NotFoundException("Post not found") }
        val category = postCategoryRepo.findById(request.categoryId)
            .orElseThrow { NotFoundException("Category not found") }

        if (postRepo.existsBySlugAndIdNot(request.slug, id)) {
            throw ConflictException("Slug ${request.slug} already exists")
        }

        post.title = request.title
        post.slug = request.slug
        post.content = request.content
        post.category = category
        
        postRepo.save(post)

        return PostResponse(
            id = post.id,
            title = post.title,
            slug = post.slug,
            content = post.content,
            categoryId = post.category.id,
            status = post.status.name,
            createdAt = post.createdAt.toString(),
            updatedAt = post.updatedAt.toString()
        )
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @su.isOwner(#id, 'Post')")
    fun deletePost(id: Long) {
        val post = postRepo.findById(id).orElseThrow { NotFoundException("Post not found") }
        postRepo.delete(post)
    }

    fun getCategories(page: Int, size: Int): Page<PostCategoryResponse> {
		val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
		return postCategoryRepo.findAll(pageable).map { category ->
			PostCategoryResponse(
				id = category.id,
				name = category.name,
                status = category.status.name,
				slug = category.slug,
				description = category.description,
				createdAt = category.createdAt.toString(),
				updatedAt = category.updatedAt.toString()
			)
		}
	}

    fun getCategoryById(id: Long): PostCategoryResponse {
		val category = postCategoryRepo.findById(id).orElseThrow { NotFoundException("Category not found") }
		return PostCategoryResponse(
			id = category.id,
			name = category.name,
			slug = category.slug,
            status = category.status.name,
			description = category.description,
			createdAt = category.createdAt.toString(),
			updatedAt = category.updatedAt.toString()
		)
	}

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
	fun createCategory(request: PostCategoryRequest): PostCategoryResponse {
        if (request.slug.isEmpty()) {
            request.slug = request.name.lowercase().replace("\\s+".toRegex(), "-").replace("[^a-z0-9-]".toRegex(), "")
        }
		if (postCategoryRepo.existsBySlug(request.slug)) {
			throw ConflictException("Slug ${request.slug} already exists")
		}

		val category = postCategoryRepo.save(
			PostCategory(
				name = request.name,
				slug = request.slug,
				description = request.description,
			)
		)
		return PostCategoryResponse(
			id = category.id,
			name = category.name,
            status = category.status.name,
			slug = category.slug,
			description = category.description,
			createdAt = category.createdAt.toString(),
			updatedAt = category.updatedAt.toString()
		)
	}

	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	fun deleteCategory(id: Long) {
		val category = postCategoryRepo.findById(id).orElseThrow { NotFoundException("Category not found") }
		postCategoryRepo.delete(category)
	}

	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	fun updateCategory(id: Long, request: PostCategoryRequest): PostCategoryResponse {
		val category = postCategoryRepo.findById(id).orElseThrow { NotFoundException("Category not found") }

        if (request.slug.isEmpty()) {
            request.slug = request.name.lowercase().replace("\\s+".toRegex(), "-").replace("[^a-z0-9-]".toRegex(), "")
        }
		if (category.slug != request.slug) {
			if (postCategoryRepo.existsBySlug(request.slug)) {
                throw ConflictException("Slug ${request.slug} already exists")
            }
		}
		category.name = request.name
		category.slug = request.slug
		category.description = request.description

		postCategoryRepo.save(category)
		return PostCategoryResponse(
			id = category.id,
			name = category.name,
			slug = category.slug,
            status = category.status.name,
			description = category.description,
			createdAt = category.createdAt.toString(),
			updatedAt = category.updatedAt.toString()
		)
	}
}
