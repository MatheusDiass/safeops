import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useOrganizationStore } from '../stores/organization.store';

export function useOrganizationList() {
  const { t } = useI18n();
  const organizationStore = useOrganizationStore();
  const { organizations } = storeToRefs(organizationStore);

  const isLoading = ref(false);
  const errorMessage = ref<string | null>(null);

  let latestRequestId = 0;

  async function load(): Promise<void> {
    const requestId = ++latestRequestId;

    errorMessage.value = null;
    isLoading.value = true;

    try {
      await organizationStore.loadOrganizations();
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
    organizations,
    isLoading,
    errorMessage,
    load,
  };
}
