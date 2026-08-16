package com.bytepowerlabs.safeops_api.modules.identity.repository

import com.bytepowerlabs.safeops_api.modules.identity.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {
    fun findByTokenHash(refreshToken: String): RefreshTokenEntity?
}