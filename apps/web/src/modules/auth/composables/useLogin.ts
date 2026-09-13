import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '../api/auth.api';
import { useAuthStore } from '../stores/auth.store';
import { useOrganizationStore } from '../../organization/stores/organization.store';

export function useLogin() {
  const router = useRouter();
  const authStore = useAuthStore();
  const organizationStore = useOrganizationStore();
  const isSubmitting = ref(false);
  const errorMessage = ref<string>();

  async function submit(email: string, password: string): Promise<void> {
    isSubmitting.value = true;
    errorMessage.value = undefined;
    try {
      const session = await login({ email, password });
      authStore.setSession(session);
      organizationStore.clear();

      try {
        await authStore.loadAuthenticatedUser();
      } catch (error: unknown) {
        authStore.clear();
        throw error;
      }

      try {
        await organizationStore.loadOrganizations();
      } catch {
        // Organization loading failure does not invalidate the authenticated session.
      }

      await router.push({ name: 'dashboard' });
    } catch (error: unknown) {
      errorMessage.value =
        error instanceof Error ? error.message : 'Unable to sign in. Please try again.';
    } finally {
      isSubmitting.value = false;
    }
  }
  return { errorMessage, isSubmitting, submit };
}
