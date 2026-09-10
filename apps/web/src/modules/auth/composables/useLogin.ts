import { ref } from 'vue';
import { getAuthenticatedUser, login } from '../api/auth.api';
import type { AuthenticatedUser } from '../types/auth.types';

export function useLogin() {
  const isSubmitting = ref(false);
  const errorMessage = ref<string>();
  const authenticatedUser = ref<AuthenticatedUser>();

  async function submit(email: string, password: string): Promise<void> {
    isSubmitting.value = true;
    errorMessage.value = undefined;
    try {
      const session = await login({ email, password });
      authenticatedUser.value = await getAuthenticatedUser(session.accessToken);
    } catch (error: unknown) {
      errorMessage.value =
        error instanceof Error ? error.message : 'Unable to sign in. Please try again.';
    } finally {
      isSubmitting.value = false;
    }
  }
  return { authenticatedUser, errorMessage, isSubmitting, submit };
}
