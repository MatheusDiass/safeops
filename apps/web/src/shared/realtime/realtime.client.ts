import {
  EventStreamContentType,
  fetchEventSource,
  type EventSourceMessage,
} from '@microsoft/fetch-event-source';
import { apiBaseUrl } from '../api/http';
import { realtimeEventSchema, type RealtimeEvent } from './realtime.types';

type ConnectRealtimeProps = {
  organizationId: string;
  accessToken: string;
  signal: AbortSignal;
  onEvent: (event: RealtimeEvent) => void;
};

function parseRealtimeEvent(message: EventSourceMessage): RealtimeEvent | null {
  let data: unknown;

  try {
    data = JSON.parse(message.data) as unknown;
  } catch (error: unknown) {
    console.warn('Ignoring malformed realtime event.', error);
    return null;
  }

  const eventResult = realtimeEventSchema.safeParse(data);

  if (!eventResult.success) {
    console.warn('Ignoring invalid realtime event.', {
      event: eventResult.success ? undefined : eventResult.error.issues,
    });
    return null;
  }

  return eventResult.data;
}

export function connectRealtime(props: ConnectRealtimeProps): Promise<void> {
  return fetchEventSource(`${apiBaseUrl}/organizations/${props.organizationId}/events`, {
    headers: {
      Accept: EventStreamContentType,
      Authorization: `Bearer ${props.accessToken}`,
    },
    signal: props.signal,
    onmessage(message) {
      if (message.event === 'connected') {
        return;
      }

      const event = parseRealtimeEvent(message);

      if (event) {
        props.onEvent(event);
      }
    },
    onclose() {
      throw new Error('Realtime connection closed unexpectedly.');
    },
    onerror(error) {
      throw error;
    },
  });
}
