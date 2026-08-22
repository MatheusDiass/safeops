package com.bytepowerlabs.safeops_api.modules.site.entity

import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
@Table(schema = "site", name = "site")
class SiteEntity (
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID = Uuid.generateV7().toJavaUuid(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false, updatable = false)
    var organization: OrganizationEntity,

    @Column(name = "name", nullable = false, length = 150)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    var status: SiteStatus = SiteStatus.ACTIVE,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant? = null,
)