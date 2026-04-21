# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 4.0.5 Kotlin application with the following architecture:

- **Framework**: Spring Boot 4.0.5 with Kotlin 2.2.21
- **Database**: MySQL with JPA/Hibernate and Flyway migrations
- **Authentication**: JWT-based with Spring Security
- **API Documentation**: SpringDoc OpenAPI 3.0.2
- **Virtual Threads**: Enabled (Project Loom)

## Key Components

### Domain Models
- `User`: JPA entity with Spring Security integration
- `Post`: Blog post entity with status management
- `PostCategory`: Category entity for organizing posts

### Controllers
- `AuthController`: Authentication endpoints (`/api/auth/*`)
- `PostController`: Post management endpoints (`/api/v1/posts/*`)
- `HomeController`: Basic home endpoints

### Services
- `AuthService`: User registration and authentication
- `PostService`: Post CRUD operations with ownership checks
- `JwtService`: JWT token generation and validation

## Development Commands

### Building and Running
```bash
# Build the project
./gradlew build

# Run the application (local profile)
./gradlew bootRun

# Run with debug mode (port 5005)
./gradlew bootRun --debug-jvm

# Build OCI image
./gradlew bootBuildImage
```

### Database
```bash
# Run Flyway migrations
./gradlew flywayMigrate

# Clean database
./gradlew flywayClean
```

### Testing
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.example.demo.DemoApplicationTests"

# Run tests with specific profile
./gradlew test --args="--spring.profiles.active=test"
```

## Configuration

### Environment Variables
- `SPRING_PROFILES_ACTIVE`: Profile to use (default: local)
- `DB_HOST`, `DB_PORT`, `DB_NAME`: Database connection
- `DB_USERNAME`, `DB_PASSWORD`: Database credentials
- `JWT_SECRET`: JWT signing secret
- `SERVER_PORT`: Application port (default: 8080)

### Profiles
- `local`: Development profile with local database
- `prod`: Production profile

## API Structure

### Authentication
- `POST /api/auth/register`: User registration
- `POST /api/auth/login`: User login

### Posts
- `GET /api/v1/posts`: List posts (paginated)
- `GET /api/v1/posts/{id}`: Get single post
- `POST /api/v1/posts`: Create post (requires auth)
- `PUT /api/v1/posts/{id}`: Update post (owner/admin only)
- `DELETE /api/v1/posts/{id}`: Delete post (owner/admin only)

### Categories
- `GET /api/v1/post-categories`: List categories (paginated)
- `GET /api/v1/post-categories/{id}`: Get single category
- `POST /api/v1/post-categories`: Create category (admin only)
- `PUT /api/v1/post-categories/{id}`: Update category (admin only)
- `DELETE /api/v1/post-categories/{id}`: Delete category (admin only)

## Security

### Authentication
- JWT tokens with configurable expiration (default: 5 minutes)
- Refresh tokens with configurable expiration (default: 30 minutes)
- Role-based access control (USER, ADMIN)

### Authorization
- `@PreAuthorize` annotations for method-level security
- Ownership checks for post operations
- Admin-only access for category management

## Code Architecture

### Package Structure
```
com.example.demo
├── controller          # REST controllers
├── dto                # Data transfer objects
├── model              # JPA entities
├── repository         # Spring Data JPA repositories
├── service            # Business logic
├── config             # Configuration classes
└── exception          # Custom exceptions
```

### Key Patterns
- **DTO Pattern**: Separate request/response objects from entities
- **Service Layer**: Business logic separated from controllers
- **Repository Pattern**: Spring Data JPA for data access
- **JWT Security**: Stateless authentication with Spring Security

## Database Schema

### Tables
- `users`: User accounts with authentication data
- `posts`: Blog posts with status and relationships
- `post_categories`: Categories for organizing posts

### Relationships
- `Post` ←→ `User`: Many-to-one (createdBy, updatedBy)
- `Post` ←→ `PostCategory`: Many-to-one (category)
- `PostCategory` ←→ `Post`: One-to-many (items)

## OpenAPI Documentation

- Available at `/swagger-ui.html` or `/v3/api-docs`
- Auto-generated from controller annotations
- Includes security definitions for JWT authentication

## Testing Strategy

### Unit Tests
- Service layer tests with mock repositories
- Controller tests with mock MVC
- Utility class tests

### Integration Tests
- Repository tests with test database
- End-to-end API tests
- Security tests for authentication/authorization

## Development Notes

### Important Considerations
- Use DTOs for all API requests/responses
- Validate all input with `@Valid` annotations
- Implement proper error handling with custom exceptions
- Use Spring Security for all protected endpoints
- Follow the established naming conventions
- Maintain the separation of concerns between layers

### Performance
- Virtual threads enabled for better concurrency
- Lazy loading for relationships to prevent N+1 queries
- Pagination for list endpoints
- Database indexes on frequently queried columns

### Security Best Practices
- Never expose entity objects directly in APIs
- Use strong JWT secrets and rotate them regularly
- Implement proper CORS configuration
- Validate all user inputs
- Use HTTPS in production

## Common Tasks

### Adding New Entity
1. Create model class with JPA annotations
2. Create repository interface
3. Create DTOs for request/response
4. Create service with business logic
5. Create controller with REST endpoints
6. Add OpenAPI annotations
7. Create migration scripts if needed

### Adding New API Endpoint
1. Define DTOs for request/response
2. Add method to appropriate service
3. Create controller endpoint with proper annotations
4. Add security annotations if needed
5. Document with OpenAPI annotations
6. Add tests for the new endpoint