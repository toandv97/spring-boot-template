package com.example.demo.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

import org.springframework.context.annotation.Bean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@Configuration
@ConditionalOnProperty(prefix = "firebase.service-account", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class FirebaseConfig(
    private val resourceLoader: ResourceLoader
) {

    @Value("\${firebase.service-account.path}")
    private lateinit var serviceAccountPath: String

    @Bean
    fun firebaseApp() : FirebaseApp {
        // Spring ResourceLoader rất thông minh:
        // - Nếu path bắt đầu bằng "file:", nó tìm trên ổ đĩa.
        // - Nếu path bắt đầu bằng "classpath:", nó tìm trong resources.
        val resource = resourceLoader.getResource(serviceAccountPath)

        // Kiểm tra xem file có tồn tại không để log lỗi rõ ràng hơn
        if (!resource.exists()) {
            throw Exception("Không tìm thấy file cấu hình Firebase")
        }
        
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(resource.inputStream))
            .build()

        return if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        } else {
            FirebaseApp.getInstance()
        }
    }
}
