# SafeOps Web Agent Guide

## Purpose

SafeOps Web is the frontend application for the SafeOps safety management platform.

This repository follows a feature-oriented Vue 3 architecture with strong conventions for pages, components, composables, API access, forms, validation, and TypeScript.

This file defines repository-wide instructions for coding agents.

Detailed architectural and implementation rules live in the project documentation.

---

## Technology Stack

Use the existing project stack:

- Vue 3
- TypeScript
- Vite
- Vue Router
- Pinia
- PrimeVue 4 in Styled Mode
- Composition API
- `<script setup lang="ts">`
- Axios
- SCSS
- npm
- `@primevue/forms`
- Zod

Do not introduce another frontend framework, router, state-management library, HTTP library, form library, validation library, UI library, or major dependency unless explicitly requested or supported by an intentional architectural decision.

Prefer existing project dependencies and native Vue/browser capabilities.

---

## Source of Truth

Before modifying code:

1. inspect the relevant existing implementation;
2. read the documentation related to the task;
3. search for an existing implementation that already serves the same role.

Architecture:

- `ARCHITECTURE.md`

Implementation conventions:

- `docs/modules.md`
- `docs/pages.md`
- `docs/components.md`
- `docs/composables.md`
- `docs/stores.md`
- `docs/api.md`
- `docs/testing.md`
- `docs/design-system.md`

Do not duplicate architectural rules across documentation files.

When existing code and documented conventions disagree, prefer the documented convention unless the task explicitly changes the architecture.

If a mature existing implementation is identified by the documentation as a reference implementation, follow its structure where applicable.

---

## General Engineering Principles

Prefer:

- simple solutions;
- explicit code;
- feature locality;
- small cohesive abstractions;
- established project patterns;
- strong TypeScript typing;
- composition over large components;
- incremental changes.

Avoid:

- speculative abstractions;
- premature generalization;
- unnecessary wrappers;
- unnecessary dependencies;
- unrelated refactors;
- duplicated behavior;
- giant components;
- giant composables;
- global state for local concerns.

Do not create abstractions only because they might be useful in the future.

Create them when they solve an existing reuse, complexity, or maintainability problem.

Consistency with established SafeOps patterns is preferred over introducing another technically valid approach.

---

## Feature Architecture

Feature-specific code belongs under:

```text
src/modules/<feature>/
```

A feature may contain only the directories it actually needs, such as:

```text
<feature>/
├── api/
├── components/
├── composables/
├── pages/
├── schemas/
├── stores/
└── types/
```

Do not create empty directories merely to keep modules visually symmetrical.

Reusable SafeOps application-level components belong under:

```text
src/components/
```

Cross-feature reusable logic belongs under:

```text
src/shared/
```

Application infrastructure belongs under:

```text
src/app/
```

Layouts belong under:

```text
src/layouts/
```

Keep feature-specific code inside its feature unless genuine cross-feature reuse exists.

Do not move code into `shared` only because it could theoretically be reused.

Prefer local-first implementation and extract only after actual reuse or complexity justifies it.

---

## Vue

Use Composition API.

Use:

```vue
<script setup lang="ts">
```

Do not use Options API unless required by an existing integration.

Vue Single File Components should use this order:

1. `<script setup lang="ts">`
2. `<template>`
3. `<style scoped lang="scss">`

Component names use PascalCase.

Composable names begin with `use`.

Name feature composables using:

```text
use{Feature}{Responsibility}
```

Examples include `useSiteList`, `useSiteDetails`, `useSiteCreateForm`, and `useSiteUpdateForm`.

Do not create a composable merely to reduce the number of lines in a Vue component.

Pages should coordinate screen-level behavior.

Components should remain focused on UI responsibilities.

Composables should encapsulate cohesive reactive behavior, not act as generic service containers.

---

## UI Components and Styling

PrimeVue 4 in Styled Mode is the official SafeOps UI component library.

Before implementing a generic control, check whether PrimeVue already provides it.

Use PrimeVue directly for generic controls such as:

- buttons;
- inputs;
- selects;
- dialogs;
- tables;
- menus;
- tabs;
- toasts;
- tags;
- form controls.

Do not create wrappers such as `AppButton`, `AppInput`, `AppSelect`, or similar components merely to rename PrimeVue components, proxy their APIs, or theoretically isolate the dependency.

Create a SafeOps component only when it adds meaningful:

- domain semantics;
- application-specific behavior;
- composition;
- genuine reuse;
- presentation logic.

Prefer styling in this order:

1. PrimeVue semantic design tokens;
2. PrimeVue component design tokens;
3. application-level SCSS;
4. component-scoped SCSS.

Do not globally override PrimeVue internal CSS classes when design tokens can express the change.

Do not introduce another UI component library without an explicit architectural decision.

Detailed UI, field-state, validation presentation, and theming rules are documented in:

```text
docs/design-system.md
```

---

## Forms and Validation

PrimeVue Forms and Zod are the standard form and client-side validation solution for SafeOps.

When creating or modifying forms:

- use `@primevue/forms` for form state, validation lifecycle, and submission;
- use Zod for client-side validation schemas;
- integrate Zod through `zodResolver` from `@primevue/forms/resolvers/zod`;
- keep feature-specific validation schemas under the feature's `schemas/` directory;
- reuse field schemas between related forms when appropriate;
- prefer `z.infer<typeof schema>` when it avoids duplicating form value types;
- register PrimeVue form fields using `name`;
- let PrimeVue Forms own registered field state;
- do not create parallel `v-model` / `ref` state for a field without a concrete need;
- validate on blur and submit by default, not on every value update;
- prevent invalid forms from calling the API;
- do not reproduce schema validation manually in submit handlers;
- do not implement field validation through watchers or one error `ref` per field;
- keep frontend validation focused on input shape and user experience;
- keep authorization, permissions, persisted resource state, ownership, and other trusted business rules in the backend;
- keep Zod field validation errors separate from API `ProblemDetail` and business errors;
- do not introduce another form or validation library without an explicit architectural decision.

Do not create generic abstractions such as:

- `AppForm`;
- `AppFormField`;
- generic form composables;

unless an existing, demonstrated reuse or complexity problem justifies them.

Validation rules must reflect actual product or API requirements.

Do not invent domain constraints merely because they appear reasonable.

Follow the form architecture defined in:

```text
ARCHITECTURE.md
```

Follow field-state and validation presentation rules defined in:

```text
docs/design-system.md
```

---

## Internationalization

- All user-facing text must use the project's `vue-i18n` system.
- Do not hardcode Portuguese or English user-facing strings in Vue components.
- Use semantic translation keys; never use displayed text as a key.
- Shared translations belong in `src/i18n/shared`.
- Feature-specific translations belong in `src/modules/<feature>/i18n`.
- Name feature locale files by locale, such as `pt-BR.ts` and `en-US.ts`.
- Use the centralized locale definitions instead of duplicating locale strings.
- Keep API enum and domain values unchanged and translate them only for presentation.
- Never translate domain values before sending them to the API.
- Translate user-facing validation messages.
- Pass the translation function into Zod schemas instead of importing the global i18n instance.
- UI components must use the existing locale utilities; they must not duplicate locale persistence logic or access `localStorage` directly.
- Keep language selector names self-named as `Português` and `English`.
- Add translations only for features that currently need them; do not create speculative keys for future functionality.

---

## TypeScript

Keep TypeScript strict.

Do not use `any` unless interacting with an unavoidable untyped external API.

When `any` is unavoidable:

- keep its scope minimal;
- document why it is required.

Prefer:

- domain-specific types;
- `type` for domain models, DTOs, component props, aliases, unions, and ordinary object shapes;
- explicit public function types where useful;
- `import type`;
- union types where appropriate;
- inferred local types when obvious.

Avoid:

- unnecessary type assertions;
- unnecessary non-null assertions;
- duplicated types representing the same model;
- broad generic types when a domain type exists.

Use `interface` only when declaration merging or an intentionally interface-specific extension pattern is required.

When runtime values and TypeScript types represent the same finite set, prefer deriving the type from the runtime source when appropriate.

Example:

```ts
export const ORGANIZATION_STATUSES = ['ACTIVE', 'DISABLED'] as const;

export type OrganizationStatus = (typeof ORGANIZATION_STATUSES)[number];
```

Avoid maintaining duplicated enum-like values and union types separately when one can safely derive from the other.

---

## HTTP

Vue components must not call the raw HTTP client directly.

HTTP communication belongs in:

```text
src/modules/<feature>/api/
```

or, for cross-feature infrastructure:

```text
src/shared/api/
```

Feature API modules may know:

- endpoint URLs;
- request DTOs;
- response DTOs;
- HTTP methods.

Feature API modules must not know:

- Vue Router;
- component state;
- modal state;
- notifications;
- Vue `ref`, `computed`, or `watch`.

Group a feature's related HTTP operations according to the existing API conventions documented in:

```text
docs/api.md
```

Do not change endpoint semantics from the frontend unless the task explicitly requires an API contract change.

---

## State Management

Use component-local state when state belongs to one component or screen.

Use composables for cohesive reactive behavior.

Use Pinia for application state shared across unrelated screens or features.

Do not put ordinary local state in Pinia.

Examples that normally remain local:

- modal visibility;
- password visibility;
- form fields;
- page loading state;
- page-local filters;
- selected tab.

Do not store PrimeVue Forms field state in Pinia.

Follow these established feature boundaries:

- Site data is page-scoped and currently uses feature composables that call `siteApi` directly. Do not create a Site store unless site state becomes shared across unrelated screens.
- Organization data and `selectedOrganizationId` are application-wide context and remain in the Organization Pinia store.
- Organization store actions may call `organizationApi` and synchronize shared state.
- Organization composables must own page-specific loading, error messages, filters, watchers, and form submission state.
- Do not create a composable that merely forwards arguments to a store action without adding cohesive reactive or orchestration behavior.
- Do not create one composable mechanically for every API endpoint.
- Do not add a permanent loaded-once cache for organizations. Organization refresh operations must be able to query the API again because memberships may change externally.
- Concurrent request deduplication or stale-response protection is allowed; it must not prevent later refreshes.

For feature composables:

- use domain-specific data names such as `sites` and `site`, not generic `data`;
- use `isLoading` for reads and `isSubmitting` for form mutations;
- use `errorMessage` with `string | null` for a display-ready operation error;
- use `load` for list/details loading and `submit` for form submission;
- use a latest-request identifier when overlapping reads can occur and stale responses could overwrite current state;
- use an `isSubmitting` guard for mutations that must not execute concurrently;
- keep navigation and success notifications in the page unless a documented shared workflow requires otherwise;
- use `handleSubmit` for the component event handler and alias a composable's `submit` when useful, for example `submit: createSite`;
- render edit forms only after real API-backed initial values exist; do not mount them with placeholder values and expect `initialValues` to reinitialize later.

Follow the detailed conventions in:

```text
docs/composables.md
docs/stores.md
```

---

## Security

The backend is authoritative for authorization and security-sensitive validation.

Frontend permission checks exist only to improve user experience.

Never weaken backend security assumptions because an action is hidden or disabled in the frontend.

Never:

- expose secrets in frontend code;
- commit credentials;
- log passwords;
- log access or refresh tokens;
- store refresh tokens in JavaScript-accessible storage.

Authentication behavior must follow the existing SafeOps backend contract.

Frontend validation must never be treated as a security boundary.

---

## Scope of Changes

Keep changes focused on the requested task.

Do not perform unrelated refactors.

Before creating a new:

- component;
- composable;
- utility;
- store;
- API abstraction;
- validation schema abstraction;
- shared type;

search the repository for an existing equivalent or established pattern.

Before creating a generic interactive or visual component, also check whether PrimeVue already provides it.

Prefer extending an existing convention over introducing a competing pattern.

Do not rename, move, or reorganize unrelated files while implementing a focused task.

---

## Dependencies

Before adding a dependency:

1. check whether the current stack already solves the problem;
2. check whether the browser or Vue provides the required functionality;
3. confirm the dependency has a clear architectural benefit;
4. prefer actively maintained packages;
5. avoid adding a package for trivial functionality.

Another UI component library, form library, validation library, HTTP library, or state-management library requires an explicit architectural decision.

Do not add a dependency silently when implementing an unrelated task.

If a dependency is required by the requested architectural pattern, verify whether it is already installed before adding it.

---

## Agent Workflow

### Before implementing

1. Understand the requested behavior.
2. Inspect the relevant module.
3. Read the relevant convention documents.
4. Search for an existing implementation serving the same role.
5. Identify API and domain constraints relevant to the task.
6. Determine the smallest coherent change.

### While implementing

1. Follow existing project conventions.
2. Keep feature code local to its module.
3. Keep pages focused on orchestration.
4. Keep components focused on UI.
5. Keep HTTP details in API modules.
6. Keep reusable reactive behavior in composables.
7. Keep validation schemas inside the owning feature.
8. Avoid speculative abstractions.
9. Avoid changing behavior outside the task scope.

### After implementing

1. Review the complete diff.
2. Remove accidental or unrelated modifications.
3. Run the relevant project validation commands.
4. Fix failures introduced by the change.
5. Verify the affected user flow when practical.
6. Summarize what changed and any relevant constraints.

---

## Verification

Use the project scripts defined in `package.json`.

Typical checks include:

```bash
npm run lint
npm run type-check
```

When the change can affect the production bundle, also run:

```bash
npm run build
```

Run relevant tests when the project provides them.

Do not claim a command passed unless it was actually executed.

If a command does not exist, cannot be executed, or fails because of an unrelated pre-existing issue, state that clearly.

Do not silently fix unrelated pre-existing failures.

---

## Documentation Changes

Update documentation when a task intentionally changes an established:

- module structure;
- dependency direction;
- global state strategy;
- router strategy;
- HTTP strategy;
- form strategy;
- validation strategy;
- UI component strategy;
- testing strategy.

Do not duplicate implementation details across multiple documentation files.

Use:

- `ARCHITECTURE.md` for architecture and responsibility boundaries;
- `docs/design-system.md` for visual and interaction standards;
- focused files under `docs/` for implementation conventions;
- this `AGENTS.md` for concise agent instructions.

---

## Definition of Done

A task is complete when:

- the requested behavior is implemented;
- the documented architecture is respected;
- relevant loading states are handled;
- relevant error states are handled;
- TypeScript types are correct;
- unnecessary duplication was not introduced;
- relevant tests were created or updated when appropriate;
- relevant lint and type-check commands pass for the changed code;
- no unrelated files were modified;
- documentation was updated if the task intentionally changed an established architectural convention.

---

## Core Rule

Consistency with SafeOps conventions is more important than introducing another valid Vue pattern.

When multiple approaches are technically valid, prefer the approach already established by this repository.
