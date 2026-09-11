<script setup lang="ts">
import { computed, ref } from 'vue';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import Password from 'primevue/password';
import AuthLayout from '../../../layouts/AuthLayout.vue';
import { useRegistration } from '../composables/useRegistration';

const name = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');
const hasSubmitted = ref(false);
const { errorMessage, isComplete, isSubmitting, submit } = useRegistration();

const passwordsDoNotMatch = computed(
  () => confirmPassword.value.length > 0 && password.value !== confirmPassword.value,
);

function handleSubmit(): void {
  hasSubmitted.value = true;

  if (passwordsDoNotMatch.value) {
    return;
  }

  void submit({
    name: name.value.trim(),
    email: email.value.trim(),
    password: password.value,
  });
}
</script>

<template>
  <AuthLayout>
    <template #title>Create your SafeOps account</template>
    <template #subtitle>Set up your work account to start managing safety operations.</template>
    <form
      class="login-form"
      @submit.prevent="handleSubmit"
    >
      <div class="field">
        <label for="name">Full name</label>
        <InputText
          id="name"
          v-model="name"
          autocomplete="name"
          required
          :disabled="isSubmitting || isComplete"
          fluid
        />
      </div>

      <div class="field">
        <label for="email">Work email</label>
        <InputText
          id="email"
          v-model="email"
          type="email"
          autocomplete="email"
          required
          :disabled="isSubmitting || isComplete"
          :aria-invalid="Boolean(errorMessage)"
          :aria-describedby="errorMessage ? 'registration-error' : undefined"
          fluid
        />
      </div>

      <div class="field">
        <label for="password">Password</label>
        <Password
          v-model="password"
          input-id="password"
          autocomplete="new-password"
          required
          :feedback="false"
          toggle-mask
          :disabled="isSubmitting || isComplete"
          aria-describedby="password-hint"
          fluid
        />
        <small id="password-hint">Use between 15 and 128 characters.</small>
      </div>

      <div class="field">
        <label for="confirm-password">Confirm password</label>
        <Password
          v-model="confirmPassword"
          input-id="confirm-password"
          autocomplete="new-password"
          required
          :feedback="false"
          toggle-mask
          :disabled="isSubmitting || isComplete"
          :aria-invalid="hasSubmitted && passwordsDoNotMatch"
          :aria-describedby="passwordsDoNotMatch ? 'confirm-password-error' : undefined"
          fluid
        />
        <small
          v-if="hasSubmitted && passwordsDoNotMatch"
          id="confirm-password-error"
          class="field-error"
        >
          Passwords do not match.
        </small>
      </div>

      <Message
        v-if="errorMessage"
        id="registration-error"
        severity="error"
      >
        {{ errorMessage }}
      </Message>

      <Message
        v-if="isComplete"
        severity="success"
      >
        Your account has been created. You can now sign in.
      </Message>

      <Button
        type="submit"
        label="Create account"
        :loading="isSubmitting"
        :disabled="isSubmitting || isComplete"
        fluid
      />
    </form>

    <template #footer
      >Already have an account? <RouterLink to="/login">Sign in</RouterLink></template
    >
  </AuthLayout>
</template>
