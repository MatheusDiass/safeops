import { z } from 'zod';
import { ORGANIZATION_STATUSES } from '../types/organization.types';

const organizationNameSchema = z
  .string()
  .trim()
  .min(1, 'Organization name is required.')
  .min(3, 'Organization name must have at least 3 characters.')
  .max(150, 'Organization name must have at most 150 characters.');

const organizationFieldsSchema = z.object({
  name: organizationNameSchema,
});

export const createOrganizationSchema = organizationFieldsSchema;

export const updateOrganizationSchema = organizationFieldsSchema.extend({
  status: z.enum(ORGANIZATION_STATUSES, {
    error: 'Organization status is required.',
  }),
});

export type CreateOrganizationFormValues = z.infer<typeof createOrganizationSchema>;
export type UpdateOrganizationFormValues = z.infer<typeof updateOrganizationSchema>;
