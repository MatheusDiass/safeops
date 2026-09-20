# Composable Conventions

## Purpose

This document defines how SafeOps uses Vue composables.

The goal is to keep page-specific reactive behavior cohesive, predictable, and local to the feature that owns it.

Pinia store rules are documented separately in `docs/stores.md`.

---

## Responsibility boundaries

### Feature API

A feature API contains HTTP details only:

- endpoint paths;
- HTTP methods;
- request DTOs;
- response DTOs.

It must not contain Vue state, routing, notifications, form state, or translations.

### Composable

A composable encapsulates a cohesive reactive UI workflow, such as:

- loading a list or details;
- tracking loading and submission state;
- protecting against stale asynchronous responses;
- exposing display-ready operation errors;
- coordinating reactive inputs and watchers;
- submitting a create or update form.

A composable is not required for every API operation. Do not create a composable that only renames or forwards an API or store method without adding meaningful reactive behavior or orchestration.

## Naming

Name feature composables using:

```text
use{Feature}{Responsibility}
```

Examples:

```text
useSiteList
useSiteDetails
useSiteCreateForm
useSiteUpdateForm

useOrganizationList
useOrganizationDetails
useOrganizationCreateForm
useOrganizationUpdateForm

useIncidentList
useIncidentDetails
useIncidentCreateForm
useIncidentUpdateForm
useIncidentStatus
```

Put reusable cross-feature composables under `src/shared/composables/`, for example `useDebounce`.

Do not use ambiguous feature composable names such as `useSite`, `useSites`, or `useSiteService`.

---

## Standard return names

Use domain-specific state names instead of generic `data`.

### List

```ts
{
  sites,
  isLoading,
  errorMessage,
  load,
}
```

### Details

```ts
{
  site,
  isLoading,
  errorMessage,
  load,
}
```

### Create or update form

```ts
{
  isSubmitting,
  errorMessage,
  submit,
}
```

Use:

- `isLoading` for read operations;
- `isSubmitting` for form mutations;
- `isUpdating` or `isDeleting` for non-form operations when those names are more precise;
- `errorMessage` for a translated, display-ready operation error;
- `load` for list and details loading;
- `submit` for form submission.

Represent an explicitly empty error or single-resource state with `null`:

```ts
const site = ref<Site | null>(null);
const errorMessage = ref<string | null>(null);
```

---

## Site module

Site state is currently page-scoped. Site composables call `siteApi` directly:

```text
Page
  → Site composable
    → siteApi
      → shared HTTP client
```

The established Site composables are:

```text
useSiteList
useSiteDetails
useSiteCreateForm
useSiteUpdateForm
```

Do not add a Site Pinia store merely for structural symmetry. Add one only if site state must later be shared across unrelated screens, layouts, or application-wide workflows.

### Overlapping reads

When `load` may run again before an earlier request finishes, use a monotonically increasing request identifier:

```ts
let latestRequestId = 0;

async function load(organizationId: string | null): Promise<void> {
  const requestId = ++latestRequestId;

  // Execute request.

  if (requestId === latestRequestId) {
    // Apply only the newest result.
  }
}
```

Apply the same check before changing data, error, and loading state. This does not cancel the older HTTP request; it prevents its stale result from overwriting the latest state.

### Form mutations

Use an `isSubmitting` guard instead of a request identifier when only one create or update operation may run at a time:

```ts
if (isSubmitting.value) {
  return false;
}
```

A form composable may return `boolean` when the caller only needs to decide whether to continue, such as navigating after success. Return the created or updated entity only when the caller needs its ID or response data.

---

## Organization composables

Organization composables add page-specific reactive behavior around the shared Organization store.

The Organization data flow is:

```text
Page
  → Organization composable
    → Organization store
      → organizationApi
        → shared HTTP client
```

Organization composables own:

- page-specific `isLoading` and `errorMessage`;
- search and filtered results;
- route and reactive-input watchers;
- form-specific `isSubmitting`;
- the submission workflow exposed to the page.

The intended Organization composables are:

```text
useOrganizationList
useOrganizationDetails
useOrganizationCreateForm
useOrganizationUpdateForm
```

Create them as the corresponding page workflows are implemented. Do not add empty files in advance.

Store ownership, synchronization, and organization freshness rules are documented in `docs/stores.md`.

---

## Component integration

Use `handleSubmit` for the PrimeVue Form event handler. Keep `submit` as the standard operation exposed by a form composable and alias it to the domain action when that improves clarity:

```ts
const { submit: createSite, isSubmitting, errorMessage } = useSiteCreateForm();

async function handleSubmit(event: FormSubmitEvent): Promise<void> {
  if (!event.valid) {
    return;
  }

  const wasCreated = await createSite(organizationId, {
    name: event.values.name,
  });

  if (!wasCreated) {
    return;
  }

  await router.replace({ name: 'sites' });
}
```

Pages normally own routing and success notifications. Composables own their asynchronous operation state and display-ready operation error.

---

## Async edit-form initialization

Do not render an edit form with placeholder initial values while its resource is still loading.

PrimeVue Forms initial values must represent a real loaded snapshot. Prefer a nullable initial-values ref:

```ts
const initialValues = ref<UpdateSiteFormValues | null>(null);
```

Populate it only after a successful load:

```ts
async function loadSite(): Promise<void> {
  initialValues.value = null;

  await load(organizationId, siteId);

  if (!site.value) {
    return;
  }

  initialValues.value = {
    name: site.value.name,
    status: site.value.status,
  };
}
```

Render the form only when the snapshot exists:

```vue
<Form
  v-if="initialValues"
  :key="siteId"
  :initial-values="initialValues"
  @submit="handleSubmit"
>
```

Do not use fallback edit values such as an empty name or `ACTIVE` status merely to mount the form before the API response arrives. Loading completion and form readiness are separate concerns.

---

## Decision checklist

Before adding a composable, ask:

1. Does it encapsulate cohesive reactive state or orchestration?
2. Does it simplify a page without becoming a generic feature service?
3. Does it add more than a one-line pass-through to an API or store?

If the answers are no, call the existing API or store action from the page instead.
