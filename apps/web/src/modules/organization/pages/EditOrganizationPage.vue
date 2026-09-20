<script setup lang="ts">
import { Form, type FormSubmitEvent } from '@primevue/forms';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import ProgressSpinner from 'primevue/progressspinner';
import Select from 'primevue/select';
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { useOrganizationDetails } from '../composables/useOrganizationDetails';
import { useOrganizationUpdateForm } from '../composables/useOrganizationUpdateForm';
import {
  updateOrganizationSchema,
  type UpdateOrganizationFormValues,
} from '../schemas/organization.schema';
import type { OrganizationStatus } from '../types/organization.types';

type StatusOption = {
  label: string;
  value: OrganizationStatus;
};

const route = useRoute();
const router = useRouter();
const { t } = useI18n();
const { organization, isLoading, errorMessage: loadErrorMessage, load } = useOrganizationDetails();
const { isSubmitting, errorMessage: submitErrorMessage, submit } = useOrganizationUpdateForm();
const statusOptions = computed<StatusOption[]>(() => [
  { label: t('organizations.status.ACTIVE'), value: 'ACTIVE' },
  { label: t('organizations.status.DISABLED'), value: 'DISABLED' },
]);
const initialValues = ref<UpdateOrganizationFormValues | null>(null);
const resolver = computed(() => zodResolver(updateOrganizationSchema(t)));

const organizationId = computed(() => {
  const routeId = route.params.organizationId;

  return Array.isArray(routeId) ? routeId[0] : routeId;
});

async function loadOrganization(): Promise<void> {
  initialValues.value = null;
  await load(organizationId.value);

  if (!organization.value) {
    return;
  }

  initialValues.value = {
    name: organization.value.name,
    status: organization.value.status,
  };
}

function cancel(): void {
  void router.push({ name: 'organization' });
}

async function handleSubmit(event: FormSubmitEvent): Promise<void> {
  if (!event.valid || !organizationId.value) {
    return;
  }

  const wasUpdated = await submit(organizationId.value, {
    name: event.values.name,
    status: event.values.status,
  });

  if (!wasUpdated) {
    return;
  }

  await router.replace({ name: 'organization' });
}

onMounted(() => {
  void loadOrganization();
});
</script>

<template>
  <main class="edit-organization-page">
    <header>
      <p class="edit-organization-page__eyebrow">{{ t('organizations.title') }}</p>
      <h1>{{ t('organizations.edit.title') }}</h1>
      <p class="edit-organization-page__description">
        {{ t('organizations.edit.description') }}
      </p>
    </header>

    <div
      v-if="isLoading"
      class="edit-organization-page__loading"
      role="status"
      :aria-label="t('organizations.loadingLabel')"
    >
      <ProgressSpinner
        class="edit-organization-page__spinner"
        stroke-width="5"
      />
      <span>{{ t('organizations.loading') }}</span>
    </div>

    <Message
      v-else-if="loadErrorMessage"
      severity="error"
    >
      <div class="load-error">
        <span>{{ loadErrorMessage }}</span>
        <Button
          type="button"
          :label="t('common.actions.retry')"
          severity="danger"
          text
          size="small"
          @click="loadOrganization"
        />
      </div>
    </Message>

    <template v-else-if="initialValues">
      <Form
        id="edit-organization-form"
        :key="organizationId"
        v-slot="$form"
        class="organization-details-card"
        :initial-values="initialValues"
        :resolver="resolver"
        :validate-on-value-update="false"
        validate-on-blur
        validate-on-submit
        novalidate
        @submit="handleSubmit"
      >
        <div class="organization-details-card__header">
          <h2>{{ t('organizations.edit.detailsTitle') }}</h2>
          <p>{{ t('organizations.edit.detailsDescription') }}</p>
        </div>

        <div class="field">
          <label for="organization-name">{{ t('organizations.fields.name.label') }}</label>
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
            {{ t('organizations.fields.name.hint') }}
          </small>
        </div>

        <div class="field">
          <label for="organization-status">{{ t('organizations.fields.status.label') }}</label>
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
            {{ t('organizations.fields.status.hint') }}
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
          :label="t('common.actions.cancel')"
          severity="secondary"
          outlined
          :disabled="isSubmitting"
          @click="cancel"
        />
        <Button
          type="submit"
          form="edit-organization-form"
          :label="t('common.actions.save')"
          :loading="isSubmitting"
          :disabled="isSubmitting"
        />
      </div>

      <section
        class="danger-zone"
        aria-labelledby="danger-zone-title"
      >
        <div>
          <h2 id="danger-zone-title">{{ t('organizations.dangerZone.title') }}</h2>
          <p>{{ t('organizations.dangerZone.description') }}</p>
        </div>
        <div class="danger-zone__action">
          <Button
            type="button"
            :label="t('organizations.actions.delete')"
            severity="danger"
            outlined
            disabled
          />
          <small>{{ t('organizations.dangerZone.unavailable') }}</small>
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
