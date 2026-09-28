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
  let latestLoadRequestId = 0;
  let backgroundRefresh: Promise<void> | null = null;
  let queuedRefreshOrganizationId: string | null = null;

  async function requestIncidents(organizationId: string, background: boolean): Promise<void> {
    const requestId = ++latestRequestId;

    if (!background) {
      latestLoadRequestId = requestId;
      incidents.value = [];
      errorMessage.value = null;
      isLoading.value = true;
    }

    try {
      const availableIncidents = await incidentApi.list(organizationId);

      if (requestId === latestRequestId) {
        incidents.value = availableIncidents;
      }
    } catch (error: unknown) {
      if (background) {
        console.warn('Could not refresh incidents.', error);
        return;
      }

      if (!background && requestId === latestRequestId) {
        errorMessage.value = error instanceof Error ? error.message : t('incidents.errors.load');
      }
    } finally {
      if (!background && requestId === latestLoadRequestId) {
        isLoading.value = false;
      }
    }
  }

  async function load(organizationId: string): Promise<void> {
    await requestIncidents(organizationId, false);
  }

  function refresh(organizationId: string): Promise<void> {
    queuedRefreshOrganizationId = organizationId;
    backgroundRefresh ??= runQueuedRefreshes();
    return backgroundRefresh;
  }

  async function runQueuedRefreshes(): Promise<void> {
    try {
      while (queuedRefreshOrganizationId !== null) {
        const organizationId = queuedRefreshOrganizationId;
        queuedRefreshOrganizationId = null;
        await requestIncidents(organizationId, true);
      }
    } finally {
      backgroundRefresh = null;
    }
  }

  return {
    incidents,
    isLoading,
    errorMessage,
    load,
    refresh,
  };
}
