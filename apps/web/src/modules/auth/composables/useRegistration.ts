import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { registerUser } from '../api/auth.api';
import type { RegisterUserRequest } from '../types/auth.types';

export function useRegistration() {
  const { t } = useI18n();
  const isSubmitting = ref(false);
  const errorMessage = ref<string>();
  const isComplete = ref(false);

  async function submit(request: RegisterUserRequest): Promise<void> {
    isSubmitting.value = true;
    errorMessage.value = undefined;
    isComplete.value = false;

    try {
      await registerUser(request);
      isComplete.value = true;
    } catch (error: unknown) {
      errorMessage.value =
        error instanceof Error ? error.message : t('auth.registration.errors.generic');
    } finally {
      isSubmitting.value = false;
    }
  }

  return { errorMessage, isComplete, isSubmitting, submit };
}
