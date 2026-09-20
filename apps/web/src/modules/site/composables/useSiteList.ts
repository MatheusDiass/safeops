import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { siteApi } from '../api/site.api';
import type { Site } from '../types/site.types';

export function useSiteList() {
  const { t } = useI18n();

  const sites = ref<Site[]>([]);
  const isLoading = ref(false);
  const errorMessage = ref<string | null>(null);

  let latestRequestId = 0;

  async function load(organizationId: string | null): Promise<void> {
    const requestId = ++latestRequestId;

    sites.value = [];
    errorMessage.value = null;

    if (!organizationId) {
      isLoading.value = false;
      return;
    }

    isLoading.value = true;

    try {
      const availableSites = await siteApi.list(organizationId);

      if (requestId === latestRequestId) {
        sites.value = availableSites;
      }
    } catch (error: unknown) {
      if (requestId === latestRequestId) {
        errorMessage.value = error instanceof Error ? error.message : t('sites.errors.load');
      }
    } finally {
      if (requestId === latestRequestId) {
        isLoading.value = false;
      }
    }
  }

  return {
    sites,
    isLoading,
    errorMessage,
    load,
  };
}
