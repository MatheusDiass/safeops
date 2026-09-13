<script setup lang="ts">
import { mdiMagnify, mdiOfficeBuildingPlusOutline, mdiPlus } from '@mdi/js';
import { storeToRefs } from 'pinia';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import { computed, ref } from 'vue';
import OrganizationCard from '../components/OrganizationCard.vue';
import { useOrganizationStore } from '../stores/organization.store';

const organizationStore = useOrganizationStore();
const { organizations } = storeToRefs(organizationStore);
const searchQuery = ref('');

const hasOrganizations = computed(() => organizations.value.length > 0);
const organizationCountLabel = computed(() => {
  const count = organizations.value.length;
  return `You belong to ${count} ${count === 1 ? 'organization' : 'organizations'}.`;
});
const filteredOrganizations = computed(() => {
  const query = searchQuery.value.trim().toLocaleLowerCase();

  if (!query) {
    return organizations.value;
  }

  return organizations.value.filter((organization) =>
    organization.name.toLocaleLowerCase().includes(query),
  );
});
</script>

<template>
  <main class="organizations-page">
    <header class="organizations-page__header">
      <div>
        <p class="organizations-page__eyebrow">Workspace</p>
        <h1>Organizations</h1>
        <p class="organizations-page__subtitle">{{ organizationCountLabel }}</p>
      </div>

      <Button
        v-if="hasOrganizations"
        type="button"
      >
        <svg
          class="button-icon"
          viewBox="0 0 24 24"
          aria-hidden="true"
        >
          <path :d="mdiPlus" />
        </svg>
        <span>New organization</span>
      </Button>
    </header>

    <div
      v-if="hasOrganizations"
      class="organizations-search"
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
        placeholder="Search organizations"
        aria-label="Search organizations by name"
        fluid
      />
    </div>

    <section
      v-if="hasOrganizations && filteredOrganizations.length > 0"
      class="organizations-grid"
      aria-label="Organizations"
    >
      <OrganizationCard
        v-for="organization in filteredOrganizations"
        :key="organization.id"
        :organization="organization"
      />
    </section>

    <section
      v-else-if="hasOrganizations"
      class="organizations-no-results"
      aria-live="polite"
    >
      <h2>No organizations found</h2>
      <p>Try searching for a different organization name.</p>
    </section>

    <section
      v-else
      class="organizations-empty-state"
    >
      <span
        class="organizations-empty-state__icon"
        aria-hidden="true"
      >
        <svg viewBox="0 0 24 24">
          <path :d="mdiOfficeBuildingPlusOutline" />
        </svg>
      </span>
      <div>
        <h2>Create your first organization</h2>
        <p>Organizations bring your sites and safety operations together in one workspace.</p>
      </div>
      <Button type="button">
        <svg
          class="button-icon"
          viewBox="0 0 24 24"
          aria-hidden="true"
        >
          <path :d="mdiPlus" />
        </svg>
        <span>Create organization</span>
      </Button>
    </section>
  </main>
</template>

<style scoped lang="scss">
.organizations-page {
  width: min(100%, 75rem);
  padding: 3rem clamp(1.25rem, 4vw, 4rem);
}

.organizations-page__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 2rem;
}

.organizations-page__eyebrow {
  margin: 0 0 0.5rem;
  color: var(--p-primary-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.organizations-page h1 {
  margin: 0;
}

.organizations-page__subtitle {
  margin: 0.75rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.button-icon {
  width: 1.125rem;
  height: 1.125rem;
  fill: currentcolor;
}

.organizations-search {
  position: relative;
  width: min(100%, 28rem);
  margin-top: 2rem;
}

.organizations-search > svg {
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

.organizations-search :deep(input) {
  padding-left: 2.6rem;
}

.organizations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(min(100%, 18rem), 1fr));
  gap: 1.25rem;
  margin-top: 2rem;
}

.organizations-no-results,
.organizations-empty-state {
  border: 1px dashed var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  margin-top: 2rem;
  background: var(--p-content-background);
  text-align: center;
}

.organizations-no-results {
  padding: 3rem 1.5rem;
}

.organizations-empty-state {
  display: grid;
  justify-items: center;
  gap: 1.5rem;
  padding: clamp(2.5rem, 7vw, 5rem) 1.5rem;
}

.organizations-empty-state__icon {
  display: grid;
  width: 4rem;
  height: 4rem;
  place-items: center;
  border-radius: 50%;
  background: var(--p-primary-50);
  color: var(--p-primary-700);
}

.organizations-empty-state__icon svg {
  width: 2rem;
  height: 2rem;
  fill: currentcolor;
}

.organizations-no-results h2,
.organizations-empty-state h2 {
  margin: 0;
  font-size: 1.125rem;
}

.organizations-no-results p,
.organizations-empty-state p {
  max-width: 32rem;
  margin: 0.5rem auto 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

@media (max-width: 640px) {
  .organizations-page {
    padding-top: 2rem;
  }

  .organizations-page__header {
    display: grid;
    gap: 1.5rem;
  }

  .organizations-page__header :deep(.p-button) {
    width: 100%;
  }
}
</style>
