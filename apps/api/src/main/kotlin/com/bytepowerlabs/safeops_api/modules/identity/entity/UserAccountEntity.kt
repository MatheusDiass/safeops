package com.bytepowerlabs.safeops_api.modules.identity.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

@Entity
@Table(schema = "identity", name = "user_account")
class UserAccountEntity(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private var id: UUID = Uuid.generateV7().toJavaUuid(),

    @Column(name = "name", nullable = false, length = 100)
    var name: String,

    @Column(name = "email", nullable = false, length = 100, unique = true)
    var email: String,

    @Column(name = "password_hash", nullable = false, length = 255)
    var passwordHash: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    var status: AccountStatus = AccountStatus.ACTIVE,

    @Column(name = "password_changed_at", nullable = true)
    var passwordChangedAt: Instant? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null,
)