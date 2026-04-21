package com.example.demo.dto

import jakarta.validation.constraints.*

data class PostCategoryRequest(
    @field:NotBlank(message = "Category name is required")
	@field:Size(max = 256, message = "Name must be less than 256 characters")
    val name: String,

	@field:Size(max = 256, message = "Slug must be less than 256 characters")
	@Pattern(
        regexp = "^[a-z0-9-]*$",
        message = "Slug must be lowercase, alphanumeric, and hyphenated (e.g., 'my-post-title')"
    )
    var slug: String,

    val description: String
)
