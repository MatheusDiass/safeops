package com.bytepowerlabs.safeops_api.shared

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ProblemDetail {
        val httpStatus = getHttpStatus(e.errorCategory)

        val errorType = e.code
            .lowercase()
            .replace("_", "-")

        return ProblemDetail.forStatusAndDetail(httpStatus, e.message).apply {
            title = e.title
            type = URI.create("urn:safeops:problem:$errorType")
            setProperty("code", e.code)
        }
    }

    private fun getHttpStatus(errorCategory: ErrorCategory): HttpStatus {
        return when (errorCategory) {
            ErrorCategory.RESOURCE_NOT_FOUND -> HttpStatus.NOT_FOUND
            ErrorCategory.ACCESS_DENIED -> HttpStatus.FORBIDDEN
            ErrorCategory.CONFLICT -> HttpStatus.CONFLICT
            ErrorCategory.INVALID_INPUT -> HttpStatus.BAD_REQUEST
            ErrorCategory.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
        }
    }
}