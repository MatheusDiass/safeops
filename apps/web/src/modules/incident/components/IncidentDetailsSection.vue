<script setup lang="ts">
import type { FormFieldState } from '@primevue/forms';
import DatePicker from 'primevue/datepicker';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import Select from 'primevue/select';
import SelectButton from 'primevue/selectbutton';
import Textarea from 'primevue/textarea';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import {
  INCIDENT_SEVERITIES,
  INCIDENT_TYPES,
  type IncidentSeverity,
  type IncidentType,
} from '../types/incident.types';

type Props = {
  form: Record<string, FormFieldState | undefined>;
  isSubmitting: boolean;
};

type Option<T> = {
  label: string;
  value: T;
};

defineProps<Props>();

const { t } = useI18n();
const maxOccurredAt = ref(new Date());
const typeOptions = computed<Option<IncidentType>[]>(() =>
  INCIDENT_TYPES.map((value) => ({
    label: t(`incidents.type.${value}`),
    value,
  })),
);
const severityOptions = computed<Option<IncidentSeverity>[]>(() =>
  INCIDENT_SEVERITIES.map((value) => ({
    label: t(`incidents.severity.${value}`),
    value,
  })),
);

function refreshOccurredAtLimit(): void {
  maxOccurredAt.value = new Date();
}
</script>

<template>
  <section class="form-card">
    <header class="form-card__header">
      <h2>{{ t('incidents.create.sections.details') }}</h2>
    </header>

    <div class="form-grid">
      <div class="field form-grid__wide">
        <label for="incident-title">
          {{ t('incidents.create.fields.title.label') }}
          <span aria-hidden="true">*</span>
        </label>
        <InputText
          id="incident-title"
          name="title"
          maxlength="150"
          autocomplete="off"
          :placeholder="t('incidents.create.fields.title.placeholder')"
          :disabled="isSubmitting"
          :invalid="form.title?.invalid"
          :aria-invalid="form.title?.invalid"
          aria-required="true"
          :aria-describedby="form.title?.invalid ? 'incident-title-error' : undefined"
          fluid
        />
        <Message
          v-if="form.title?.invalid"
          id="incident-title-error"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ form.title.error?.message }}
        </Message>
      </div>

      <div class="field">
        <label for="incident-type">
          {{ t('incidents.create.fields.type.label') }}
          <span aria-hidden="true">*</span>
        </label>
        <Select
          input-id="incident-type"
          name="type"
          :options="typeOptions"
          option-label="label"
          option-value="value"
          :placeholder="t('incidents.create.fields.type.placeholder')"
          :disabled="isSubmitting"
          :invalid="form.type?.invalid"
          :aria-invalid="form.type?.invalid"
          aria-required="true"
          :aria-describedby="form.type?.invalid ? 'incident-type-error' : undefined"
          fluid
        />
        <Message
          v-if="form.type?.invalid"
          id="incident-type-error"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ form.type.error?.message }}
        </Message>
      </div>

      <div class="field">
        <label for="incident-occurred-at">
          {{ t('incidents.create.fields.occurredAt.label') }}
          <span aria-hidden="true">*</span>
        </label>
        <DatePicker
          input-id="incident-occurred-at"
          name="occurredAt"
          show-time
          show-icon
          hour-format="24"
          :max-date="maxOccurredAt"
          :disabled="isSubmitting"
          :invalid="form.occurredAt?.invalid"
          :aria-invalid="form.occurredAt?.invalid"
          aria-required="true"
          :aria-describedby="form.occurredAt?.invalid ? 'incident-occurred-at-error' : undefined"
          fluid
          @show="refreshOccurredAtLimit"
        />
        <Message
          v-if="form.occurredAt?.invalid"
          id="incident-occurred-at-error"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ form.occurredAt.error?.message }}
        </Message>
      </div>

      <fieldset class="field severity-field form-grid__wide">
        <legend>{{ t('incidents.create.fields.severity.label') }}</legend>
        <SelectButton
          name="severity"
          :options="severityOptions"
          option-label="label"
          option-value="value"
          :disabled="isSubmitting"
          :aria-describedby="
            form.severity?.invalid ? 'incident-severity-error' : 'incident-severity-hint'
          "
        />
        <Message
          v-if="form.severity?.invalid"
          id="incident-severity-error"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ form.severity.error?.message }}
        </Message>
        <small
          v-else
          id="incident-severity-hint"
        >
          {{ t('incidents.create.fields.severity.hint') }}
        </small>
      </fieldset>

      <div class="field form-grid__wide">
        <label for="incident-description">
          {{ t('incidents.create.fields.description.label') }}
          <span aria-hidden="true">*</span>
        </label>
        <Textarea
          id="incident-description"
          name="description"
          rows="5"
          maxlength="3000"
          auto-resize
          :placeholder="t('incidents.create.fields.description.placeholder')"
          :disabled="isSubmitting"
          :invalid="form.description?.invalid"
          :aria-invalid="form.description?.invalid"
          aria-required="true"
          :aria-describedby="
            form.description?.invalid ? 'incident-description-error' : 'incident-description-hint'
          "
          fluid
        />
        <Message
          v-if="form.description?.invalid"
          id="incident-description-error"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ form.description.error?.message }}
        </Message>
        <small
          v-else
          id="incident-description-hint"
        >
          {{ t('incidents.create.fields.description.hint') }}
        </small>
      </div>

      <div class="field form-grid__wide">
        <label for="incident-immediate-actions">
          {{ t('incidents.create.fields.immediateActions.label') }}
        </label>
        <Textarea
          id="incident-immediate-actions"
          name="immediateActions"
          rows="4"
          maxlength="5000"
          auto-resize
          :placeholder="t('incidents.create.fields.immediateActions.placeholder')"
          :disabled="isSubmitting"
          :invalid="form.immediateActions?.invalid"
          :aria-invalid="form.immediateActions?.invalid"
          :aria-describedby="
            form.immediateActions?.invalid ? 'incident-immediate-actions-error' : undefined
          "
          fluid
        />
        <Message
          v-if="form.immediateActions?.invalid"
          id="incident-immediate-actions-error"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ form.immediateActions.error?.message }}
        </Message>
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

.form-grid__wide {
  grid-column: 1 / -1;
}

.field {
  display: grid;
  min-width: 0;
  align-content: start;
  gap: 0.5rem;
}

.field label,
.field legend {
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

.severity-field {
  padding: 0;
  border: 0;
  margin: 0;
}

.severity-field legend {
  padding: 0;
  margin-bottom: 0.5rem;
}

.severity-field :deep(.p-selectbutton) {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.severity-field :deep(.p-togglebutton) {
  justify-content: center;
}

@media (max-width: 700px) {
  .form-grid {
    grid-template-columns: minmax(0, 1fr);
  }

  .form-grid__wide {
    grid-column: auto;
  }

  .severity-field :deep(.p-selectbutton) {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
