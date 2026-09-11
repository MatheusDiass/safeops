<script setup lang="ts">
import { ref } from 'vue';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import Password from 'primevue/password';
import AuthLayout from '../../../layouts/AuthLayout.vue';
import { useLogin } from '../composables/useLogin';

const email = ref('');
const password = ref('');
const { authenticatedUser, errorMessage, isSubmitting, submit } = useLogin();

function handleSubmit(): void {
  void submit(email.value, password.value);
}
</script>

<template>
  <AuthLayout>
    <template #title>Sign in to SafeOps</template>
    <template #subtitle
      >Use your work account to access your organizations and incident queue.</template
    >
    <form
      class="login-form"
      @submit.prevent="handleSubmit"
    >
      <div class="field">
        <label for="email">Work email</label>
        <InputText
          id="email"
          v-model="email"
          type="email"
          autocomplete="email"
          required
          :disabled="isSubmitting"
          :aria-invalid="Boolean(errorMessage)"
          :aria-describedby="errorMessage ? 'login-error' : undefined"
          fluid
        />
      </div>

      <div class="field">
        <div class="field__header">
          <label for="password">Password</label><a href="/forgot-password">Forgot password?</a>
        </div>
        <Password
          v-model="password"
          input-id="password"
          autocomplete="current-password"
          required
          :feedback="false"
          toggle-mask
          :disabled="isSubmitting"
          :aria-invalid="Boolean(errorMessage)"
          :aria-describedby="errorMessage ? 'login-error' : undefined"
          fluid
        />
      </div>

      <Message
        v-if="errorMessage"
        id="login-error"
        severity="error"
      >
        {{ errorMessage }}
      </Message>

      <Message
        v-if="authenticatedUser"
        severity="success"
      >
        Signed in as {{ authenticatedUser.name }}.
      </Message>

      <Button
        type="submit"
        label="Sign in"
        :loading="isSubmitting"
        :disabled="isSubmitting"
        fluid
      />
    </form>
    <template #footer>Don't have an account? <RouterLink to="/register">Create one</RouterLink></template>
  </AuthLayout>
</template>
