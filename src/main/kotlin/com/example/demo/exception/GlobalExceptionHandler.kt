// GlobalExceptionHandler.kt
package com.example.demo.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.servlet.resource.NoResourceFoundException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.security.access.AccessDeniedException

import jakarta.servlet.http.HttpServletRequest

import com.example.demo.exception.*


@RestControllerAdvice // ← Đây là chìa khóa! Kết hợp @ControllerAdvice + @ResponseBody
class GlobalExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val message = "Content type '${ex.contentType}' is not supported. Supported types: ${ex.supportedMediaTypes.joinToString(", ")}"
        val error = ErrorResponse(
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
            error = "Unsupported Media Type",
            message = message
        )
        return ResponseEntity(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }

    // ✅ Bắt khi request body bị thiếu hoặc JSON sai format
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {

        val message = when {
            ex.message?.contains("Required request body is missing") == true ->
                "Request body is required but was not provided"

            ex.message?.contains("JSON parse error") == true ->
                "JSON is not valid, please check the format"

            else -> "The data sent could not be read, please check your request"
        }

        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = message
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    // Xử lý: dữ liệu không hợp lệ (400)
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(
        ex: BadRequestException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),     // 400
            error = "Bad Request",
            message = ex.message ?: "Invalid request"
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    // Xử lý: dữ liệu không hợp lệ (400)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(
        ex: NotFoundException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),        // 404
            error = "Not Found",
            message = ex.message ?: "Resource not found"
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    // Xử lý: lỗi validation từ @Valid (400)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {
        // Lấy tất cả các field bị lỗi và gộp lại
        val errors = ex.bindingResult.fieldErrors
            .joinToString("; ") { "${it.field}: ${it.defaultMessage}" }

        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            message = errors
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    // Xử lý: conflict (409) - ví dụ email đã tồn tại
    @ExceptionHandler(ConflictException::class)
    fun handleConflict(
        ex: ConflictException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),        // 409
            error = "Conflict",
            message = ex.message ?: "Resource already exists"
        )
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    // Xử lý: chưa xác thực (401)
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(
        ex: UnauthorizedException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.UNAUTHORIZED.value(),    // 401
            error = "Unauthorized",
            message = ex.message ?: "Authentication required"
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    // Xử lý: lỗi validation tùy chỉnh (400)
    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(
        ex: ValidationException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),     // 400
            error = "Validation Error",
            message = ex.message ?: "Validation failed"
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    // Xử lý: lỗi xác thực (401)
    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        ex: AuthenticationException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.UNAUTHORIZED.value(),    // 401
            error = "Authentication Failed",
            message = ex.message ?: "Authentication failed"
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    // Xử lý: lỗi phân quyền (403)
    @ExceptionHandler(AuthorizationException::class)
    fun handleAuthorizationException(
        ex: AuthorizationException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            error = "Authorization Failed",
            message = ex.message ?: "You do not have permission to perform this action"
        )
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }

    // Xử lý: method không được hỗ trợ (405)
    @ExceptionHandler(MethodNotAllowedException::class)
    fun handleMethodNotAllowed(
        ex: MethodNotAllowedException,
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.METHOD_NOT_ALLOWED.value(),  // 405
            error = "Method Not Allowed",
            message = ex.message ?: "HTTP method not supported for this endpoint"
        )
        return ResponseEntity(error, HttpStatus.METHOD_NOT_ALLOWED)
    }

    // Xử lý: Spring HttpRequestMethodNotSupportedException (405)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.METHOD_NOT_ALLOWED.value(),  // 405
            error = "Method Not Allowed",
            message = "Method '${ex.method}' is not supported for this endpoint"
        )
        return ResponseEntity(error, HttpStatus.METHOD_NOT_ALLOWED)
    }

    // Xử lý: quá nhiều request (429)
    @ExceptionHandler(TooManyRequestsException::class)
    fun handleTooManyRequests(
        ex: TooManyRequestsException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.TOO_MANY_REQUESTS.value(),   // 429
            error = "Too Many Requests",
            message = ex.message ?: "Rate limit exceeded, please try again later"
        )
        return ResponseEntity(error, HttpStatus.TOO_MANY_REQUESTS)
    }

    // Xử lý: service không khả dụng (503)
    @ExceptionHandler(ServiceUnavailableException::class)
    fun handleServiceUnavailable(
        ex: ServiceUnavailableException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.SERVICE_UNAVAILABLE.value(),   // 503
            error = "Service Unavailable",
            message = ex.message ?: "Service is temporarily unavailable"
        )
        return ResponseEntity(error, HttpStatus.SERVICE_UNAVAILABLE)
    }

    // Xử lý: Spring AccessDeniedException (403)
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(
        ex: AccessDeniedException
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            error = "Access Denied",
            message = ex.message ?: "You do not have permission to access this resource"
        )
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }

    // ✅ Bắt khi URL không tồn tại
    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Not Found",
            message = "URL not found"
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    // Xử lý: mọi lỗi còn lại không lường trước (500)
    @ExceptionHandler(Exception::class)
    fun handleGeneral(): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),  // 500
            error = "Internal Server Error",
            message = "Something went wrong, please try again later"
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
