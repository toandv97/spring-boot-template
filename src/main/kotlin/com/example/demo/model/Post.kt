package com.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "posts")
class Post(
    @Column(nullable = false, length = 256)
    var title: String,

	@Column(nullable = false, length = 256, unique = true)
    var slug: String,
	
	@Column(columnDefinition = "TEXT")
    var content: String,

	@Enumerated(EnumType.STRING)
    var status: PostStatus = PostStatus.ACTIVE,

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: PostCategory,

) : BaseEntity()

enum class PostStatus { ACTIVE, INACTIVE }
