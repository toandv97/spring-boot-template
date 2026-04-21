package com.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "post_categories")
class PostCategory (

    @Column(nullable = false, length = 256)
    var name: String,

	@Column(nullable = false, length = 256, unique = true)
    var slug: String,

    @Column()
    var description: String,

	@Enumerated(EnumType.STRING)
    var status: PostCategoryStatus = PostCategoryStatus.ACTIVE,

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val items: MutableList<Post> = mutableListOf(),
    
) : BaseEntity()

enum class PostCategoryStatus { ACTIVE, INACTIVE }
