<script setup lang="ts">
import { ref } from 'vue';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import Password from 'primevue/password';
import AuthLayout from '../../../layouts/AuthLayout.vue';
import { useLogin } from '../composables/useLogin';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
const email = ref('');
const password = ref('');
const { errorMessage, isSubmitting, submit } = useLogin();

function handleSubmit(): void {
  void submit(email.value, password.value);
}
</script>

<template>
  <AuthLayout>
    <template #title>{{ t('auth.login.title') }}</template>
    <template #subtitle>{{ t('auth.login.subtitle') }}</template>
    <form
      class="login-form"
      @submit.prevent="handleSubmit"
    >
      <div class="field">
        <label for="email">{{ t('auth.fields.email') }}</label>
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
          <label for="password">{{ t('auth.fields.password') }}</label
          ><a href="/forgot-password">{{ t('auth.login.actions.forgotPassword') }}</a>
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

      <Button
        type="submit"
        :label="t('auth.login.actions.submit')"
        :loading="isSubmitting"
        :disabled="isSubmitting"
        fluid
      />
    </form>
    <template #footer>
      {{ t('auth.login.registrationPrompt') }}
      <RouterLink to="/register">{{ t('auth.login.registrationLink') }}</RouterLink>
    </template>
  </AuthLayout>
</template>
