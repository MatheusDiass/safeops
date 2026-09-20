export const SUPPORTED_LOCALES = ['pt-BR', 'en-US'] as const;

export type AppLocale = (typeof SUPPORTED_LOCALES)[number];

export const DEFAULT_LOCALE: AppLocale = 'pt-BR';
export const FALLBACK_LOCALE: AppLocale = 'en-US';

export const LOCALE_STORAGE_KEY = 'safeops.locale';
