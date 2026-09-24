<script setup lang="ts">
import Column from 'primevue/column';
import DataTable, { type DataTableRowClickEvent } from 'primevue/datatable';
import { useI18n } from 'vue-i18n';
import type { Incident } from '../types/incident.types';
import IncidentSeverityBadge from './IncidentSeverityBadge.vue';
import IncidentStatusBadge from './IncidentStatusBadge.vue';

type Props = {
  incidents: Incident[];
};

defineProps<Props>();

const emit = defineEmits<{
  select: [incidentId: string];
}>();

const { locale, t } = useI18n();

function formatDate(value: string): string {
  return new Intl.DateTimeFormat(locale.value, { dateStyle: 'medium' }).format(new Date(value));
}

function handleRowClick(event: DataTableRowClickEvent<Incident>): void {
  emit('select', event.data.id);
}
</script>

<template>
  <DataTable
    :value="incidents"
    data-key="id"
    class="incident-table"
    striped-rows
    @row-click="handleRowClick"
  >
    <Column :header="t('incidents.columns.incident')">
      <template #body="{ data }: { data: Incident }">
        <button
          class="incident-table__title"
          type="button"
          :aria-label="t('incidents.aria.open', { title: data.title })"
          @click.stop="emit('select', data.id)"
        >
          {{ data.title }}
        </button>
        <span
          v-if="data.location"
          class="incident-table__secondary"
        >
          {{ data.location }}
        </span>
      </template>
    </Column>
    <Column :header="t('incidents.columns.severity')">
      <template #body="{ data }: { data: Incident }">
        <IncidentSeverityBadge :severity="data.severity" />
      </template>
    </Column>
    <Column :header="t('incidents.columns.status')">
      <template #body="{ data }: { data: Incident }">
        <IncidentStatusBadge :status="data.status" />
      </template>
    </Column>
    <Column :header="t('incidents.columns.type')">
      <template #body="{ data }: { data: Incident }">
        {{ t(`incidents.type.${data.type}`) }}
      </template>
    </Column>
    <Column
      field="site.name"
      :header="t('incidents.columns.site')"
    />
    <Column :header="t('incidents.columns.occurredAt')">
      <template #body="{ data }: { data: Incident }">
        {{ formatDate(data.occurredAt) }}
      </template>
    </Column>
    <Column
      field="reportedBy.name"
      :header="t('incidents.columns.reporter')"
    />
  </DataTable>
</template>

<style scoped lang="scss">
.incident-table {
  overflow: hidden;
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
}

.incident-table :deep(tbody > tr) {
  cursor: pointer;
}

.incident-table__title {
  display: block;
  padding: 0;
  border: 0;
  background: transparent;
  color: var(--p-text-color);
  cursor: pointer;
  font: inherit;
  font-weight: 650;
  text-align: left;
}

.incident-table__title:hover {
  color: var(--p-primary-color);
  text-decoration: underline;
}

.incident-table__title:focus-visible {
  border-radius: var(--p-border-radius-sm);
  outline: 2px solid var(--p-primary-color);
  outline-offset: 3px;
}

.incident-table__secondary {
  display: block;
  margin-top: 0.3rem;
  color: var(--p-text-muted-color);
  font-size: 0.8rem;
}
</style>
