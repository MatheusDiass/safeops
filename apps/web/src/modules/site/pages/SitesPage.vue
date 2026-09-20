<script setup lang="ts">
import { mdiMagnify, mdiMapMarkerPlusOutline, mdiPlus } from '@mdi/js';
import { storeToRefs } from 'pinia';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import ProgressSpinner from 'primevue/progressspinner';
import { computed, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { useOrganizationStore } from '../../organization/stores/organization.store';
import SiteCard from '../components/SiteCard.vue';
import { useSiteList } from '../composables/useSiteList';

const { t } = useI18n();
const router = useRouter();
const organizationStore = useOrganizationStore();
const { organizations, selectedOrganizationId } = storeToRefs(organizationStore);
const { errorMessage, isLoading, load, sites } = useSiteList();
const searchQuery = ref('');

const selectedOrganization = computed(() =>
  organizations.value.find(({ id }) => id === selectedOrganizationId.value),
);
const hasSites = computed(() => sites.value.length > 0);
const siteCountLabel = computed(() => {
  if (!selectedOrganization.value) {
    return t('sites.selectOrganization.description');
  }

  const count = sites.value.length;
  const key = count === 1 ? 'sites.summary.singular' : 'sites.summary.plural';

  return t(key, { count, organization: selectedOrganization.value.name });
});
const filteredSites = computed(() => {
  const query = searchQuery.value.trim().toLocaleLowerCase();

  if (!query) {
    return sites.value;
  }

  return sites.value.filter((site) => site.name.toLocaleLowerCase().includes(query));
});

watch(
  selectedOrganizationId,
  (organizationId) => {
    if (organizationId) {
      searchQuery.value = '';
      void load(organizationId);
    }
  },
  { immediate: true },
);

function openCreateSite(): void {
  void router.push({ name: 'create-site' });
}

function openEditSite(siteId: string): void {
  void router.push({ name: 'edit-site', params: { siteId } });
}
</script>

<template>
  <main class="sites-page">
    <header class="sites-page__header">
      <div>
        <p class="sites-page__eyebrow">{{ t('common.navigation.workspace') }}</p>
        <h1>{{ t('sites.title') }}</h1>
        <p class="sites-page__subtitle">{{ siteCountLabel }}</p>
      </div>

      <Button
        v-if="selectedOrganization && hasSites && !isLoading && !errorMessage"
        type="button"
        @click="openCreateSite"
      >
        <svg
          class="button-icon"
          viewBox="0 0 24 24"
          aria-hidden="true"
        >
          <path :d="mdiPlus" />
        </svg>
        <span>{{ t('sites.actions.new') }}</span>
      </Button>
    </header>

    <div
      v-if="isLoading"
      class="sites-page__loading"
      role="status"
      :aria-label="t('sites.loadingLabel')"
    >
      <ProgressSpinner
        class="sites-page__spinner"
        stroke-width="5"
      />
      <span>{{ t('sites.loading') }}</span>
    </div>

    <Message
      v-else-if="errorMessage"
      severity="error"
      class="sites-page__error"
    >
      <div class="sites-page__error-content">
        <span>{{ errorMessage }}</span>
      </div>
    </Message>

    <template v-else-if="selectedOrganization">
      <div
        v-if="hasSites"
        class="sites-search"
      >
        <svg
          viewBox="0 0 24 24"
          aria-hidden="true"
        >
          <path :d="mdiMagnify" />
        </svg>
        <InputText
          v-model="searchQuery"
          type="search"
          :placeholder="t('sites.search.placeholder')"
          :aria-label="t('sites.search.label')"
          fluid
        />
      </div>

      <section
        v-if="hasSites && filteredSites.length > 0"
        class="sites-grid"
        :aria-label="t('sites.aria.list')"
      >
        <SiteCard
          v-for="site in filteredSites"
          :key="site.id"
          :site="site"
          @edit="openEditSite(site.id)"
        />
      </section>

      <section
        v-else-if="hasSites"
        class="sites-no-results"
        aria-live="polite"
      >
        <h2>{{ t('sites.search.noResultsTitle') }}</h2>
        <p>{{ t('sites.search.noResultsDescription') }}</p>
      </section>

      <section
        v-else
        class="sites-empty-state"
      >
        <span
          class="sites-empty-state__icon"
          aria-hidden="true"
        >
          <svg viewBox="0 0 24 24">
            <path :d="mdiMapMarkerPlusOutline" />
          </svg>
        </span>
        <div>
          <h2>{{ t('sites.empty.title') }}</h2>
          <p>{{ t('sites.empty.description', { organization: selectedOrganization.name }) }}</p>
        </div>
        <Button
          type="button"
          @click="openCreateSite"
        >
          <svg
            class="button-icon"
            viewBox="0 0 24 24"
            aria-hidden="true"
          >
            <path :d="mdiPlus" />
          </svg>
          <span>{{ t('sites.actions.create') }}</span>
        </Button>
      </section>
    </template>

    <section
      v-else
      class="sites-empty-state"
    >
      <span
        class="sites-empty-state__icon"
        aria-hidden="true"
      >
        <svg viewBox="0 0 24 24">
          <path :d="mdiMapMarkerPlusOutline" />
        </svg>
      </span>
      <div>
        <h2>{{ t('sites.selectOrganization.title') }}</h2>
        <p>{{ t('sites.selectOrganization.description') }}</p>
      </div>
    </section>
  </main>
</template>

<style scoped lang="scss">
.sites-page {
  width: min(100%, 75rem);
  padding: 3rem clamp(1.25rem, 4vw, 4rem);
}

.sites-page__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 2rem;
}

.sites-page__header h1 {
  margin: 0;
}

.sites-page__eyebrow {
  margin: 0 0 0.5rem;
  color: var(--p-primary-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.sites-page__subtitle {
  margin: 0.75rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.button-icon {
  width: 1.125rem;
  height: 1.125rem;
  fill: currentcolor;
}

.sites-page__loading {
  display: flex;
  min-height: 14rem;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 0.875rem;
  color: var(--p-text-muted-color);
}

.sites-page__spinner {
  width: 2rem;
  height: 2rem;
}

.sites-page__error {
  margin-top: 2rem;
}

.sites-page__error-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.sites-search {
  position: relative;
  width: min(100%, 28rem);
  margin-top: 2rem;
}

.sites-search > svg {
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

.sites-search :deep(input) {
  padding-left: 2.6rem;
}

.sites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(min(100%, 18rem), 1fr));
  gap: 1.25rem;
  margin-top: 2rem;
}

.sites-no-results,
.sites-empty-state {
  border: 1px dashed var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  margin-top: 2rem;
  background: var(--p-content-background);
  text-align: center;
}

.sites-no-results {
  padding: 3rem 1.5rem;
}

.sites-empty-state {
  display: grid;
  justify-items: center;
  gap: 1.5rem;
  padding: clamp(2.5rem, 7vw, 5rem) 1.5rem;
}

.sites-empty-state__icon {
  display: grid;
  width: 4rem;
  height: 4rem;
  place-items: center;
  border-radius: 50%;
  background: var(--p-primary-50);
  color: var(--p-primary-700);
}

.sites-empty-state__icon svg {
  width: 2rem;
  height: 2rem;
  fill: currentcolor;
}

.sites-no-results h2,
.sites-empty-state h2 {
  margin: 0;
  font-size: 1.125rem;
}

.sites-no-results p,
.sites-empty-state p {
  max-width: 32rem;
  margin: 0.5rem auto 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

@media (max-width: 640px) {
  .sites-page {
    padding-top: 2rem;
  }

  .sites-page__header {
    display: grid;
    gap: 1.5rem;
  }

  .sites-page__header :deep(.p-button) {
    width: 100%;
  }

  .sites-page__error-content {
    align-items: stretch;
    flex-direction: column;
  }
}
</style>
