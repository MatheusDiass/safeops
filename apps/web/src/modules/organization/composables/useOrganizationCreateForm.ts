import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useOrganizationStore } from '../stores/organization.store';
import type { CreateOrganizationRequest } from '../types/organization.types';

export function useOrganizationCreateForm() {
  const { t } = useI18n();
  const organizationStore = useOrganizationStore();

  const isSubmitting = ref(false);
  const errorMessage = ref<string | null>(null);

  async function submit(request: CreateOrganizationRequest): Promise<boolean> {
    errorMessage.value = null;

    if (isSubmitting.value) {
      return false;
    }

    isSubmitting.value = true;

    try {
      await organizationStore.createOrganization(request);
      return true;
    } catch (error: unknown) {
      errorMessage.value =
        error instanceof Error ? error.message : t('organizations.errors.create');
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
