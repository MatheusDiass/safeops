package com.bytepowerlabs.safeops_api.modules.identity.repository

import com.bytepowerlabs.safeops_api.modules.identity.entity.RefreshTokenEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.UUID

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByTokenHash(refreshToken: String): RefreshTokenEntity?
}