package com.secretj.pubg_bot.common.exception

import com.secretj.pubg_bot.common.enum.CommonErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

class BusinessException(val errorCode: CommonErrorCode) : RuntimeException(errorCode.message)

@RestControllerAdvice
class CommonExceptionHandler {
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(e.errorCode))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(CommonErrorCode.UNKNOWN_ERROR))
    }
}

data class ErrorResponse(
    val code: Int,
    val message: String
) {
    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(
                code = errorCode.code,
                message = errorCode.message
            )
        }
    }
}