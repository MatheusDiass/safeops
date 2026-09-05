## Component Conventions

Vue components should have one primary UI responsibility.

---

## File Naming

Use PascalCase.

Examples:

```text
IncidentCard.vue
IncidentForm.vue
IncidentStatusBadge.vue
PageHeader.vue
EmptyState.vue
```

Prefer descriptive multi-word names.

Avoid:

```text
Card.vue
Form.vue
Item.vue
Modal.vue
```

when the component has domain meaning.

---

## SFC Structure

Use:

```vue
<script setup lang="ts"></script>

<template></template>

<style scoped lang="scss"></style>
```

Inside `<script setup>`, prefer this order:

```text
imports
local types
props
models
emits
dependencies
state
computed
watchers
handlers/functions
lifecycle
```

Do not add empty section comments solely to represent this order.

---

## What Belongs Inside a Component

Keep component-specific behavior inside the component.

Examples:

- showing or hiding a password
- opening a component-owned dropdown
- emitting selection events
- presentation-oriented computed values
- component-specific lifecycle behavior

Example:

```ts
const showPassword = ref(false);

function togglePasswordVisibility() {
  showPassword.value = !showPassword.value;
}
```

---

## What Usually Belongs Elsewhere

### HTTP communication

Move to:

```text
api/
```

### Reusable reactive behavior

Move to:

```text
composables/
```

### Pure reusable functions

Move to:

```text
shared/utils/
```

or feature-local utilities when domain-specific.

### Cross-feature state

Consider Pinia.

### Complex reusable domain behavior

Keep outside visual components.

---

## Props

Props represent information supplied by the parent.

Prefer strongly typed props.

Example:

```ts
type Props = {
  incident: Incident;
};

defineProps<Props>();
```

Avoid very large unrelated prop lists.

Passing a domain object is appropriate when the component conceptually represents that object.

---

## Emits

Use events to communicate child interactions to the parent.

Example:

```ts
const emit = defineEmits<{
  select: [incidentId: string];
}>();
```

Prefer events representing user intent:

```text
select
submit
cancel
close
change-status
```

rather than events exposing implementation details.

---

## `defineModel`

Use `defineModel` when the component naturally represents an editable value.

Good examples:

```text
SiteSelector
OrganizationSelector
```

Avoid using two-way binding when explicit props/events make ownership clearer.

---

## Component Extraction

Extract a child component when at least one is true:

- it has an independent UI responsibility
- it is reused
- it has meaningful internal interaction
- extracting it makes the parent substantially clearer

Do not extract components only because markup exceeds a particular number of lines.

Avoid excessive fragmentation such as:

```text
LoginTitle.vue
LoginSubtitle.vue
LoginButtonText.vue
```

when these elements have no independent responsibility.

---

## PrimeVue as the Generic UI Source

Before creating a generic interactive or visual component, check whether PrimeVue already provides it.

Prefer direct use:

```vue
<Button />
<InputText />
<Select />
<AutoComplete />
<Dialog />
```

instead of application wrappers that add no meaningful behavior:

```vue
<AppButton />
<AppInput />
<AppSelect />
<AppAutocomplete />
<AppDialog />
```

PrimeVue provides generic UI primitives. Feature components and reusable application components should add SafeOps-specific semantics, behavior, composition, reuse, or presentation logic.

---

## PrimeVue Wrappers

Bad:

```text
AppButton
    → PrimeVue Button
```

This wrapper is not useful when it only forwards PrimeVue props, slots, and events or merely renames the component. Do not create wrappers solely to theoretically isolate PrimeVue.

Good:

```text
IncidentStatusBadge
    → PrimeVue Tag
```

`IncidentStatusBadge` is useful when it maps SafeOps incident statuses to consistent labels, severity, icons, and accessible presentation.

Feature-specific components belong under:

```text
src/modules/<feature>/components/
```

Reusable SafeOps application-level components may belong under `src/components/` when they are genuinely used across features.

---

## Component Styling

SCSS remains valid for custom component-specific presentation and layout. Prefer PrimeVue semantic and component design tokens before adding SCSS overrides, and avoid styling PrimeVue through its internal CSS classes when tokens can express the intended result.

See `docs/design-system.md` for the complete styling priority and UI conventions.
