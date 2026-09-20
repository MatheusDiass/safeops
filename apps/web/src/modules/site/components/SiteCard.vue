<script setup lang="ts">
import { mdiCalendarOutline, mdiMapMarkerOutline } from '@mdi/js';
import Tag from 'primevue/tag';
import { useI18n } from 'vue-i18n';
import type { Site, SiteStatus } from '../types/site.types';

type Props = {
  site: Site;
};

defineProps<Props>();
const { locale, t } = useI18n();

function getStatusSeverity(status: SiteStatus): 'success' | 'secondary' {
  return status === 'ACTIVE' ? 'success' : 'secondary';
}

function formatDate(value: string): string {
  return new Intl.DateTimeFormat(locale.value, { dateStyle: 'medium' }).format(new Date(value));
}
</script>

<template>
  <article class="site-card">
    <div class="site-card__header">
      <div class="site-card__identity">
        <span
          class="site-card__icon"
          aria-hidden="true"
        >
          <svg viewBox="0 0 24 24">
            <path :d="mdiMapMarkerOutline" />
          </svg>
        </span>

        <h2>{{ site.name }}</h2>
      </div>

      <Tag
        :value="t(`sites.status.${site.status}`)"
        :severity="getStatusSeverity(site.status)"
      />
    </div>

    <div class="site-card__metadata">
      <svg
        viewBox="0 0 24 24"
        aria-hidden="true"
      >
        <path :d="mdiCalendarOutline" />
      </svg>
      <span>{{ t('sites.createdAt', { date: formatDate(site.createdAt) }) }}</span>
    </div>
  </article>
</template>

<style scoped lang="scss">
.site-card {
  display: grid;
  min-width: 0;
  gap: 1rem;
  padding: 1.5rem;
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  background: var(--p-content-background);
}

.site-card__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1rem;
}

.site-card__identity {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 0.875rem;
}

.site-card__icon {
  display: grid;
  width: 2.75rem;
  height: 2.75rem;
  flex: 0 0 auto;
  place-items: center;
  border-radius: var(--p-border-radius-lg);
  background: var(--p-primary-50);
  color: var(--p-primary-700);
}

.site-card__icon svg {
  width: 1.4rem;
  height: 1.4rem;
  fill: currentcolor;
}

.site-card h2 {
  overflow-wrap: anywhere;
  margin: 0;
  color: var(--p-text-color);
  font-size: 1.125rem;
  line-height: 1.35;
}

.site-card__metadata {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding-top: 1rem;
  border-top: 1px solid var(--p-content-border-color);
  color: var(--p-text-muted-color);
  font-size: 0.8rem;
}

.site-card__metadata svg {
  width: 1rem;
  height: 1rem;
  flex: 0 0 auto;
  fill: currentcolor;
}
</style>
