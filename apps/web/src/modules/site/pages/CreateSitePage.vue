<script setup lang="ts">
import { Form, type FormSubmitEvent } from '@primevue/forms';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import { storeToRefs } from 'pinia';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { useOrganizationStore } from '../../organization/stores/organization.store';
import { createSiteSchema, type CreateSiteFormValues } from '../schemas/site.schema';
import { useSiteCreateForm } from '../composables/useSiteCreateForm';

const router = useRouter();
const { t } = useI18n();
const organizationStore = useOrganizationStore();
const { organizations, selectedOrganizationId } = storeToRefs(organizationStore);
const { errorMessage, isSubmitting, submit } = useSiteCreateForm();
const initialValues: CreateSiteFormValues = { name: '' };
const resolver = computed(() => zodResolver(createSiteSchema(t)));

const selectedOrganization = computed(() =>
  organizations.value.find(({ id }) => id === selectedOrganizationId.value),
);
const pageDescription = computed(() =>
  selectedOrganization.value
    ? t('sites.create.description', { organization: selectedOrganization.value.name })
    : t('sites.selectOrganization.description'),
);

function cancel(): void {
  void router.push({ name: 'sites' });
}

async function handleSubmit(event: FormSubmitEvent): Promise<void> {
  if (!event.valid || !selectedOrganizationId.value) {
    return;
  }

  const wasCreated = await submit(selectedOrganizationId.value, { name: event.values.name });

  if (wasCreated) {
    await router.replace({ name: 'sites' });
  }
}
</script>

<template>
  <main class="create-site-page">
    <header>
      <p class="create-site-page__eyebrow">{{ t('sites.title') }}</p>
      <h1>{{ t('sites.create.title') }}</h1>
      <p class="create-site-page__description">
        {{ pageDescription }}
      </p>
    </header>

    <Message
      v-if="!selectedOrganization"
      severity="warn"
    >
      {{ t('sites.selectOrganization.description') }}
    </Message>

    <Form
      id="create-site-form"
      v-slot="$form"
      class="create-site-form"
      :initial-values="initialValues"
      :resolver="resolver"
      :validate-on-value-update="false"
      validate-on-blur
      validate-on-submit
      novalidate
      @submit="handleSubmit"
    >
      <div class="field">
        <label for="site-name">{{ t('sites.fields.name.label') }}</label>
        <InputText
          id="site-name"
          name="name"
          autocomplete="off"
          :disabled="isSubmitting || !selectedOrganization"
          :invalid="$form.name?.invalid"
          :aria-invalid="$form.name?.invalid"
          aria-required="true"
          :aria-describedby="$form.name?.invalid ? 'site-name-error' : 'site-name-hint'"
          autofocus
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

      <Message
        v-if="errorMessage"
        severity="error"
      >
        {{ errorMessage }}
      </Message>
    </Form>

    <div class="create-site-page__actions">
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
        form="create-site-form"
        :label="t('sites.actions.create')"
        :loading="isSubmitting"
        :disabled="isSubmitting || !selectedOrganization"
      />
    </div>
  </main>
</template>

<style scoped lang="scss">
.create-site-page {
  width: min(100%, 52rem);
  padding: 3rem clamp(1.25rem, 4vw, 4rem);
}

.create-site-page__eyebrow {
  margin: 0 0 0.5rem;
  color: var(--p-primary-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.create-site-page h1 {
  margin: 0;
}

.create-site-page__description {
  max-width: 38rem;
  margin: 0.75rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.create-site-page > .p-message {
  margin-top: 2rem;
}

.create-site-form {
  display: grid;
  gap: 1.5rem;
  padding: clamp(1.25rem, 3vw, 2rem);
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  margin-top: 2rem;
  background: var(--p-content-background);
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

.create-site-page__actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 1.25rem;
}

@media (max-width: 480px) {
  .create-site-page {
    padding-top: 2rem;
  }

  .create-site-page__actions {
    flex-direction: column-reverse;
  }

  .create-site-page__actions :deep(.p-button) {
    width: 100%;
  }
}
</style>
