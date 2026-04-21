package com.example.demo.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(unique = true, nullable = false)
    private val username: String,

    @Column(unique = true, nullable = false)
    val email: String,

    val phone: String? = null,

    @Column(nullable = false)
    private val password: String,

    @Column(unique = true, name = "firebase_uid")
    private val firebaseUid: String? = null,

    // Giả sử mỗi user chỉ có một role đơn giản, nếu bạn có nhiều role thì có thể dùng @ManyToMany
    // @ManyToMany(fetch = FetchType.EAGER)
    // val roles: Set<Role> = emptySet()
    val role: String = "USER",

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    var updatedAt: LocalDateTime = LocalDateTime.now(),

) : UserDetails {
    override fun getAuthorities(): List<SimpleGrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_$role"))
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
    // Thêm hàm tiện ích để lấy Firebase UID nếu cần
    fun getFirebaseUid(): String? = firebaseUid
}