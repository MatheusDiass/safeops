<script setup lang="ts">
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useOrganizationStore } from '../stores/organization.store';

const router = useRouter();
const organizationStore = useOrganizationStore();
const organizationName = ref('');
const hasSubmitted = ref(false);
const isSubmitting = ref(false);
const errorMessage = ref<string>();

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

function cancel(): void {
  void router.push({ name: 'organization' });
}

async function submit(): Promise<void> {
  hasSubmitted.value = true;
  errorMessage.value = undefined;

  if (organizationNameError.value) {
    return;
  }

  isSubmitting.value = true;

  try {
    await organizationStore.createOrganization({ name: normalizedOrganizationName.value });
    await router.replace({ name: 'organization' });
  } catch (error: unknown) {
    errorMessage.value =
      error instanceof Error
        ? error.message
        : 'Unable to create the organization. Please try again.';
  } finally {
    isSubmitting.value = false;
  }
}

function handleSubmit(): void {
  void submit();
}
</script>

<template>
  <main class="create-organization-page">
    <header>
      <p class="create-organization-page__eyebrow">Organizations</p>
      <h1>Create organization</h1>
      <p class="create-organization-page__description">
        Add an organization to start managing its sites and safety operations.
      </p>
    </header>

    <form
      id="create-organization-form"
      class="create-organization-form"
      @submit.prevent="handleSubmit"
    >
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
          autofocus
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

      <Message
        v-if="errorMessage"
        severity="error"
      >
        {{ errorMessage }}
      </Message>
    </form>

    <div class="create-organization-page__actions">
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
        form="create-organization-form"
        label="Create organization"
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

.field .field-error {
  color: var(--p-red-600);
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
