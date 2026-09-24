<script setup lang="ts">
import Tag from 'primevue/tag';
import { useI18n } from 'vue-i18n';
import type { IncidentSeverity } from '../types/incident.types';

type Props = {
  severity: IncidentSeverity | null;
};

defineProps<Props>();

const { t } = useI18n();

function getSeverity(severity: IncidentSeverity): 'success' | 'warn' | 'danger' {
  const severities: Record<IncidentSeverity, 'success' | 'warn' | 'danger'> = {
    LOW: 'success',
    MEDIUM: 'warn',
    HIGH: 'danger',
    CRITICAL: 'danger',
  };

  return severities[severity];
}
</script>

<template>
  <Tag
    v-if="severity"
    :value="t(`incidents.severity.${severity}`)"
    :severity="getSeverity(severity)"
  />
  <span
    v-else
    class="incident-severity-badge__empty"
  >
    {{ t('incidents.severity.notAssigned') }}
  </span>
</template>

<style scoped lang="scss">
.incident-severity-badge__empty {
  color: var(--p-text-muted-color);
  font-size: 0.8rem;
}
</style>
