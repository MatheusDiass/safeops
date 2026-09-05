## Page Conventions

Pages are route-level orchestration components.

A page should make the flow of a screen understandable without containing all of its implementation details.

---

## Page Responsibilities

Pages may:

- read route parameters
- use router navigation
- invoke feature composables
- compose feature components
- coordinate loading
- coordinate create/update operations
- coordinate page-specific error handling
- decide which major UI states are displayed

Pages should not:

- call the raw HTTP client
- contain reusable utility functions
- contain large reusable form logic
- duplicate logic already available in a composable
- implement backend authorization rules
- render large repeated UI sections that deserve dedicated components

---

## Standard Page Names

When appropriate:

```text
XListPage.vue
XDetailsPage.vue
CreateXPage.vue
EditXPage.vue
```

Examples:

```text
IncidentListPage.vue
IncidentDetailsPage.vue
CreateIncidentPage.vue
EditIncidentPage.vue
```

Feature-specific workflows may use descriptive names instead.

Example:

```text
LoginPage.vue
ForgotPasswordPage.vue
```

---

## List Page

Typical responsibility:

```text
XListPage
    ↓
useXList
    ↓
XFilters
XTable / XList
```

Example:

```vue
<script setup lang="ts">
import { onMounted } from 'vue';

import IncidentTable from '../components/IncidentTable.vue';
import { useIncidentList } from '../composables/useIncidentList';

const { incidents, isLoading, error, load } = useIncidentList();

onMounted(load);
</script>

<template>
  <main>
    <h1>Incidents</h1>

    <p v-if="error">
      {{ error }}
    </p>

    <IncidentTable
      :incidents="incidents"
      :loading="isLoading"
    />
  </main>
</template>
```

The exact implementation may differ, but the page should remain focused on orchestration.

---

## Details Page

Typical structure:

```text
XDetailsPage
    ↓
useXDetails
    ↓
feature detail components
    ↓
feature actions
```

The details page may coordinate additional feature-specific composables such as:

```text
useIncidentStatus
```

Do not place every possible feature action inside `useXDetails`.

---

## Create Page

Typical structure:

```text
CreateXPage
    ↓
useXForm
    ↓
XForm
    ↓
feature API create operation
```

The page generally coordinates:

- form submission
- success navigation
- submission error state when not owned by the form behavior
- interaction with route context

---

## Edit Page

Typical structure:

```text
EditXPage
    ↓
useXDetails
    ↓
useXForm
    ↓
XForm
    ↓
feature API update operation
```

Reuse the same form component between create and edit when practical.

Avoid nearly identical `CreateXForm.vue` and `EditXForm.vue` components.

---

## Page Size

There is no strict line limit.

Review a page when:

- it becomes difficult to understand its screen flow
- multiple independent UI sections contain substantial markup
- reusable reactive logic appears
- API details leak into the page
- feature rules become mixed with rendering

Split by responsibility, not by arbitrary line count.
