package com.bytepowerlabs.safeops_api.modules.organization.exception

class OrganizationAccessDeniedException : RuntimeException("You do not have permission to update this organization") {
}