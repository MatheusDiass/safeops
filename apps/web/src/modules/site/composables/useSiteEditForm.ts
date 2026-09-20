import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { siteApi } from '../api/site.api';
import type { UpdateSiteRequest } from '../types/site.types';

export function useSiteEditForm() {
  const { t } = useI18n();

  const isSubmitting = ref(false);
  const errorMessage = ref<string | null>(null);

  async function submit(
    organizationId: string,
    siteId: string,
    request: UpdateSiteRequest,
  ): Promise<boolean> {
    errorMessage.value = null;

    if (isSubmitting.value) {
      return false;
    }

    isSubmitting.value = true;

    try {
      await siteApi.update(organizationId, siteId, request);
      return true;
    } catch (error: unknown) {
      errorMessage.value = error instanceof Error ? error.message : t('sites.errors.update');
      return false;
    } finally {
      isSubmitting.value = false;
    }
  }

  return {
    isSubmitting,
    errorMessage,
    submit,
  };
}
