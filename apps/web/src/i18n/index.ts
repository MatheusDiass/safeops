import { watch } from 'vue';
import { createI18n } from 'vue-i18n';
import authEnUS from '../modules/auth/i18n/en-US';
import authPtBR from '../modules/auth/i18n/pt-BR';
import organizationsEnUS from '../modules/organization/i18n/en-US';
import organizationsPtBR from '../modules/organization/i18n/pt-BR';
import sitesEnUS from '../modules/site/i18n/en-US';
import sitesPtBR from '../modules/site/i18n/pt-BR';
import sharedEnUS from './shared/en-US';
import sharedPtBR from './shared/pt-BR';
import {
  DEFAULT_LOCALE,
  FALLBACK_LOCALE,
  LOCALE_STORAGE_KEY,
  SUPPORTED_LOCALES,
  type AppLocale,
} from './locale';

function isSupportedLocale(locale: string | null): locale is AppLocale {
  return SUPPORTED_LOCALES.some((supportedLocale) => supportedLocale === locale);
}

function detectLocale(): AppLocale {
  const savedLocale = localStorage.getItem(LOCALE_STORAGE_KEY);

  if (isSupportedLocale(savedLocale)) {
    return savedLocale;
  }

  for (const browserLocale of navigator.languages) {
    const language = browserLocale.toLowerCase();

    if (language.startsWith('pt')) {
      return 'pt-BR';
    }

    if (language.startsWith('en')) {
      return 'en-US';
    }
  }

  return DEFAULT_LOCALE;
}

export const i18n = createI18n({
  legacy: false,
  locale: detectLocale(),
  fallbackLocale: FALLBACK_LOCALE,
  messages: {
    'pt-BR': {
      ...sharedPtBR,
      ...authPtBR,
      ...organizationsPtBR,
      ...sitesPtBR,
    },
    'en-US': {
      ...sharedEnUS,
      ...authEnUS,
      ...organizationsEnUS,
      ...sitesEnUS,
    },
  },
});

export function setLocale(locale: AppLocale): void {
  i18n.global.locale.value = locale;
}

watch(
  i18n.global.locale,
  (locale) => {
    localStorage.setItem(LOCALE_STORAGE_KEY, locale);
    document.documentElement.lang = locale;
  },
  { immediate: true },
);
