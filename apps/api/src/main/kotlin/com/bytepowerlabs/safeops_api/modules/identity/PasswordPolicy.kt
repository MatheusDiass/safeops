package com.bytepowerlabs.safeops_api.modules.identity

import com.bytepowerlabs.safeops_api.modules.identity.exception.InvalidPasswordException
import org.springframework.stereotype.Component

@Component
class PasswordPolicy {
    fun validate(password: String) {
        val length = password.codePointCount(0, password.length)

        if (length < MIN_LENGTH) {
            throw InvalidPasswordException("Password must contain at least $MIN_LENGTH characters")
        }

        if (length > MAX_LENGTH) {
            throw InvalidPasswordException("Password must contain at most $MAX_LENGTH characters")
        }
    }

    private companion object {
        const val MIN_LENGTH = 15
        const val MAX_LENGTH = 128
    }
}