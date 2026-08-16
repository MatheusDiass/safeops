package com.bytepowerlabs.safeops_api.modules.identity.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

@Entity
@Table(schema = "identity", name = "refresh_token")
class RefreshTokenEntity(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID = Uuid.generateV7().toJavaUuid(),

    @Column(name = "token_hash", nullable = false, updatable = false)
    var tokenHash: String,

    @Column(name = "expires_at", nullable = false, updatable = false)
    var expiresAt: Instant,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_session_id", nullable = false, updatable = false)
    var authSession: AuthSessionEntity,
) {
    @Column(name = "used_at", nullable = true, updatable = true)
    var usedAt: Instant? = null
        protected set

    fun markAsUsed() {
        usedAt = Instant.now()
    }
}