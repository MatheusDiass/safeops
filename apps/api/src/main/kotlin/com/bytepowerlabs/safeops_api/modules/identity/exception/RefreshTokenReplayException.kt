package com.bytepowerlabs.safeops_api.modules.identity.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class RefreshTokenReplayException : BaseException(
    code = "REFRESH_TOKEN_REPLAY",
    title = "Refresh token replay detected",
    message = "The refresh token has already been used.",
    errorCategory = ErrorCategory.UNAUTHORIZED
)
