<script setup lang="ts">
import { mdiCalendarOutline, mdiMapMarkerOutline } from '@mdi/js';
import { useI18n } from 'vue-i18n';
import type { Incident } from '../types/incident.types';
import IncidentSeverityBadge from './IncidentSeverityBadge.vue';
import IncidentStatusBadge from './IncidentStatusBadge.vue';

type Props = {
  incident: Incident;
};

defineProps<Props>();

const emit = defineEmits<{
  select: [];
}>();

const { locale, t } = useI18n();

function formatDate(value: string): string {
  return new Intl.DateTimeFormat(locale.value, { dateStyle: 'medium' }).format(new Date(value));
}
</script>

<template>
  <article class="incident-card">
    <button
      class="incident-card__button"
      type="button"
      :aria-label="t('incidents.aria.open', { title: incident.title })"
      @click="emit('select')"
    >
      <span class="incident-card__header">
        <span>
          <strong>{{ incident.title }}</strong>
          <span
            v-if="incident.location"
            class="incident-card__location"
          >
            <svg
              viewBox="0 0 24 24"
              aria-hidden="true"
            >
              <path :d="mdiMapMarkerOutline" />
            </svg>
            {{ incident.location }}
          </span>
        </span>
        <IncidentSeverityBadge :severity="incident.severity" />
      </span>

      <span class="incident-card__badges">
        <IncidentStatusBadge :status="incident.status" />
        <span class="incident-card__type">{{ t(`incidents.type.${incident.type}`) }}</span>
      </span>

      <span class="incident-card__metadata">
        <span>
          <small>{{ t('incidents.columns.site') }}</small>
          {{ incident.site.name }}
        </span>
        <span>
          <small>{{ t('incidents.columns.reporter') }}</small>
          {{ incident.reportedBy.name }}
        </span>
        <span>
          <small>{{ t('incidents.columns.occurredAt') }}</small>
          <span class="incident-card__date">
            <svg
              viewBox="0 0 24 24"
              aria-hidden="true"
            >
              <path :d="mdiCalendarOutline" />
            </svg>
            {{ formatDate(incident.occurredAt) }}
          </span>
        </span>
      </span>
    </button>
  </article>
</template>

<style scoped lang="scss">
.incident-card {
  overflow: hidden;
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  background: var(--p-content-background);
}

.incident-card__button {
  display: grid;
  width: 100%;
  gap: 1rem;
  padding: 1.25rem;
  border: 0;
  background: transparent;
  color: var(--p-text-color);
  cursor: pointer;
  font: inherit;
  text-align: left;
}

.incident-card__button:hover {
  background: var(--p-surface-50);
}

.incident-card__button:focus-visible {
  outline: 2px solid var(--p-primary-color);
  outline-offset: -2px;
}

.incident-card__header {
  display: flex;
  min-width: 0;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1rem;
}

.incident-card__header > span:first-child {
  display: grid;
  min-width: 0;
  gap: 0.4rem;
}

.incident-card__header strong {
  overflow-wrap: anywhere;
  font-size: 1rem;
  line-height: 1.4;
}

.incident-card__location,
.incident-card__date {
  display: flex;
  align-items: center;
  gap: 0.35rem;
}

.incident-card__location {
  color: var(--p-text-muted-color);
  font-size: 0.8rem;
}

.incident-card__location svg,
.incident-card__date svg {
  width: 1rem;
  height: 1rem;
  flex: 0 0 auto;
  fill: currentcolor;
}

.incident-card__badges {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.5rem;
}

.incident-card__type {
  color: var(--p-text-muted-color);
  font-size: 0.8rem;
}

.incident-card__metadata {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0.875rem;
  padding-top: 1rem;
  border-top: 1px solid var(--p-content-border-color);
  font-size: 0.85rem;
}

.incident-card__metadata > span {
  display: grid;
  min-width: 0;
  gap: 0.25rem;
  overflow-wrap: anywhere;
}

.incident-card__metadata > span:last-child {
  grid-column: 1 / -1;
}

.incident-card__metadata small {
  color: var(--p-text-muted-color);
  font-size: 0.7rem;
  font-weight: 650;
  letter-spacing: 0.04em;
  text-transform: uppercase;
}
</style>
