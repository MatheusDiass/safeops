package com.bytepowerlabs.safeops_api.modules.organization.entity

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
@Table(schema = "organization", name = "organization")
class OrganizationEntity(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID = Uuid.generateV7().toJavaUuid(),

    @Column(name = "name", nullable = false, length = 150)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    var status: OrganizationStatus = OrganizationStatus.ACTIVE,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant? = null,
) {
    fun update(name: String?, status: OrganizationStatus?) {
        name?.let { this.name = name }
        status?.let { this.status = status }
        updatedAt = Instant.now()
    }
}