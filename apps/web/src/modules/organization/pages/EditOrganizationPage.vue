<script setup lang="ts">
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import ProgressSpinner from 'primevue/progressspinner';
import Select from 'primevue/select';
import { computed, ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useOrganizationStore } from '../stores/organization.store';
import type { OrganizationStatus } from '../types/organization.types';

type StatusOption = {
  label: string;
  value: OrganizationStatus;
};

const statusOptions: StatusOption[] = [
  { label: 'Active', value: 'ACTIVE' },
  { label: 'Disabled', value: 'DISABLED' },
];

const route = useRoute();
const router = useRouter();
const organizationStore = useOrganizationStore();
const organizationName = ref('');
const organizationStatus = ref<OrganizationStatus>('ACTIVE');
const hasSubmitted = ref(false);
const isLoading = ref(true);
const isSubmitting = ref(false);
const loadErrorMessage = ref<string>();
const submitErrorMessage = ref<string>();

const organizationId = computed(() => {
  const routeId = route.params.organizationId;

  return Array.isArray(routeId) ? routeId[0] : routeId;
});
const normalizedOrganizationName = computed(() => organizationName.value.trim());
const organizationNameError = computed(() => {
  const nameLength = normalizedOrganizationName.value.length;

  if (nameLength === 0) {
    return 'Organization name is required.';
  }

  if (nameLength < 3 || nameLength > 150) {
    return 'Organization name must be between 3 and 150 characters.';
  }

  return undefined;
});

function getErrorMessage(error: unknown, fallback: string): string {
  return error instanceof Error ? error.message : fallback;
}

async function loadOrganization(): Promise<void> {
  if (!organizationId.value) {
    loadErrorMessage.value = 'Organization not found.';
    isLoading.value = false;
    return;
  }

  isLoading.value = true;
  loadErrorMessage.value = undefined;

  try {
    const organization = await organizationStore.loadOrganization(organizationId.value);
    organizationName.value = organization.name;
    organizationStatus.value = organization.status;
  } catch (error: unknown) {
    loadErrorMessage.value = getErrorMessage(
      error,
      'Unable to load the organization. Please try again.',
    );
  } finally {
    isLoading.value = false;
  }
}

function cancel(): void {
  void router.push({ name: 'organization' });
}

async function submit(): Promise<void> {
  hasSubmitted.value = true;
  submitErrorMessage.value = undefined;

  if (!organizationId.value || organizationNameError.value) {
    return;
  }

  isSubmitting.value = true;

  try {
    await organizationStore.updateOrganization(organizationId.value, {
      name: normalizedOrganizationName.value,
      status: organizationStatus.value,
    });
    await router.replace({ name: 'organization' });
  } catch (error: unknown) {
    submitErrorMessage.value = getErrorMessage(
      error,
      'Unable to update the organization. Please try again.',
    );
  } finally {
    isSubmitting.value = false;
  }
}

function handleSubmit(): void {
  void submit();
}

onMounted(() => {
  void loadOrganization();
});
</script>

<template>
  <main class="edit-organization-page">
    <header>
      <p class="edit-organization-page__eyebrow">Organizations</p>
      <h1>Edit organization</h1>
      <p class="edit-organization-page__description">
        Update the organization name and availability across your workspace.
      </p>
    </header>

    <div
      v-if="isLoading"
      class="edit-organization-page__loading"
      role="status"
      aria-label="Loading organization"
    >
      <ProgressSpinner
        class="edit-organization-page__spinner"
        stroke-width="5"
      />
      <span>Loading organization...</span>
    </div>

    <Message
      v-else-if="loadErrorMessage"
      severity="error"
    >
      <div class="load-error">
        <span>{{ loadErrorMessage }}</span>
        <Button
          type="button"
          label="Try again"
          severity="danger"
          text
          size="small"
          @click="loadOrganization"
        />
      </div>
    </Message>

    <template v-else>
      <form
        id="edit-organization-form"
        class="organization-details-card"
        @submit.prevent="handleSubmit"
      >
        <div class="organization-details-card__header">
          <h2>Organization details</h2>
          <p>Manage the identifying details and current status of this organization.</p>
        </div>

        <div class="field">
          <label for="organization-name">Organization name</label>
          <InputText
            id="organization-name"
            v-model="organizationName"
            autocomplete="organization"
            minlength="3"
            maxlength="150"
            required
            :disabled="isSubmitting"
            :invalid="hasSubmitted && Boolean(organizationNameError)"
            :aria-invalid="hasSubmitted && Boolean(organizationNameError)"
            :aria-describedby="
              hasSubmitted && organizationNameError
                ? 'organization-name-error'
                : 'organization-name-hint'
            "
            fluid
          />
          <small
            v-if="hasSubmitted && organizationNameError"
            id="organization-name-error"
            class="field-error"
          >
            {{ organizationNameError }}
          </small>
          <small
            v-else
            id="organization-name-hint"
          >
            Use between 3 and 150 characters.
          </small>
        </div>

        <div class="field">
          <label for="organization-status">Status</label>
          <Select
            v-model="organizationStatus"
            input-id="organization-status"
            :options="statusOptions"
            option-label="label"
            option-value="value"
            :disabled="isSubmitting"
            fluid
          />
          <small>Disabled organizations are unavailable for active safety operations.</small>
        </div>

        <Message
          v-if="submitErrorMessage"
          severity="error"
        >
          {{ submitErrorMessage }}
        </Message>
      </form>

      <div class="edit-organization-page__actions">
        <Button
          type="button"
          label="Cancel"
          severity="secondary"
          outlined
          :disabled="isSubmitting"
          @click="cancel"
        />
        <Button
          type="submit"
          form="edit-organization-form"
          label="Save changes"
          :loading="isSubmitting"
          :disabled="isSubmitting"
        />
      </div>

      <section
        class="danger-zone"
        aria-labelledby="danger-zone-title"
      >
        <div>
          <h2 id="danger-zone-title">Danger zone</h2>
          <p>Deleting an organization is permanent and cannot be undone.</p>
        </div>
        <div class="danger-zone__action">
          <Button
            type="button"
            label="Delete organization"
            severity="danger"
            outlined
            disabled
          />
          <small>Not available yet</small>
        </div>
      </section>
    </template>
  </main>
</template>

<style scoped lang="scss">
.edit-organization-page {
  width: min(100%, 52rem);
  padding: 3rem clamp(1.25rem, 4vw, 4rem);
}

.edit-organization-page__eyebrow {
  margin: 0 0 0.5rem;
  color: var(--p-primary-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.edit-organization-page h1 {
  margin: 0;
}

.edit-organization-page__description {
  max-width: 38rem;
  margin: 0.75rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.edit-organization-page__loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.875rem;
  min-height: 12rem;
  color: var(--p-text-muted-color);
  text-align: center;
}

.edit-organization-page__spinner {
  width: 2rem;
  height: 2rem;
}

.load-error {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.organization-details-card {
  display: grid;
  gap: 1.5rem;
  padding: clamp(1.25rem, 3vw, 2rem);
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  margin-top: 2rem;
  background: var(--p-content-background);
}

.organization-details-card__header {
  padding-bottom: 1.25rem;
  border-bottom: 1px solid var(--p-content-border-color);
}

.organization-details-card__header h2,
.danger-zone h2 {
  margin: 0;
  font-size: 1.125rem;
}

.organization-details-card__header p,
.danger-zone p {
  margin: 0.5rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.5;
}

.field {
  display: grid;
  gap: 0.5rem;
}

.field label {
  font-size: 0.875rem;
  font-weight: 600;
}

.field small {
  color: var(--p-text-muted-color);
  line-height: 1.4;
}

.field .field-error {
  color: var(--p-red-600);
}

.edit-organization-page__actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 1.25rem;
}

.danger-zone {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1.5rem;
  padding: clamp(1.25rem, 3vw, 2rem);
  border: 1px solid var(--p-red-200);
  border-radius: var(--p-border-radius-lg);
  margin-top: 3rem;
  background: var(--p-content-background);
}

.danger-zone h2 {
  color: var(--p-red-700);
}

.danger-zone__action {
  display: grid;
  flex: 0 0 auto;
  justify-items: center;
  gap: 0.375rem;
}

.danger-zone__action small {
  color: var(--p-text-muted-color);
}

@media (max-width: 640px) {
  .edit-organization-page {
    padding-top: 2rem;
  }

  .load-error,
  .danger-zone {
    align-items: stretch;
    flex-direction: column;
  }

  .danger-zone__action {
    justify-items: stretch;
  }
}

@media (max-width: 480px) {
  .edit-organization-page__actions {
    flex-direction: column-reverse;
  }

  .edit-organization-page__actions :deep(.p-button) {
    width: 100%;
  }
}
</style>
