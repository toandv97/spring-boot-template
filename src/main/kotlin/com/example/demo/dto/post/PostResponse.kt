package com.example.demo.dto

data class PostResponse(
    val id: Long,
    val title: String,
    val slug: String,
    val status: String,
    val content: String,
    val categoryId: Long,
	val createdAt: String,
	val updatedAt: String
)
