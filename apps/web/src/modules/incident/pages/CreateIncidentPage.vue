<script setup lang="ts">
import { Form, type FormSubmitEvent } from '@primevue/forms';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import { storeToRefs } from 'pinia';
import Button from 'primevue/button';
import Message from 'primevue/message';
import { computed, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../../auth/stores/auth.store';
import { useOrganizationStore } from '../../organization/stores/organization.store';
import { useSiteList } from '../../site/composables/useSiteList';
import IncidentDetailsSection from '../components/IncidentDetailsSection.vue';
import IncidentLocationSection from '../components/IncidentLocationSection.vue';
import IncidentReporterSection from '../components/IncidentReporterSection.vue';
import { useIncidentCreateForm } from '../composables/useIncidentCreateForm';
import {
  createIncidentSchema,
  type CreateIncidentFormValues,
} from '../schemas/incident.schema';

const router = useRouter();
const { t } = useI18n();
const authStore = useAuthStore();
const organizationStore = useOrganizationStore();
const { authenticatedUser } = storeToRefs(authStore);
const { organizations, selectedOrganizationId } = storeToRefs(organizationStore);
const {
  errorMessage: siteErrorMessage,
  isLoading: areSitesLoading,
  load: loadSites,
  sites,
} = useSiteList();
const { errorMessage, isSubmitting, submit } = useIncidentCreateForm();

const initialValues: Partial<CreateIncidentFormValues> = {
  siteId: '',
  location: '',
  title: '',
  type: undefined,
  occurredAt: undefined,
  severity: null,
  description: '',
  immediateActions: '',
};
const resolver = computed(() => zodResolver(createIncidentSchema(t)));

const selectedOrganization = computed(() =>
  organizations.value.find(({ id }) => id === selectedOrganizationId.value),
);
const hasActiveSites = computed(() => sites.value.some(({ status }) => status === 'ACTIVE'));

watch(
  selectedOrganizationId,
  (organizationId) => {
    if (organizationId) {
      void loadSites(organizationId);
    }
  },
  { immediate: true },
);

function cancel(): void {
  void router.push({ name: 'incidents' });
}

async function handleSubmit(event: FormSubmitEvent): Promise<void> {
  if (!event.valid || !selectedOrganizationId.value) {
    return;
  }

  const values = event.values;
  const wasCreated = await submit(selectedOrganizationId.value, {
    title: values.title.trim(),
    description: values.description.trim(),
    type: values.type,
    severity: values.severity,
    occurredAt: values.occurredAt.toISOString(),
    location: values.location.trim() || null,
    immediateActions: values.immediateActions.trim() || null,
    siteId: values.siteId,
  });

  if (wasCreated) {
    await router.replace({ name: 'incidents' });
  }
}
</script>

<template>
  <main class="create-incident-page">
    <header class="create-incident-page__header">
      <p class="create-incident-page__eyebrow">{{ t('incidents.title') }}</p>
      <h1>{{ t('incidents.create.title') }}</h1>
      <p>{{ t('incidents.create.description') }}</p>
    </header>

    <Message
      v-if="!selectedOrganization"
      class="create-incident-page__notice"
      severity="warn"
    >
      {{ t('incidents.selectOrganization.description') }}
    </Message>

    <Form
      id="create-incident-form"
      :key="selectedOrganizationId ?? 'no-organization'"
      v-slot="$form"
      class="incident-form"
      :initial-values="initialValues"
      :resolver="resolver"
      :validate-on-value-update="false"
      validate-on-blur
      validate-on-submit
      novalidate
      @submit="handleSubmit"
    >
      <IncidentLocationSection
        :form="$form"
        :sites="sites"
        :are-sites-loading="areSitesLoading"
        :site-error-message="siteErrorMessage"
        :has-selected-organization="Boolean(selectedOrganization)"
        :is-submitting="isSubmitting"
      />
      <IncidentDetailsSection
        :form="$form"
        :is-submitting="isSubmitting"
      />
      <IncidentReporterSection :reporter-name="authenticatedUser?.name" />

      <Message
        v-if="errorMessage"
        severity="error"
      >
        {{ errorMessage }}
      </Message>
    </Form>

    <div class="create-incident-page__actions">
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
        form="create-incident-form"
        :label="t('incidents.actions.report')"
        :loading="isSubmitting"
        :disabled="
          isSubmitting ||
          areSitesLoading ||
          !selectedOrganization ||
          !hasActiveSites ||
          Boolean(siteErrorMessage)
        "
      />
    </div>
  </main>
</template>

<style scoped lang="scss">
.create-incident-page {
  width: min(100%, 68rem);
  padding: 3rem clamp(1.25rem, 4vw, 4rem);
}

.create-incident-page__header h1 {
  margin: 0;
}

.create-incident-page__header > p:last-child {
  max-width: 42rem;
  margin: 0.75rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.create-incident-page__eyebrow {
  margin: 0 0 0.5rem;
  color: var(--p-primary-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.create-incident-page__notice {
  margin-top: 2rem;
}

.incident-form {
  display: grid;
  gap: 1.25rem;
  margin-top: 2rem;
}

.create-incident-page__actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 1.25rem;
}

@media (max-width: 700px) {
  .create-incident-page {
    padding-top: 2rem;
  }
}

@media (max-width: 480px) {
  .create-incident-page__actions {
    flex-direction: column-reverse;
  }

  .create-incident-page__actions :deep(.p-button) {
    width: 100%;
  }
}
</style>
