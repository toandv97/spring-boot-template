package com.example.demo.exception

class BadRequestException(message: String) : RuntimeException(message)

class ForbiddenException(message: String) : RuntimeException(message)

class NotFoundException(message: String) : RuntimeException(message)

class ConflictException(message: String) : RuntimeException(message)

class InternalServerException(message: String) : RuntimeException(message)

class ValidationException(message: String) : RuntimeException(message)

class UnauthorizedException(message: String?) : RuntimeException(message)

class AuthenticationException(message: String?) : RuntimeException(message)

class AuthorizationException(message: String?) : RuntimeException(message)

class MethodNotAllowedException(message: String) : RuntimeException(message)

class TooManyRequestsException(message: String) : RuntimeException(message)

class ServiceUnavailableException(message: String) : RuntimeException(message)
