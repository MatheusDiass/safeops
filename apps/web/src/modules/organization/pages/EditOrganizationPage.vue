<script setup lang="ts">
import { Form, type FormSubmitEvent } from '@primevue/forms';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import ProgressSpinner from 'primevue/progressspinner';
import Select from 'primevue/select';
import { computed, ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
  updateOrganizationSchema,
  type UpdateOrganizationFormValues,
} from '../schemas/organization.schema';
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
const initialValues = ref<UpdateOrganizationFormValues>({ name: '', status: 'ACTIVE' });
const resolver = zodResolver(updateOrganizationSchema);
const isLoading = ref(true);
const isSubmitting = ref(false);
const loadErrorMessage = ref<string>();
const submitErrorMessage = ref<string>();

const organizationId = computed(() => {
  const routeId = route.params.organizationId;

  return Array.isArray(routeId) ? routeId[0] : routeId;
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
    initialValues.value = {
      name: organization.name,
      status: organization.status,
    };
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

async function submit(event: FormSubmitEvent): Promise<void> {
  submitErrorMessage.value = undefined;

  if (!event.valid || !organizationId.value || isSubmitting.value) {
    return;
  }

  isSubmitting.value = true;

  try {
    await organizationStore.updateOrganization(organizationId.value, {
      name: event.values.name,
      status: event.values.status,
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
      <Form
        id="edit-organization-form"
        v-slot="$form"
        class="organization-details-card"
        :initial-values="initialValues"
        :resolver="resolver"
        :validate-on-value-update="false"
        validate-on-blur
        validate-on-submit
        novalidate
        @submit="submit"
      >
        <div class="organization-details-card__header">
          <h2>Organization details</h2>
          <p>Manage the identifying details and current status of this organization.</p>
        </div>

        <div class="field">
          <label for="organization-name">Organization name</label>
          <InputText
            id="organization-name"
            name="name"
            autocomplete="organization"
            :disabled="isSubmitting"
            :invalid="$form.name?.invalid"
            :aria-invalid="$form.name?.invalid"
            aria-required="true"
            :aria-describedby="
              $form.name?.invalid ? 'organization-name-error' : 'organization-name-hint'
            "
            fluid
          />
          <Message
            v-if="$form.name?.invalid"
            id="organization-name-error"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ $form.name.error?.message }}
          </Message>
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
            name="status"
            input-id="organization-status"
            :options="statusOptions"
            option-label="label"
            option-value="value"
            :disabled="isSubmitting"
            :invalid="$form.status?.invalid"
            :aria-invalid="$form.status?.invalid"
            :aria-describedby="
              $form.status?.invalid ? 'organization-status-error' : 'organization-status-hint'
            "
            aria-required="true"
            fluid
          />
          <Message
            v-if="$form.status?.invalid"
            id="organization-status-error"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ $form.status.error?.message }}
          </Message>
          <small
            v-else
            id="organization-status-hint"
          >
            Disabled organizations are unavailable for active safety operations.
          </small>
        </div>

        <Message
          v-if="submitErrorMessage"
          severity="error"
        >
          {{ submitErrorMessage }}
        </Message>
      </Form>

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
