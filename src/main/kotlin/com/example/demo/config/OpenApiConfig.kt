package com.example.demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration	
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
		val securitySchemeName = "bearerAuth"

        return OpenAPI()
            .info(
                Info()
                    .title("My Spring Boot API")
                    .description("Tài liệu API cho hệ thống quản lý")
                    .version("v1.0.0")
            )
			// Khai báo security scheme loại Bearer JWT
            .components(
                Components().addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            // Áp dụng global cho tất cả endpoint
            .addSecurityItem(
                SecurityRequirement().addList(securitySchemeName)
            )
    }
}