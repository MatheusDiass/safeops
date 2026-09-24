import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { incidentApi } from '../api/incident.api';
import type { Incident } from '../types/incident.types';

export function useIncidentList() {
  const { t } = useI18n();

  const incidents = ref<Incident[]>([]);
  const isLoading = ref(false);
  const errorMessage = ref<string | null>(null);

  let latestRequestId = 0;

  async function load(organizationId: string): Promise<void> {
    const requestId = ++latestRequestId;

    incidents.value = [];
    errorMessage.value = null;
    isLoading.value = true;

    try {
      const availableIncidents = await incidentApi.list(organizationId);

      if (requestId === latestRequestId) {
        incidents.value = availableIncidents;
      }
    } catch (error: unknown) {
      if (requestId === latestRequestId) {
        errorMessage.value = error instanceof Error ? error.message : t('incidents.errors.load');
      }
    } finally {
      if (requestId === latestRequestId) {
        isLoading.value = false;
      }
    }
  }

  return {
    incidents,
    isLoading,
    errorMessage,
    load,
  };
}
