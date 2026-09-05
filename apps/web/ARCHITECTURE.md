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

# Source Structure

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

# `app/`

Contains application-level infrastructure.

Examples:

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

# `modules/`

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
└── types/
```

Only create directories that the feature actually needs.

Do not create empty directories merely to keep modules visually symmetrical.

Feature-specific code should remain inside the feature.

Example:

```text
modules/incident/components/IncidentStatusBadge.vue
```

should not be placed under `src/components/` because the concept of incident status belongs to the Incidents feature.

---

# `components/`

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

# UI Architecture

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

Detailed UI and theming rules are documented in `docs/design-system.md`.

---

# `layouts/`

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

# `shared/`

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

> local first, shared after actual reuse.

---

# Dependency Direction

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

# Pages

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

Detailed page rules are documented in:

`docs/pages.md`

---

# Components

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

Detailed rules are documented in:

`docs/components.md`

---

# Composables

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

Detailed rules are documented in:

`docs/composables.md`

---

# API Layer

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

Detailed rules are documented in:

`docs/api.md`

---

# Pinia

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

# Domain Boundaries

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

# Reuse Strategy

Use three levels of reuse.

## Feature local

Default location.

Example:

```text
modules/incident/components/IncidentCard.vue
```

## Cross-feature shared

Extract only when multiple features genuinely need the behavior.

Example:

```text
shared/composables/useDebounce.ts
```

## Generic UI primitive

Use the corresponding PrimeVue component directly.

Examples include `Button`, `InputText`, `Select`, `Dialog`, and `DataTable`.

Do not create an application wrapper that only renames or forwards a PrimeVue component API.

Avoid premature extraction.

---

# Authorization

Authorization rules are enforced by the backend.

The frontend may use permission information to:

- hide unavailable actions
- disable unavailable controls
- improve navigation
- improve user feedback

Frontend authorization must never be treated as a security boundary.

---

# Architectural Change

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
