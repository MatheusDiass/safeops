import type { Pinia } from 'pinia';
import { refreshSession } from '../modules/auth/api/auth.api';
import { useAuthStore } from '../modules/auth/stores/auth.store';
import { useOrganizationStore } from '../modules/organization/stores/organization.store';

export async function initializeAuthenticatedApplication(pinia: Pinia): Promise<boolean> {
  const authStore = useAuthStore(pinia);
  const organizationStore = useOrganizationStore(pinia);

  try {
    const session = await refreshSession();
    authStore.setSession(session);
    organizationStore.clear();
    await authStore.loadAuthenticatedUser();
  } catch {
    authStore.clear();
    organizationStore.clear();
    return false;
  }

  try {
    await organizationStore.loadOrganizations();
  } catch {
    // Organization loading failure does not invalidate the authenticated session.
  }

  return true;
}
