<script setup lang="ts">
import {
  mdiAlertCircleOutline,
  mdiAlertOutline,
  mdiCheckCircleOutline,
  mdiClipboardAlertOutline,
  mdiFilterRemoveOutline,
  mdiMagnify,
  mdiPlus,
} from '@mdi/js';
import { storeToRefs } from 'pinia';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import ProgressSpinner from 'primevue/progressspinner';
import Select from 'primevue/select';
import { computed, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { useOrganizationStore } from '../../organization/stores/organization.store';
import IncidentCard from '../components/IncidentCard.vue';
import IncidentTable from '../components/IncidentTable.vue';
import { useIncidentList } from '../composables/useIncidentList';
import {
  INCIDENT_SEVERITIES,
  INCIDENT_STATUSES,
  INCIDENT_TYPES,
  type IncidentSeverity,
  type IncidentStatus,
  type IncidentType,
} from '../types/incident.types';

type FilterOption<T> = {
  label: string;
  value: T;
};

type SummaryCard = {
  labelKey: string;
  value: number;
  icon: string;
  tone: 'info' | 'warning' | 'danger' | 'success';
};

// TODO: Replace this isolated mock summary with data from the future summary API.
const MOCK_SUMMARY_CARDS: SummaryCard[] = [
  { labelKey: 'incidents.summary.reported', value: 12, icon: mdiAlertOutline, tone: 'info' },
  {
    labelKey: 'incidents.summary.underReview',
    value: 4,
    icon: mdiClipboardAlertOutline,
    tone: 'warning',
  },
  {
    labelKey: 'incidents.summary.actionRequired',
    value: 3,
    icon: mdiAlertCircleOutline,
    tone: 'danger',
  },
  {
    labelKey: 'incidents.summary.closed',
    value: 28,
    icon: mdiCheckCircleOutline,
    tone: 'success',
  },
];

const { locale, t } = useI18n();
const router = useRouter();
const organizationStore = useOrganizationStore();
const { organizations, selectedOrganizationId } = storeToRefs(organizationStore);
const { errorMessage, incidents, isLoading, load } = useIncidentList();

const searchQuery = ref('');
const selectedSeverity = ref<IncidentSeverity | null>(null);
const selectedStatus = ref<IncidentStatus | null>(null);
const selectedType = ref<IncidentType | null>(null);

const selectedOrganization = computed(() =>
  organizations.value.find(({ id }) => id === selectedOrganizationId.value),
);
const hasIncidents = computed(() => incidents.value.length > 0);
const severityOptions = computed<FilterOption<IncidentSeverity>[]>(() =>
  INCIDENT_SEVERITIES.map((value) => ({
    label: t(`incidents.severity.${value}`),
    value,
  })),
);
const statusOptions = computed<FilterOption<IncidentStatus>[]>(() =>
  INCIDENT_STATUSES.map((value) => ({
    label: t(`incidents.status.${value}`),
    value,
  })),
);
const typeOptions = computed<FilterOption<IncidentType>[]>(() =>
  INCIDENT_TYPES.map((value) => ({
    label: t(`incidents.type.${value}`),
    value,
  })),
);
const hasActiveFilters = computed(
  () =>
    searchQuery.value.trim().length > 0 ||
    selectedSeverity.value !== null ||
    selectedStatus.value !== null ||
    selectedType.value !== null,
);
const filteredIncidents = computed(() => {
  const query = searchQuery.value.trim().toLocaleLowerCase(locale.value);

  return incidents.value.filter((incident) => {
    const matchesQuery =
      !query ||
      [incident.title, incident.location, incident.site.name, incident.reportedBy.name].some(
        (value) => value?.toLocaleLowerCase(locale.value).includes(query),
      );
    const matchesSeverity = !selectedSeverity.value || incident.severity === selectedSeverity.value;
    const matchesStatus = !selectedStatus.value || incident.status === selectedStatus.value;
    const matchesType = !selectedType.value || incident.type === selectedType.value;

    return matchesQuery && matchesSeverity && matchesStatus && matchesType;
  });
});
const resultCountLabel = computed(() => {
  const count = filteredIncidents.value.length;
  const key = count === 1 ? 'incidents.results.singular' : 'incidents.results.plural';

  return t(key, { count });
});

watch(
  selectedOrganizationId,
  (organizationId) => {
    resetFilters();

    if (organizationId) {
      void load(organizationId);
    }
  },
  { immediate: true },
);

function resetFilters(): void {
  searchQuery.value = '';
  selectedSeverity.value = null;
  selectedStatus.value = null;
  selectedType.value = null;
}

function openReportIncident(): void {
  void router.push({ name: 'report-incident' });
}

function openIncident(incidentId: string): void {
  void router.push({ name: 'incident-details', params: { incidentId } });
}
</script>

<template>
  <main class="incidents-page">
    <header class="incidents-page__header">
      <div>
        <p class="incidents-page__eyebrow">{{ t('common.navigation.workspace') }}</p>
        <h1>{{ t('incidents.title') }}</h1>
        <p class="incidents-page__subtitle">{{ t('incidents.description') }}</p>
      </div>

      <Button
        v-if="selectedOrganization"
        type="button"
        @click="openReportIncident"
      >
        <svg
          class="button-icon"
          viewBox="0 0 24 24"
          aria-hidden="true"
        >
          <path :d="mdiPlus" />
        </svg>
        <span>{{ t('incidents.actions.report') }}</span>
      </Button>
    </header>

    <section
      v-if="selectedOrganization"
      class="incident-summary"
      :aria-label="t('incidents.summary.label')"
    >
      <article
        v-for="card in MOCK_SUMMARY_CARDS"
        :key="card.labelKey"
        class="summary-card"
      >
        <span
          class="summary-card__icon"
          :class="`summary-card__icon--${card.tone}`"
          aria-hidden="true"
        >
          <svg viewBox="0 0 24 24">
            <path :d="card.icon" />
          </svg>
        </span>
        <span>
          <strong>{{ card.value }}</strong>
          <small>{{ t(card.labelKey) }}</small>
        </span>
      </article>
    </section>

    <div
      v-if="isLoading"
      class="incidents-page__loading"
      role="status"
      :aria-label="t('incidents.loadingLabel')"
    >
      <ProgressSpinner
        class="incidents-page__spinner"
        stroke-width="5"
      />
      <span>{{ t('incidents.loading') }}</span>
    </div>

    <Message
      v-else-if="errorMessage"
      class="incidents-page__error"
      severity="error"
    >
      {{ errorMessage }}
    </Message>

    <template v-else-if="selectedOrganization">
      <section
        v-if="hasIncidents"
        class="incident-controls"
        :aria-label="t('incidents.filters.label')"
      >
        <div class="incident-search">
          <svg
            viewBox="0 0 24 24"
            aria-hidden="true"
          >
            <path :d="mdiMagnify" />
          </svg>
          <InputText
            v-model="searchQuery"
            type="search"
            :placeholder="t('incidents.search.placeholder')"
            :aria-label="t('incidents.search.label')"
            fluid
          />
        </div>

        <div class="incident-filter">
          <label for="incident-severity-filter">{{ t('incidents.filters.severity') }}</label>
          <Select
            v-model="selectedSeverity"
            input-id="incident-severity-filter"
            :options="severityOptions"
            option-label="label"
            option-value="value"
            :placeholder="t('incidents.filters.allSeverities')"
            fluid
          />
        </div>

        <div class="incident-filter">
          <label for="incident-status-filter">{{ t('incidents.filters.status') }}</label>
          <Select
            v-model="selectedStatus"
            input-id="incident-status-filter"
            :options="statusOptions"
            option-label="label"
            option-value="value"
            :placeholder="t('incidents.filters.allStatuses')"
            fluid
          />
        </div>

        <div class="incident-filter">
          <label for="incident-type-filter">{{ t('incidents.filters.type') }}</label>
          <Select
            v-model="selectedType"
            input-id="incident-type-filter"
            :options="typeOptions"
            option-label="label"
            option-value="value"
            :placeholder="t('incidents.filters.allTypes')"
            fluid
          />
        </div>

        <Button
          type="button"
          class="incident-controls__reset"
          severity="secondary"
          text
          :disabled="!hasActiveFilters"
          @click="resetFilters"
        >
          <svg
            class="button-icon"
            viewBox="0 0 24 24"
            aria-hidden="true"
          >
            <path :d="mdiFilterRemoveOutline" />
          </svg>
          <span>{{ t('incidents.actions.resetFilters') }}</span>
        </Button>
      </section>

      <div
        v-if="hasIncidents"
        class="incident-results"
        aria-live="polite"
      >
        <p>{{ resultCountLabel }}</p>

        <template v-if="filteredIncidents.length > 0">
          <div class="incident-results__desktop">
            <IncidentTable
              :incidents="filteredIncidents"
              @select="openIncident"
            />
          </div>

          <section
            class="incident-results__mobile"
            :aria-label="t('incidents.aria.list')"
          >
            <IncidentCard
              v-for="incident in filteredIncidents"
              :key="incident.id"
              :incident="incident"
              @select="openIncident(incident.id)"
            />
          </section>
        </template>

        <section
          v-else
          class="incidents-empty-state incidents-empty-state--compact"
        >
          <span
            class="incidents-empty-state__icon"
            aria-hidden="true"
          >
            <svg viewBox="0 0 24 24">
              <path :d="mdiMagnify" />
            </svg>
          </span>
          <div>
            <h2>{{ t('incidents.search.noResultsTitle') }}</h2>
            <p>{{ t('incidents.search.noResultsDescription') }}</p>
          </div>
          <Button
            type="button"
            severity="secondary"
            outlined
            :label="t('incidents.actions.resetFilters')"
            @click="resetFilters"
          />
        </section>
      </div>

      <section
        v-else
        class="incidents-empty-state"
      >
        <span
          class="incidents-empty-state__icon"
          aria-hidden="true"
        >
          <svg viewBox="0 0 24 24">
            <path :d="mdiAlertOutline" />
          </svg>
        </span>
        <div>
          <h2>{{ t('incidents.empty.title') }}</h2>
          <p>{{ t('incidents.empty.description', { organization: selectedOrganization.name }) }}</p>
        </div>
        <Button
          type="button"
          :label="t('incidents.actions.report')"
          @click="openReportIncident"
        />
      </section>
    </template>

    <section
      v-else
      class="incidents-empty-state"
    >
      <span
        class="incidents-empty-state__icon"
        aria-hidden="true"
      >
        <svg viewBox="0 0 24 24">
          <path :d="mdiAlertOutline" />
        </svg>
      </span>
      <div>
        <h2>{{ t('incidents.selectOrganization.title') }}</h2>
        <p>{{ t('incidents.selectOrganization.description') }}</p>
      </div>
    </section>
  </main>
</template>

<style scoped lang="scss">
.incidents-page {
  width: min(100%, 92rem);
  padding: 3rem clamp(1.25rem, 4vw, 4rem);
}

.incidents-page__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 2rem;
}

.incidents-page__header h1 {
  margin: 0;
}

.incidents-page__eyebrow {
  margin: 0 0 0.5rem;
  color: var(--p-primary-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.incidents-page__subtitle {
  max-width: 42rem;
  margin: 0.75rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.button-icon {
  width: 1.125rem;
  height: 1.125rem;
  fill: currentcolor;
}

.incident-summary {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 1rem;
  margin-top: 2rem;
}

.summary-card {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 0.875rem;
  padding: 1.125rem;
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  background: var(--p-content-background);
}

.summary-card__icon {
  display: grid;
  width: 2.75rem;
  height: 2.75rem;
  flex: 0 0 auto;
  place-items: center;
  border-radius: var(--p-border-radius-lg);
}

.summary-card__icon svg {
  width: 1.4rem;
  height: 1.4rem;
  fill: currentcolor;
}

.summary-card__icon--info {
  background: var(--p-blue-50);
  color: var(--p-blue-700);
}

.summary-card__icon--warning {
  background: var(--p-orange-50);
  color: var(--p-orange-700);
}

.summary-card__icon--danger {
  background: var(--p-red-50);
  color: var(--p-red-700);
}

.summary-card__icon--success {
  background: var(--p-green-50);
  color: var(--p-green-700);
}

.summary-card > span:last-child {
  display: grid;
  min-width: 0;
  gap: 0.2rem;
}

.summary-card strong {
  font-size: 1.35rem;
  line-height: 1;
}

.summary-card small {
  color: var(--p-text-muted-color);
  font-size: 0.75rem;
  line-height: 1.35;
}

.incidents-page__loading {
  display: flex;
  min-height: 16rem;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.875rem;
  color: var(--p-text-muted-color);
  text-align: center;
}

.incidents-page__spinner {
  width: 2rem;
  height: 2rem;
}

.incidents-page__error {
  margin-top: 2rem;
}

.incident-controls {
  display: grid;
  grid-template-columns: minmax(14rem, 2fr) repeat(3, minmax(9rem, 1fr)) auto;
  align-items: end;
  gap: 0.875rem;
  margin-top: 2rem;
}

.incident-search {
  position: relative;
}

.incident-search > svg {
  position: absolute;
  z-index: 1;
  top: 50%;
  left: 0.875rem;
  width: 1.125rem;
  height: 1.125rem;
  fill: var(--p-text-muted-color);
  pointer-events: none;
  transform: translateY(-50%);
}

.incident-search :deep(input) {
  padding-left: 2.6rem;
}

.incident-filter {
  display: grid;
  gap: 0.4rem;
}

.incident-filter label {
  color: var(--p-text-muted-color);
  font-size: 0.7rem;
  font-weight: 650;
  letter-spacing: 0.04em;
  text-transform: uppercase;
}

.incident-results {
  margin-top: 1.5rem;
}

.incident-results > p {
  margin: 0 0 0.75rem;
  color: var(--p-text-muted-color);
  font-size: 0.85rem;
}

.incident-results__mobile {
  display: none;
}

.incidents-empty-state {
  display: grid;
  justify-items: center;
  gap: 1.5rem;
  padding: clamp(2.5rem, 7vw, 5rem) 1.5rem;
  border: 1px dashed var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  margin-top: 2rem;
  background: var(--p-content-background);
  text-align: center;
}

.incidents-empty-state--compact {
  margin-top: 1rem;
  padding-block: 3rem;
}

.incidents-empty-state__icon {
  display: grid;
  width: 4rem;
  height: 4rem;
  place-items: center;
  border-radius: 50%;
  background: var(--p-primary-50);
  color: var(--p-primary-700);
}

.incidents-empty-state__icon svg {
  width: 2rem;
  height: 2rem;
  fill: currentcolor;
}

.incidents-empty-state h2 {
  margin: 0;
  font-size: 1.125rem;
}

.incidents-empty-state p {
  max-width: 32rem;
  margin: 0.5rem auto 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

@media (max-width: 1180px) {
  .incident-summary {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .incident-controls {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .incident-search {
    grid-column: span 2;
  }
}

@media (max-width: 760px) {
  .incidents-page {
    padding-top: 2rem;
  }

  .incidents-page__header {
    display: grid;
    gap: 1.5rem;
  }

  .incidents-page__header :deep(.p-button) {
    width: 100%;
  }

  .incident-controls {
    grid-template-columns: 1fr;
  }

  .incident-search {
    grid-column: auto;
  }

  .incident-controls__reset {
    justify-self: start;
  }

  .incident-results__desktop {
    display: none;
  }

  .incident-results__mobile {
    display: grid;
    gap: 1rem;
  }
}

@media (max-width: 480px) {
  .incident-summary {
    grid-template-columns: 1fr;
  }
}
</style>
