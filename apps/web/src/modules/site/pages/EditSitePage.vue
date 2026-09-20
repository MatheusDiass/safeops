<script setup lang="ts">
import { Form, type FormSubmitEvent } from '@primevue/forms';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import { storeToRefs } from 'pinia';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import ProgressSpinner from 'primevue/progressspinner';
import Select from 'primevue/select';
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { useOrganizationStore } from '../../organization/stores/organization.store';
import { useSiteDetails } from '../composables/useSiteDetails';
import { useSiteEditForm } from '../composables/useSiteEditForm';
import { updateSiteSchema, type UpdateSiteFormValues } from '../schemas/site.schema';
import type { SiteStatus } from '../types/site.types';

type StatusOption = {
  label: string;
  value: SiteStatus;
};

const route = useRoute();
const router = useRouter();
const { t } = useI18n();
const organizationStore = useOrganizationStore();
const { selectedOrganizationId } = storeToRefs(organizationStore);
const { site, isLoading, errorMessage: loadErrorMessage, load } = useSiteDetails();
const { isSubmitting, errorMessage: submitErrorMessage, submit } = useSiteEditForm();
const statusOptions = computed<StatusOption[]>(() => [
  { label: t('sites.status.ACTIVE'), value: 'ACTIVE' },
  { label: t('sites.status.DISABLED'), value: 'DISABLED' },
]);
const initialValues = ref<UpdateSiteFormValues | null>();
const resolver = computed(() => zodResolver(updateSiteSchema(t)));

const siteId = computed(() => {
  const routeId = route.params.siteId;

  return Array.isArray(routeId) ? routeId[0] : routeId;
});

async function loadSite(): Promise<void> {
  if (!selectedOrganizationId.value || !siteId.value) {
    return;
  }

  await load(selectedOrganizationId.value, siteId.value);

  if (!site.value) {
    return;
  }

  initialValues.value = {
    name: site.value.name,
    status: site.value.status,
  };
}

function cancel(): void {
  void router.push({ name: 'sites' });
}

async function handleSubmit(event: FormSubmitEvent): Promise<void> {
  if (!event.valid || !selectedOrganizationId.value) {
    return;
  }

  const updated = await submit(selectedOrganizationId.value, siteId.value, {
    name: event.values.name,
    status: event.values.status,
  });

  if (updated) {
    await router.replace({ name: 'sites' });
  }
}

onMounted(() => {
  void loadSite();
});
</script>

<template>
  <main class="edit-site-page">
    <header>
      <p class="edit-site-page__eyebrow">{{ t('sites.title') }}</p>
      <h1>{{ t('sites.edit.title') }}</h1>
      <p class="edit-site-page__description">
        {{ t('sites.edit.description') }}
      </p>
    </header>

    <div
      v-if="isLoading"
      class="edit-site-page__loading"
      role="status"
      :aria-label="t('sites.loadingLabel')"
    >
      <ProgressSpinner
        class="edit-site-page__spinner"
        stroke-width="5"
      />
      <span>{{ t('sites.loading') }}</span>
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
          @click="loadSite"
        />
      </div>
    </Message>

    <template v-else-if="initialValues">
      <Form
        id="edit-site-form"
        v-slot="$form"
        class="site-details-card"
        :initial-values="initialValues"
        :resolver="resolver"
        :validate-on-value-update="false"
        validate-on-blur
        validate-on-submit
        novalidate
        @submit="handleSubmit"
      >
        <div class="site-details-card__header">
          <h2>{{ t('sites.edit.detailsTitle') }}</h2>
          <p>{{ t('sites.edit.detailsDescription') }}</p>
        </div>

        <div class="field">
          <label for="site-name">{{ t('sites.fields.name.label') }}</label>
          <InputText
            id="site-name"
            name="name"
            autocomplete="off"
            :disabled="isSubmitting"
            :invalid="$form.name?.invalid"
            :aria-invalid="$form.name?.invalid"
            aria-required="true"
            :aria-describedby="$form.name?.invalid ? 'site-name-error' : 'site-name-hint'"
            fluid
          />
          <Message
            v-if="$form.name?.invalid"
            id="site-name-error"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ $form.name.error?.message }}
          </Message>
          <small
            v-else
            id="site-name-hint"
          >
            {{ t('sites.fields.name.hint') }}
          </small>
        </div>

        <div class="field">
          <label for="site-status">{{ t('sites.fields.status.label') }}</label>
          <Select
            name="status"
            input-id="site-status"
            :options="statusOptions"
            option-label="label"
            option-value="value"
            :disabled="isSubmitting"
            :invalid="$form.status?.invalid"
            :aria-invalid="$form.status?.invalid"
            :aria-describedby="$form.status?.invalid ? 'site-status-error' : 'site-status-hint'"
            aria-required="true"
            fluid
          />
          <Message
            v-if="$form.status?.invalid"
            id="site-status-error"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ $form.status.error?.message }}
          </Message>
          <small
            v-else
            id="site-status-hint"
          >
            {{ t('sites.fields.status.hint') }}
          </small>
        </div>

        <Message
          v-if="submitErrorMessage"
          severity="error"
        >
          {{ submitErrorMessage }}
        </Message>
      </Form>

      <div class="edit-site-page__actions">
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
          form="edit-site-form"
          :label="t('common.actions.save')"
          :loading="isSubmitting"
          :disabled="isSubmitting"
        />
      </div>
    </template>
  </main>
</template>

<style scoped lang="scss">
.edit-site-page {
  width: min(100%, 52rem);
  padding: 3rem clamp(1.25rem, 4vw, 4rem);
}

.edit-site-page__eyebrow {
  margin: 0 0 0.5rem;
  color: var(--p-primary-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.edit-site-page h1 {
  margin: 0;
}

.edit-site-page__description {
  max-width: 38rem;
  margin: 0.75rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.edit-site-page__loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.875rem;
  min-height: 12rem;
  color: var(--p-text-muted-color);
  text-align: center;
}

.edit-site-page__spinner {
  width: 2rem;
  height: 2rem;
}

.load-error {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.site-details-card {
  display: grid;
  gap: 1.5rem;
  padding: clamp(1.25rem, 3vw, 2rem);
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  margin-top: 2rem;
  background: var(--p-content-background);
}

.site-details-card__header {
  padding-bottom: 1.25rem;
  border-bottom: 1px solid var(--p-content-border-color);
}

.site-details-card__header h2 {
  margin: 0;
  font-size: 1.125rem;
}

.site-details-card__header p {
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

.edit-site-page__actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 1.25rem;
}

@media (max-width: 640px) {
  .edit-site-page {
    padding-top: 2rem;
  }

  .load-error {
    align-items: stretch;
    flex-direction: column;
  }
}

@media (max-width: 480px) {
  .edit-site-page__actions {
    flex-direction: column-reverse;
  }

  .edit-site-page__actions :deep(.p-button) {
    width: 100%;
  }
}
</style>
