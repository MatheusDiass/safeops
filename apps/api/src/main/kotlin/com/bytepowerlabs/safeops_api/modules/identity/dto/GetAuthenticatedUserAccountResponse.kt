package com.bytepowerlabs.safeops_api.modules.identity.dto

import java.util.UUID

data class GetAuthenticatedUserAccountResponse(
    val id: UUID,
    val name: String,
    val email: String
)
