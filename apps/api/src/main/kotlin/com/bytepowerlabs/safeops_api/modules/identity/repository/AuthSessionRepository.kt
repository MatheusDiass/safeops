package com.bytepowerlabs.safeops_api.modules.identity.repository

import com.bytepowerlabs.safeops_api.modules.identity.entity.AuthSessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AuthSessionRepository : JpaRepository<AuthSessionEntity, UUID> {
}