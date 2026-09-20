import { z } from 'zod';

type Translate = (key: string) => string;

export function createSiteSchema(t: Translate) {
  return z.object({
    name: z
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
      }),
  });
}

export type CreateSiteFormValues = z.infer<ReturnType<typeof createSiteSchema>>;
