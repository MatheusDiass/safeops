import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { siteApi } from '../api/site.api';
import type { Site } from '../types/site.types';

export function useSiteDetails() {
  const { t } = useI18n();

  const site = ref<Site | null>(null);
  const isLoading = ref(true);
  const errorMessage = ref<string | null>(null);

  let latestRequestId = 0;

  async function load(organizationId: string, siteId: string): Promise<void> {
    const requestId = ++latestRequestId;

    site.value = null;
    errorMessage.value = null;

    isLoading.value = true;

    try {
      const loadedSite = await siteApi.get(organizationId, siteId);

      if (requestId === latestRequestId) {
        site.value = loadedSite;
        return;
      }

      return;
    } catch (error: unknown) {
      if (requestId === latestRequestId) {
        errorMessage.value = error instanceof Error ? error.message : t('sites.errors.load');
      }

      return;
    } finally {
      if (requestId === latestRequestId) {
        isLoading.value = false;
      }
    }
  }

  return {
    site,
    isLoading,
    errorMessage,
    load,
  };
}
