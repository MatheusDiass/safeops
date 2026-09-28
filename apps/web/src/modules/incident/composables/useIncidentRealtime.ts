import { watch, type Ref } from 'vue';
import { connectRealtime } from '../../../shared/realtime/realtime.client';
import { REALTIME_EVENT_TYPES } from '../../../shared/realtime/realtime.types';

type UseIncidentRealtimeProps = {
  organizationId: Ref<string | null>;
  accessToken: Ref<string | undefined>;
  refresh: (organizationId: string) => Promise<void>;
};

export function useIncidentRealtime(props: UseIncidentRealtimeProps): void {
  watch(
    [props.organizationId, props.accessToken],
    ([organizationId, accessToken], _, onCleanup) => {
      if (!organizationId || !accessToken) {
        return;
      }

      const controller = new AbortController();

      onCleanup(() => controller.abort());

      void connectRealtime({
        organizationId,
        accessToken,
        signal: controller.signal,
        onEvent: (event) => {
          if (!REALTIME_EVENT_TYPES.includes(event.eventType)) {
            return;
          }

          if (
            event.organizationId !== organizationId ||
            event.organizationId !== props.organizationId.value
          ) {
            return;
          }

          void props.refresh(organizationId);
        },
      }).catch((error: unknown) => {
        if (!controller.signal.aborted) {
          console.error('Realtime connection failed.', error);
        }
      });
    },
    { immediate: true },
  );
}
