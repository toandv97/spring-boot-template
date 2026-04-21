package com.example.demo.dto

data class PostCategoryResponse(
    val id: Long,
    val name: String,
    val slug: String,
    val status: String,
    val description: String,
	val createdAt: String,
	val updatedAt: String
)
