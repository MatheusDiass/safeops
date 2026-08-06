package com.bytepowerlabs.safeops_api.modules.identity.repository

import com.bytepowerlabs.safeops_api.modules.identity.entity.UserAccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserAccountEntity, UUID> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): UserAccountEntity?
}