<script setup lang="ts">
import Select from 'primevue/select';
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';
import enUSFlag from '../assets/flags/en-US.svg';
import ptBRFlag from '../assets/flags/pt-BR.svg';
import { setLocale } from '../i18n';
import { SUPPORTED_LOCALES, type AppLocale } from '../i18n/locale';

type LocalePresentation = {
  name: string;
  flag: string;
};

const localePresentation: Record<AppLocale, LocalePresentation> = {
  'pt-BR': { name: 'Português', flag: ptBRFlag },
  'en-US': { name: 'English', flag: enUSFlag },
};

const localeOptions = SUPPORTED_LOCALES.map((value) => ({
  value,
  ...localePresentation[value],
}));

const { locale, t } = useI18n();
const selectedLocale = computed<AppLocale>({
  get: () => locale.value as AppLocale,
  set: setLocale,
});
const selectedOption = computed(() => localeOptions.find(({ value }) => value === locale.value));
</script>

<template>
  <Select
    v-model="selectedLocale"
    class="language-selector"
    :options="localeOptions"
    option-label="name"
    option-value="value"
    :aria-label="t('common.languageSelector.label')"
  >
    <template #value>
      <span>{{ selectedOption?.name }}</span>
    </template>

    <template #option="{ option }">
      <div class="language-option">
        <img
          class="language-option__flag"
          :src="option.flag"
          alt=""
        />
        <span>{{ option.name }}</span>
      </div>
    </template>
  </Select>
</template>

<style scoped lang="scss">
.language-selector {
  width: 7.75rem;
  flex: 0 0 auto;
}

.language-selector :deep(.p-select-label) {
  padding-block: 0.45rem;
  padding-left: 0.65rem;
}

.language-selector :deep(.p-select-dropdown) {
  width: 2rem;
}

.language-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.language-option__flag {
  width: 1.25rem;
  height: 0.875rem;
  border-radius: 0.125rem;
  object-fit: cover;
}
</style>
