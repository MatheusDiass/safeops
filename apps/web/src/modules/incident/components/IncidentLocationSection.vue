<script setup lang="ts">
import type { FormFieldState } from '@primevue/forms';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import ProgressSpinner from 'primevue/progressspinner';
import Select from 'primevue/select';
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';
import type { Site } from '../../site/types/site.types';

type Props = {
  form: Record<string, FormFieldState | undefined>;
  sites: Site[];
  areSitesLoading: boolean;
  siteErrorMessage: string | null;
  hasSelectedOrganization: boolean;
  isSubmitting: boolean;
};

const props = defineProps<Props>();
const { t } = useI18n();

const siteOptions = computed(() =>
  props.sites.map((site) => ({
    label: site.name,
    value: site.id,
    disabled: site.status !== 'ACTIVE',
  })),
);
const hasActiveSites = computed(() => props.sites.some(({ status }) => status === 'ACTIVE'));
</script>

<template>
  <section class="form-card">
    <header class="form-card__header">
      <h2>{{ t('incidents.create.sections.location') }}</h2>
    </header>

    <div class="form-grid">
      <div class="field">
        <label for="incident-site">
          {{ t('incidents.create.fields.site.label') }}
          <span aria-hidden="true">*</span>
        </label>
        <div
          v-if="areSitesLoading"
          class="field-loading"
          role="status"
        >
          <ProgressSpinner stroke-width="6" />
          <span>{{ t('incidents.create.sites.loading') }}</span>
        </div>
        <Select
          v-else
          input-id="incident-site"
          name="siteId"
          :options="siteOptions"
          option-label="label"
          option-value="value"
          option-disabled="disabled"
          :placeholder="t('incidents.create.fields.site.placeholder')"
          :disabled="isSubmitting || !hasSelectedOrganization || !hasActiveSites"
          :invalid="form.siteId?.invalid"
          :aria-invalid="form.siteId?.invalid"
          aria-required="true"
          :aria-describedby="form.siteId?.invalid ? 'incident-site-error' : undefined"
          fluid
        />
        <Message
          v-if="form.siteId?.invalid"
          id="incident-site-error"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ form.siteId.error?.message }}
        </Message>
        <Message
          v-else-if="siteErrorMessage"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ siteErrorMessage }}
        </Message>
        <small v-else-if="!areSitesLoading && hasSelectedOrganization && !hasActiveSites">
          {{ t('incidents.create.sites.empty') }}
        </small>
      </div>

      <div class="field">
        <label for="incident-location">
          {{ t('incidents.create.fields.location.label') }}
        </label>
        <InputText
          id="incident-location"
          name="location"
          maxlength="150"
          autocomplete="off"
          :disabled="isSubmitting"
          :invalid="form.location?.invalid"
          :aria-invalid="form.location?.invalid"
          :aria-describedby="
            form.location?.invalid ? 'incident-location-error' : 'incident-location-hint'
          "
          fluid
        />
        <Message
          v-if="form.location?.invalid"
          id="incident-location-error"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ form.location.error?.message }}
        </Message>
        <small
          v-else
          id="incident-location-hint"
        >
          {{ t('incidents.create.fields.location.hint') }}
        </small>
      </div>
    </div>
  </section>
</template>

<style scoped lang="scss">
.form-card {
  padding: clamp(1.25rem, 3vw, 2rem);
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-border-radius-lg);
  background: var(--p-content-background);
  box-shadow: 0 1px 3px color-mix(in srgb, var(--p-surface-900) 7%, transparent);
}

.form-card__header {
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--p-content-border-color);
  margin-bottom: 1.25rem;
}

.form-card__header h2 {
  margin: 0;
  font-size: 1.05rem;
  letter-spacing: -0.015em;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 1.25rem;
}

.field {
  display: grid;
  min-width: 0;
  align-content: start;
  gap: 0.5rem;
}

.field label {
  color: var(--p-text-color);
  font-size: 0.875rem;
  font-weight: 600;
}

.field label span {
  color: var(--p-red-600);
}

.field small {
  color: var(--p-text-muted-color);
  line-height: 1.4;
}

.field-loading {
  display: flex;
  min-height: 2.625rem;
  align-items: center;
  gap: 0.625rem;
  color: var(--p-text-muted-color);
  font-size: 0.875rem;
}

.field-loading :deep(.p-progressspinner) {
  width: 1.25rem;
  height: 1.25rem;
}

@media (max-width: 700px) {
  .form-grid {
    grid-template-columns: minmax(0, 1fr);
  }
}
</style>
