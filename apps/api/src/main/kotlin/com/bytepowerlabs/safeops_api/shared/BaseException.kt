package com.bytepowerlabs.safeops_api.shared

abstract class BaseException(val code: String, message: String, val title: String, val errorCategory: ErrorCategory) :
    RuntimeException(message) {
}