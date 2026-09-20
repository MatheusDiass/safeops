import { z } from 'zod';
import { ORGANIZATION_STATUSES } from '../types/organization.types';

type Translate = (key: string) => string;

function organizationFieldsSchema(t: Translate) {
  const organizationNameSchema = z
    .string()
    .trim()
    .min(1, {
      error: () => t('organizations.validation.nameRequired'),
    })
    .min(3, {
      error: () => t('organizations.validation.nameMin'),
    })
    .max(150, {
      error: () => t('organizations.validation.nameMax'),
    });

  return z.object({
    name: organizationNameSchema,
  });
}

export function createOrganizationSchema(t: Translate) {
  return organizationFieldsSchema(t);
}

export function updateOrganizationSchema(t: Translate) {
  return organizationFieldsSchema(t).extend({
    status: z.enum(ORGANIZATION_STATUSES, {
      error: () => t('organizations.validation.statusRequired'),
    }),
  });
}

export type CreateOrganizationFormValues = z.infer<ReturnType<typeof createOrganizationSchema>>;
export type UpdateOrganizationFormValues = z.infer<ReturnType<typeof updateOrganizationSchema>>;
