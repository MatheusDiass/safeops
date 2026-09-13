import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useOrganizationStore } from '../../organization/stores/organization.store';
import { logoutSession } from '../api/auth.api';
import { useAuthStore } from '../stores/auth.store';

export function useLogout() {
  const router = useRouter();
  const authStore = useAuthStore();
  const organizationStore = useOrganizationStore();
  const isLoggingOut = ref(false);
  const errorMessage = ref<string>();

  async function logout(): Promise<void> {
    if (isLoggingOut.value) {
      return;
    }

    isLoggingOut.value = true;
    errorMessage.value = undefined;

    try {
      await logoutSession();
      authStore.clear();
      organizationStore.clear();
      await router.replace({ name: 'login' });
    } catch {
      errorMessage.value = 'Unable to log out. Please try again.';
    } finally {
      isLoggingOut.value = false;
    }
  }

  return { errorMessage, isLoggingOut, logout };
}
