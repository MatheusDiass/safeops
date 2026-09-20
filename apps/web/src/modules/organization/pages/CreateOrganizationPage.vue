<script setup lang="ts">
import { Form, type FormSubmitEvent } from '@primevue/forms';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import {
  createOrganizationSchema,
  type CreateOrganizationFormValues,
} from '../schemas/organization.schema';
import { useOrganizationStore } from '../stores/organization.store';

const router = useRouter();
const { t } = useI18n();
const organizationStore = useOrganizationStore();
const initialValues: CreateOrganizationFormValues = { name: '' };
const resolver = computed(() => zodResolver(createOrganizationSchema(t)));
const isSubmitting = ref(false);
const errorMessage = ref<string>();

function cancel(): void {
  void router.push({ name: 'organization' });
}

async function submit(event: FormSubmitEvent): Promise<void> {
  errorMessage.value = undefined;

  if (!event.valid || isSubmitting.value) {
    return;
  }

  isSubmitting.value = true;

  try {
    await organizationStore.createOrganization({ name: event.values.name });
    await router.replace({ name: 'organization' });
  } catch (error: unknown) {
    errorMessage.value = error instanceof Error ? error.message : t('organizations.errors.create');
  } finally {
    isSubmitting.value = false;
  }
}
</script>

<template>
  <main class="create-organization-page">
    <header>
      <p class="create-organization-page__eyebrow">{{ t('organizations.title') }}</p>
      <h1>{{ t('organizations.create.title') }}</h1>
      <p class="create-organization-page__description">
        {{ t('organizations.create.description') }}
      </p>
    </header>

    <Form
      id="create-organization-form"
      v-slot="$form"
      class="create-organization-form"
      :initial-values="initialValues"
      :resolver="resolver"
      :validate-on-value-update="false"
      validate-on-blur
      validate-on-submit
      novalidate
      @submit="submit"
    >
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
          autofocus
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

      <Message
        v-if="errorMessage"
        severity="error"
      >
        {{ errorMessage }}
      </Message>
    </Form>

    <div class="create-organization-page__actions">
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
        form="create-organization-form"
        :label="t('organizations.actions.create')"
        :loading="isSubmitting"
        :disabled="isSubmitting"
      />
    </div>
  </main>
</template>

<style scoped lang="scss">
.create-organization-page {
  width: min(100%, 52rem);
  padding: 3rem clamp(1.25rem, 4vw, 4rem);
}

.create-organization-page__eyebrow {
  margin: 0 0 0.5rem;
  color: var(--p-primary-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.create-organization-page h1 {
  margin: 0;
}

.create-organization-page__description {
  max-width: 38rem;
  margin: 0.75rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}

.create-organization-form {
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

.create-organization-page__actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 1.25rem;
}

@media (max-width: 480px) {
  .create-organization-page {
    padding-top: 2rem;
  }

  .create-organization-page__actions {
    flex-direction: column-reverse;
  }

  .create-organization-page__actions :deep(.p-button) {
    width: 100%;
  }
}
</style>
