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
AppButton.vue
AppModal.vue
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
type Props {
  incident: Incident;
}

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
AppInput
AppSelect
IncidentForm
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

## UI Components vs Feature Components

Domain-independent:

```text
src/components/ui/AppButton.vue
src/components/ui/AppInput.vue
```

Domain-specific:

```text
src/modules/incident/components/IncidentStatusBadge.vue
```

A reusable UI primitive must not import domain concepts.
