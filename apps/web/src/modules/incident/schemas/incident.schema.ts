import { z } from 'zod';
import { INCIDENT_SEVERITIES, INCIDENT_TYPES } from '../types/incident.types';

type Translate = (key: string) => string;

export function createIncidentSchema(t: Translate) {
  return z.object({
    siteId: z.string().min(1, {
      error: () => t('incidents.validation.siteRequired'),
    }),
    location: z
      .string()
      .trim()
      .max(150, {
        error: () => t('incidents.validation.locationMax'),
      }),
    title: z
      .string()
      .trim()
      .min(1, {
        error: () => t('incidents.validation.titleRequired'),
      })
      .max(150, {
        error: () => t('incidents.validation.titleMax'),
      }),
    type: z.enum(INCIDENT_TYPES, {
      error: () => t('incidents.validation.typeRequired'),
    }),
    occurredAt: z
      .date({
        error: () => t('incidents.validation.occurredAtRequired'),
      })
      .refine((value) => value.getTime() <= Date.now(), {
        error: () => t('incidents.validation.occurredAtFuture'),
      }),
    severity: z.enum(INCIDENT_SEVERITIES).nullable(),
    description: z
      .string()
      .trim()
      .min(1, {
        error: () => t('incidents.validation.descriptionRequired'),
      })
      .max(3000, {
        error: () => t('incidents.validation.descriptionMax'),
      }),
    immediateActions: z
      .string()
      .trim()
      .max(5000, {
        error: () => t('incidents.validation.immediateActionsMax'),
      }),
  });
}

export type CreateIncidentFormValues = z.infer<ReturnType<typeof createIncidentSchema>>;
