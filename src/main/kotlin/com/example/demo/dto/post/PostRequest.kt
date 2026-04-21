package com.example.demo.dto

import jakarta.validation.constraints.*

data class PostRequest(
    @field:NotBlank(message = "Title is required")
	@field:Size(max = 256, message = "Title must be less than 256 characters")
    val title: String,

	@field:Size(max = 256, message = "Slug must be less than 256 characters")
    @Pattern(
        regexp = "^[a-z0-0]+(?:-[a-z0-9]+)*$",
        message = "Slug must be lowercase, alphanumeric, and hyphenated (e.g., 'my-post-title')"
    )
    val slug: String,

    @field:NotBlank(message = "Content is required")
    val content: String,

    // @field:NotBlank(message = "Category is required")
    @field:Min(value = 1, message = "Category must be a valid ID")
    val categoryId: Long
)
