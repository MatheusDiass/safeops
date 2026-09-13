<script setup lang="ts">
import { mdiMapMarkerMultipleOutline } from '@mdi/js';
import Tag from 'primevue/tag';
import type { Organization, OrganizationStatus } from '../types/organization.types';

type Props = {
  organization: Organization;
};

defineProps<Props>();

function getInitials(name: string): string {
  return name
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((word) => word.charAt(0).toUpperCase())
    .join('');
}

function getStatusLabel(status: OrganizationStatus): string {
  return status === 'ACTIVE' ? 'Active' : 'Disabled';
}

function getStatusSeverity(status: OrganizationStatus): 'success' | 'secondary' {
  return status === 'ACTIVE' ? 'success' : 'secondary';
}

function getSiteCountLabel(siteCount: number): string {
  return `${siteCount} ${siteCount === 1 ? 'site' : 'sites'}`;
}
</script>

<template>
  <article class="organization-card">
    <div class="organization-card__header">
      <div class="organization-card__identity">
        <span
          class="organization-card__initials"
          aria-hidden="true"
        >
          {{ getInitials(organization.name) }}
        </span>
        <h2>{{ organization.name }}</h2>
      </div>
      <Tag
        :value="getStatusLabel(organization.status)"
        :severity="getStatusSeverity(organization.status)"
      />
    </div>

    <div class="organization-card__sites">
      <svg
        viewBox="0 0 24 24"
        aria-hidden="true"
      >
        <path :d="mdiMapMarkerMultipleOutline" />
      </svg>
      <span>{{ getSiteCountLabel(organization.siteCount) }}</span>
    </div>
  </article>
</template>

<style scoped lang="scss">
.organization-card {
  display: grid;
  min-width: 0;
  gap: 1.25rem;
  padding: 1.5rem;
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  background: var(--p-content-background);
}

.organization-card__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1rem;
}

.organization-card__identity {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 0.875rem;
}

.organization-card__initials {
  display: grid;
  width: 3rem;
  height: 3rem;
  flex: 0 0 auto;
  place-items: center;
  border-radius: var(--p-border-radius-lg);
  background: var(--p-primary-50);
  color: var(--p-primary-700);
  font-size: 0.875rem;
  font-weight: 700;
  letter-spacing: 0.04em;
}

.organization-card h2 {
  overflow-wrap: anywhere;
  margin: 0;
  color: var(--p-text-color);
  font-size: 1.125rem;
  line-height: 1.35;
}

.organization-card__sites {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding-top: 1rem;
  border-top: 1px solid var(--p-content-border-color);
  color: var(--p-text-muted-color);
  font-size: 0.875rem;
}

.organization-card__sites svg {
  width: 1.125rem;
  height: 1.125rem;
  flex: 0 0 auto;
  fill: currentcolor;
}
</style>
