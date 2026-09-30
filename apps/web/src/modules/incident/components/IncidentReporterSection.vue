<script setup lang="ts">
import { mdiAccountCircleOutline } from '@mdi/js';
import { useI18n } from 'vue-i18n';

type Props = {
  reporterName?: string;
};

defineProps<Props>();
const { t } = useI18n();
</script>

<template>
  <section class="form-card reporter-card">
    <header class="form-card__header">
      <h2>{{ t('incidents.create.sections.reporter') }}</h2>
    </header>
    <div class="reporter-metadata">
      <span aria-hidden="true">
        <svg viewBox="0 0 24 24">
          <path :d="mdiAccountCircleOutline" />
        </svg>
      </span>
      <div>
        <small>{{ t('incidents.create.reporter.label') }}</small>
        <strong>{{ reporterName }}</strong>
      </div>
    </div>
  </section>
</template>

<style scoped lang="scss">
.form-card {
  padding: clamp(1.25rem, 3vw, 2rem);
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  background: var(--p-content-background);
  box-shadow: 0 1px 3px color-mix(in srgb, var(--p-surface-900) 7%, transparent);
}

.form-card__header {
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--p-content-border-color);
  margin-bottom: 1rem;
}

.form-card__header h2 {
  margin: 0;
  font-size: 1.05rem;
  letter-spacing: -0.015em;
}

.reporter-metadata {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.reporter-metadata > span {
  display: grid;
  width: 2.75rem;
  height: 2.75rem;
  place-items: center;
  border-radius: 50%;
  background: var(--p-primary-50);
  color: var(--p-primary-700);
}

.reporter-metadata svg {
  width: 1.5rem;
  height: 1.5rem;
  fill: currentcolor;
}

.reporter-metadata div {
  display: grid;
  gap: 0.15rem;
}

.reporter-metadata small {
  color: var(--p-text-muted-color);
}

.reporter-metadata strong {
  font-size: 0.925rem;
}
</style>
