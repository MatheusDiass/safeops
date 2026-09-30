import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { incidentApi } from '../api/incident.api';
import type { CreateIncidentRequest } from '../types/incident.types';

export function useIncidentCreateForm() {
  const { t } = useI18n();

  const isSubmitting = ref(false);
  const errorMessage = ref<string | null>(null);

  async function submit(organizationId: string, request: CreateIncidentRequest): Promise<boolean> {
    errorMessage.value = null;

    if (isSubmitting.value) {
      return false;
    }

    isSubmitting.value = true;

    try {
      await incidentApi.create(organizationId, request);
      return true;
    } catch (error: unknown) {
      errorMessage.value = error instanceof Error ? error.message : t('incidents.errors.create');
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
