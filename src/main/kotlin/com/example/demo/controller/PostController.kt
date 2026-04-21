package com.example.demo.controller

import com.example.demo.dto.PostResponse
import com.example.demo.dto.PostRequest

import com.example.demo.dto.PostCategoryResponse
import com.example.demo.dto.PostCategoryRequest
import com.example.demo.service.PostService

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Post API")
class PostController(private val postService: PostService) {

	@GetMapping("/posts")
	fun getPosts(@RequestParam(defaultValue = "0") page: Int,
				 @RequestParam(defaultValue = "10") size: Int): ResponseEntity<Page<PostResponse>> =
			ResponseEntity.ok(postService.getActivePosts(page, size))

	@GetMapping("/posts/{id}")
	fun getPost(@PathVariable id: Long): ResponseEntity<PostResponse> =
			ResponseEntity.ok(postService.getPostById(id))

    @PostMapping("/posts")
    fun createPost(@Valid @RequestBody request: PostRequest): ResponseEntity<PostResponse> =
            ResponseEntity.status(201).body(postService.createPost(request))

    @PutMapping("/posts/{id}")
    fun updatePost(@PathVariable id: Long, @Valid @RequestBody request: PostRequest): ResponseEntity<PostResponse> =
            ResponseEntity.ok(postService.updatePost(id, request))

	@DeleteMapping("/posts/{id}")
	fun deletePost(@PathVariable id: Long): ResponseEntity<Void> {
		postService.deletePost(id)
		return ResponseEntity.noContent().build()
	}

	@GetMapping("/post-categories")
	fun getCategories(@RequestParam(defaultValue = "0") page: Int,
					   @RequestParam(defaultValue = "10") size: Int): ResponseEntity<Page<PostCategoryResponse>> =
			ResponseEntity.ok(postService.getCategories(page, size))

	@GetMapping("/post-categories/{id}")
	fun getCategory(@PathVariable id: Long): ResponseEntity<PostCategoryResponse> =
			ResponseEntity.ok(postService.getCategoryById(id))


	@PostMapping("/post-categories")
    fun createCategory(@Valid @RequestBody request: PostCategoryRequest): ResponseEntity<PostCategoryResponse> =
            ResponseEntity.status(201).body(postService.createCategory(request))

    @PutMapping("/post-categories/{id}")
    fun updateCategory(@PathVariable id: Long, @Valid @RequestBody request: PostCategoryRequest): ResponseEntity<PostCategoryResponse> =
            ResponseEntity.ok(postService.updateCategory(id, request))

	@DeleteMapping("/post-categories/{id}")
	fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
		postService.deleteCategory(id)
		return ResponseEntity.noContent().build()
	}
}
