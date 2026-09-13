import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getAuthenticatedUser } from '../api/auth.api';
import type { AuthenticatedUser, LoginResponse } from '../types/auth.types';

export const useAuthStore = defineStore('auth', () => {
  const accessToken = ref<string>();
  const authenticatedUser = ref<AuthenticatedUser>();

  function setSession(session: LoginResponse): void {
    accessToken.value = session.accessToken;
  }

  async function loadAuthenticatedUser(): Promise<AuthenticatedUser> {
    if (authenticatedUser.value) {
      return authenticatedUser.value;
    }

    if (!accessToken.value) {
      throw new Error('An authenticated session is required to load the current user.');
    }

    const user = await getAuthenticatedUser();
    authenticatedUser.value = user;
    return user;
  }

  function clear(): void {
    accessToken.value = undefined;
    authenticatedUser.value = undefined;
  }

  return { accessToken, authenticatedUser, clear, loadAuthenticatedUser, setSession };
});
