import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { siteApi } from '../api/site.api';
import type { CreateSiteRequest } from '../types/site.types';

export function useSiteCreateForm() {
  const { t } = useI18n();

  const isSubmitting = ref(false);
  const errorMessage = ref<string | null>(null);

  async function submit(organizationId: string, request: CreateSiteRequest): Promise<boolean> {
    errorMessage.value = null;

    if (isSubmitting.value) {
      return false;
    }

    isSubmitting.value = true;

    try {
      await siteApi.create(organizationId, request);
      return true;
    } catch (error: unknown) {
      errorMessage.value = error instanceof Error ? error.message : t('sites.errors.create');
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
