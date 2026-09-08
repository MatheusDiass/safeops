package com.bytepowerlabs.safeops_api.modules.identity.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class InvalidPasswordException(message: String) : BaseException(
    code = "INVALID_PASSWORD",
    title = "Invalid password",
    message = message,
    errorCategory = ErrorCategory.INVALID_INPUT
)
