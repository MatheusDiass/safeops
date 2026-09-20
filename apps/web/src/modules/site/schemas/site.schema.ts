import { z } from 'zod';
import { SITE_STATUSES } from '../types/site.types';

type Translate = (key: string) => string;

function siteFieldsSchema(t: Translate) {
  const siteNameSchema = z
    .string()
    .trim()
    .min(1, {
      error: () => t('sites.validation.nameRequired'),
    })
    .min(3, {
      error: () => t('sites.validation.nameMin'),
    })
    .max(150, {
      error: () => t('sites.validation.nameMax'),
    });

  return z.object({
    name: siteNameSchema,
  });
}

export function createSiteSchema(t: Translate) {
  return siteFieldsSchema(t);
}

export function updateSiteSchema(t: Translate) {
  return siteFieldsSchema(t).extend({
    status: z.enum(SITE_STATUSES, {
      error: () => t('sites.validation.statusRequired'),
    }),
  });
}

export type CreateSiteFormValues = z.infer<ReturnType<typeof createSiteSchema>>;
export type UpdateSiteFormValues = z.infer<ReturnType<typeof updateSiteSchema>>;
