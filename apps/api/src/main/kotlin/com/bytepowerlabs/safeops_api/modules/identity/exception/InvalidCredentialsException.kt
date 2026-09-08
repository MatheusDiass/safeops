package com.bytepowerlabs.safeops_api.modules.identity.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class InvalidCredentialsException : BaseException(
    code = "INVALID_CREDENTIALS",
    title = "Invalid credentials",
    message = "The email or password is invalid.",
    errorCategory = ErrorCategory.UNAUTHORIZED
)
