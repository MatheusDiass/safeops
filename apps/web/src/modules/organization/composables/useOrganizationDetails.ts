import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useOrganizationStore } from '../stores/organization.store';
import type { Organization } from '../types/organization.types';

export function useOrganizationDetails() {
  const { t } = useI18n();
  const organizationStore = useOrganizationStore();

  const organization = ref<Organization | null>(null);
  const isLoading = ref(true);
  const errorMessage = ref<string | null>(null);

  let latestRequestId = 0;

  async function load(organizationId: string): Promise<void> {
    const requestId = ++latestRequestId;

    organization.value = null;
    errorMessage.value = null;
    isLoading.value = true;

    try {
      const loadedOrganization = await organizationStore.loadOrganization(organizationId);

      if (requestId === latestRequestId) {
        organization.value = loadedOrganization;
      }
    } catch (error: unknown) {
      if (requestId === latestRequestId) {
        errorMessage.value =
          error instanceof Error ? error.message : t('organizations.errors.load');
      }
    } finally {
      if (requestId === latestRequestId) {
        isLoading.value = false;
      }
    }
  }

  return {
    organization,
    isLoading,
    errorMessage,
    load,
  };
}
