# SafeOps Web Architecture

## Overview

SafeOps Web uses a feature-oriented frontend architecture.

The goal is to keep code close to the domain feature it belongs to while maintaining a small set of shared application primitives.

The architecture favors:

- feature locality
- explicit dependencies
- predictable module structure
- simple Vue composition
- limited global state
- consistent UI composition
- gradual extraction of shared behavior

The frontend does not attempt to reproduce backend Clean Architecture layer by layer.

Frontend structure follows UI and feature responsibilities.

---

## Source Structure

```text
src/
├── app/
│   └── theme/
├── assets/
├── components/
├── layouts/
├── modules/
├── shared/
└── main.ts
```

---

## `app/`

Contains application-level infrastructure.

Example:

```text
app/
├── router/
│   └── index.ts
├── theme/
│   └── safeops.preset.ts
└── App.vue
```

Possible responsibilities include:

- Vue application configuration
- router configuration
- route guards
- plugin installation
- global application initialization
- application-wide PrimeVue theme configuration

Feature behavior must not be implemented here.

---

## `modules/`

Contains domain-oriented application features.

Examples:

```text
modules/
├── auth/
├── organization/
├── site/
└── incident/
```

A feature may contain:

```text
<feature>/
├── api/
├── components/
├── composables/
├── pages/
├── schemas/
└── types/
```

Only create directories that the feature actually needs.

Do not create empty directories merely to keep modules visually symmetrical.

Feature-specific code should remain inside the feature.

Example:

```text
modules/incident/components/IncidentStatusBadge.vue
```

This component should not be placed under `src/components/` because incident status belongs to the Incident feature.

---

## `components/`

Contains reusable SafeOps application-level components when genuine cross-feature reuse exists.

Examples:

```text
components/
├── PageHeader.vue
└── EmptyState.vue
```

Generic UI primitives come directly from PrimeVue. Do not require a `components/ui/` directory and do not add components such as `AppButton` or `AppInput` merely to proxy PrimeVue.

Feature-specific components remain inside their modules.

---

## UI Architecture

PrimeVue 4 in Styled Mode is the official UI foundation.

```text
SafeOps Theme
    ↓
PrimeVue Components
    ↓
SafeOps Feature Components
    ↓
Pages
```

PrimeVue provides generic controls. SafeOps components provide domain semantics or meaningful application-specific composition.

```text
IncidentForm
    ↓
PrimeVue InputText
PrimeVue Select
PrimeVue DatePicker
PrimeVue Button
```

```text
IncidentTable
    ↓
PrimeVue DataTable
```

The application-wide custom PrimeVue preset belongs under `src/app/theme/`, for example `src/app/theme/safeops.preset.ts`. It is based on an official PrimeVue preset and customized through design tokens.

Detailed UI, form presentation, validation presentation, and theming rules are documented in `docs/design-system.md`.

---

## `layouts/`

Contains reusable page shells.

Examples:

```text
layouts/
├── AuthLayout.vue
└── AppLayout.vue
```

Layouts may provide:

- navigation shell
- sidebar
- header
- page container
- authentication layout

Layouts should not implement feature-specific business behavior.

---

## `shared/`

Contains code genuinely reused across multiple features.

Possible structure:

```text
shared/
├── api/
├── composables/
├── types/
└── utils/
```

Examples:

```text
shared/api/http.ts
shared/composables/useDebounce.ts
shared/utils/date.ts
```

A feature implementation should not be moved here based only on predicted future reuse.

Prefer:

> _local first, shared after actual reuse._

---

## Dependency Direction

Preferred UI composition direction:

```text
SafeOps Theme
    ↓
PrimeVue Components
    ↓
SafeOps Feature Components
    ↓
Pages
```

Preferred behavior and data direction:

```text
Page
    ↓
Composable
    ↓
Feature API
    ↓
Shared HTTP Client
    ↓
SafeOps Backend
```

Shared application components and utilities may be consumed where appropriate.

This is not a strict layered architecture, but dependencies should remain understandable.

---

## Pages

Pages represent route-level screens.

Examples:

```text
IncidentListPage.vue
IncidentDetailsPage.vue
CreateIncidentPage.vue
EditIncidentPage.vue
```

Pages primarily coordinate:

- route parameters
- composables
- feature components
- navigation
- page-level actions

Pages should reveal the screen flow clearly when reading their template and script.

Detailed page rules are documented in `docs/pages.md`.

---

## Components

Components primarily represent UI responsibilities.

A component may contain:

- local UI state
- props
- emits
- models
- presentation-oriented computed values
- component-specific event handlers
- component-specific styles

Components should not become containers for unrelated application behavior.

Detailed rules are documented in `docs/components.md`.

---

## Composables

Composables encapsulate cohesive Vue reactive behavior.

Examples:

```text
useIncidentList
useIncidentDetails
useIncidentForm
useIncidentStatus
```

A composable is not a generic service container.

Avoid composables such as:

```text
useIncidents()
```

when they contain every action related to the feature.

Detailed rules are documented in `docs/composables.md`.

---

## API Layer

HTTP communication is isolated from components.

Example:

```text
modules/incident/api/incident.api.ts
```

It may expose:

```text
listIncidents()
getIncident()
createIncident()
updateIncident()
changeIncidentStatus()
```

The API layer represents communication with the SafeOps backend.

It does not manage UI state.

Detailed rules are documented in `docs/api.md`.

---

## Form Validation

SafeOps application forms that require validation and submission use **PrimeVue Forms** with **Zod** for schema-based validation.

The standard validation flow is:

```text
PrimeVue Components
        ↓
@primevue/forms
        ↓
zodResolver
        ↓
Zod Schema
        ↓
Feature API
        ↓
SafeOps Backend
```

### Responsibilities

Each layer has a distinct responsibility:

- **PrimeVue components** render form controls and validation feedback.
- **`@primevue/forms`** manages form values, field state, validation lifecycle, and submission.
- **Zod** defines client-side validation rules and transforms when appropriate.
- **`zodResolver`** adapts Zod validation results to PrimeVue Forms.
- **Feature APIs** map valid form data to backend requests.
- **The backend remains authoritative** for business rules, authorization, resource state, persistence constraints, and security-sensitive validation.

### Validation schemas

Validation schemas belong to the feature that owns the form.

Example:

```text
modules/
└── organization/
    ├── api/
    ├── pages/
    ├── schemas/
    │   └── organization.schema.ts
    └── types/
```

Create the `schemas/` directory only when the feature actually contains validation schemas.

Schemas should reuse shared field rules when Create and Update forms validate the same fields.

Schemas with user-facing validation messages must follow the translation dependency pattern documented in [Internationalization](#internationalization).

Avoid duplicating validation rules between related forms.

Do not introduce validation rules that are not part of the product or API contract merely because they seem reasonable. New domain constraints should be intentional and aligned with backend behavior.

### Form value types

When appropriate, form value types should be inferred from the Zod schema:

```ts
export type CreateOrganizationFormValues = z.infer<typeof createOrganizationSchema>;
```

Do not duplicate the same shape manually in both a Zod schema and a TypeScript type.

Form value types and API request types may remain separate when they represent different responsibilities or require mapping before submission.

### PrimeVue Forms

`@primevue/forms` is the standard form state and validation integration layer for application forms that require structured validation and submission.

Example:

```vue
<Form
  v-slot="$form"
  :initial-values="initialValues"
  :resolver="resolver"
  :validate-on-value-update="false"
  validate-on-blur
  validate-on-submit
  novalidate
  @submit="submit"
>
```

Fields should normally be registered through their `name` property:

```vue
<InputText name="name" :invalid="$form.name?.invalid" />
```

Validation feedback should be displayed close to the corresponding field.

```vue
<Message v-if="$form.name?.invalid">
  {{ $form.name.error?.message }}
</Message>
```

The visual treatment of invalid controls and validation messages must follow `docs/design-system.md`.

Avoid maintaining a separate `ref` or `v-model` for a field when PrimeVue Forms already owns that field's state, unless a component integration or specific UX requirement makes it necessary.

### Validation lifecycle

The default validation behavior is:

- do not validate every value change;
- validate on blur;
- validate on submit;
- prevent invalid forms from calling the API.

This provides validation feedback without displaying errors while the user is initially typing.

Individual fields may use different validation triggers when there is a clear UX reason.

### Submit handling

The submit handler should continue only when PrimeVue Forms reports a valid form.

Example:

```ts
const resolver = zodResolver(createOrganizationSchema(t));

function submit(event: FormSubmitEvent) {
  if (!event.valid) {
    return;
  }

  // Map form values to the API request and execute the operation.
}
```

Do not repeat schema validation manually inside the submit handler.

Submission handlers may map form values to API DTOs when the form model and backend request model differ.

### Frontend vs backend validation

Frontend validation improves user experience but does not replace backend validation.

Suitable frontend validation includes:

- required fields
- string length
- format validation
- allowed enum values
- simple date constraints when useful for UX
- client-side transformations such as trimming when they match the expected contract

Rules that depend on trusted server state remain on the backend, including:

- authorization
- organization membership
- roles and permissions
- resource existence
- resource ownership
- current persisted status
- cross-resource consistency
- security-sensitive business rules

Frontend authorization or validation must never be treated as a security boundary.

### API errors

Keep client-side field validation errors separate from API and business errors.

Zod handles field-level validation such as:

```text
Organization name is required.
```

The API handles server-side errors such as:

```text
ORGANIZATION_NOT_FOUND
ORGANIZATION_ACCESS_DENIED
ORGANIZATION_DISABLED
```

Do not convert every API error into a field validation error.

An API error should be attached to a field only when the backend explicitly reports a field-specific validation problem and the application has a defined mapping for it.

### General rules

- Prefer schema-based validation over manual `if` validation.
- Do not create one error `ref` per field.
- Do not use watchers to implement field validation.
- Do not duplicate schemas between Create and Update when rules can be shared.
- Do not introduce generic form abstractions prematurely.
- Keep validation schemas inside the owning feature.
- Keep validation messages clear, consistent, and user-friendly.
- Follow `docs/design-system.md` for validation presentation and visual states.
- Keep backend validation authoritative.

---

## Internationalization

SafeOps uses `vue-i18n`. The supported locales are `pt-BR` and `en-US`; `pt-BR` is the default locale and `en-US` is the fallback locale.

Locale definitions and global i18n configuration are centralized under `src/i18n`. Shared translations belong in:

```text
src/i18n/shared/
├── pt-BR.ts
└── en-US.ts
```

Feature-specific translations remain with the owning feature:

```text
src/modules/<feature>/i18n/
├── pt-BR.ts
└── en-US.ts
```

The current examples are `src/modules/auth/i18n` and `src/modules/organization/i18n`. Their translation namespaces are `auth` and `organizations`, respectively.

Translation keys describe meaning and context rather than copying displayed text. Examples include:

```text
common.actions.save
auth.login.title
organizations.fields.name.label
organizations.validation.nameRequired
organizations.status.ACTIVE
```

Displayed Portuguese or English text must not be used as a translation key.

### API values

API enum and domain values remain unchanged and are translated only in the presentation layer.

```ts
t(`organizations.status.${organization.status}`);
```

Never translate enum or domain values before sending them to the backend.

### Validation

Zod schemas that need translated validation messages receive the translation function as a dependency. They do not import the global i18n instance.

Prefer resolving the message when the validation error occurs so it uses the current locale:

```ts
type Translate = (key: string) => string;

export function createOrganizationSchema(t: Translate) {
  return z.object({
    name: z
      .string()
      .trim()
      .min(3, {
        error: () => t('organizations.validation.nameMin'),
      }),
  });
}
```

### Locale selection

Locale selection, browser detection, fallback behavior, and persistence are handled by the centralized `src/i18n` locale layer. UI components use the existing locale utilities and must not implement their own persistence logic or access `localStorage` directly.

The language selector displays each language using its native name, independent of the current application locale:

```text
Português
English
```

The Brazil and United States flags may be used as visual representations of the currently supported `pt-BR` and `en-US` locales. The locale value remains the authoritative selection.

---

## Pinia

Pinia represents shared application state, not every piece of reactive state.

Good candidates include:

- authenticated user
- application-wide organization context
- application-wide selected site when required across unrelated screens

Poor candidates include:

- modal visibility
- form values
- page-specific loading state
- temporary filters used only by one screen

Prefer local state until a genuine cross-screen requirement exists.

---

## Domain Boundaries

Frontend modules should reflect SafeOps domain terminology.

Prefer:

```text
auth
organization
site
incident
```

instead of technical groupings such as:

```text
forms
tables
services
models
```

at the application root.

Technical folders may exist inside individual features.

---

## Reuse Strategy

Use three levels of reuse.

### Feature local

Default location.

Example:

```text
modules/incident/components/IncidentCard.vue
```

### Cross-feature shared

Extract only when multiple features genuinely need the behavior.

Example:

```text
shared/composables/useDebounce.ts
```

### Generic UI primitive

Use the corresponding PrimeVue component directly.

Examples include `Button`, `InputText`, `Select`, `Dialog`, and `DataTable`.

Do not create an application wrapper that only renames or forwards a PrimeVue component API.

Avoid premature extraction.

---

## Authorization

Authorization rules are enforced by the backend.

The frontend may use permission information to:

- hide unavailable actions
- disable unavailable controls
- improve navigation
- improve user feedback

Frontend authorization must never be treated as a security boundary.

---

## Architectural Change

A change is architectural when it introduces or significantly changes:

- module structure
- dependency direction
- global state strategy
- router strategy
- HTTP strategy
- form strategy
- validation strategy
- UI component strategy
- testing strategy

Architectural changes should be intentional and documented.

Do not introduce a competing architecture inside an individual feature.
