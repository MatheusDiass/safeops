<script setup lang="ts">
import Tag from 'primevue/tag';
import { useI18n } from 'vue-i18n';
import type { IncidentStatus } from '../types/incident.types';

type Props = {
  status: IncidentStatus;
};

defineProps<Props>();

const { t } = useI18n();

function getStatusSeverity(status: IncidentStatus): 'info' | 'secondary' | 'warn' | 'success' {
  const severities: Record<IncidentStatus, 'info' | 'secondary' | 'warn' | 'success'> = {
    REPORTED: 'info',
    UNDER_REVIEW: 'secondary',
    ACTION_REQUIRED: 'warn',
    CLOSED: 'success',
  };

  return severities[status];
}
</script>

<template>
  <Tag
    :value="t(`incidents.status.${status}`)"
    :severity="getStatusSeverity(status)"
  />
</template>
