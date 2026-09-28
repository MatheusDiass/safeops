import { z } from 'zod';

export const REALTIME_EVENT_TYPES = [
  'incident.created',
  'incident.updated',
  'incident.status-updated',
] as const;

export const realtimeEventSchema = z.object({
  eventId: z.uuid(),
  eventType: z.enum(REALTIME_EVENT_TYPES),
  organizationId: z.uuid(),
  siteId: z.uuid(),
  resourceId: z.uuid(),
  occurredAt: z.iso.datetime({ offset: true }),
});

export type RealtimeEvent = z.infer<typeof realtimeEventSchema>;
