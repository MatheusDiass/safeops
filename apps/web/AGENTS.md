# SafeOps Web Agent Guide

## Purpose

SafeOps Web is the frontend application for the SafeOps safety management platform.

This repository follows a feature-oriented Vue 3 architecture with strong conventions for pages, components, composables, API access, and TypeScript.

This file defines repository-wide instructions for coding agents.

Detailed implementation rules live under `docs/`.

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

Do not introduce another frontend framework, router, state-management library, HTTP library, form library, validation library, or major dependency unless explicitly requested or clearly justified.

Prefer existing project dependencies and native Vue/browser capabilities.

---

## Source of Truth

Before modifying code, inspect the relevant existing implementation and read the documentation related to the task.

Architecture:

- `ARCHITECTURE.md`

Implementation conventions:

- `docs/modules.md`
- `docs/pages.md`
- `docs/components.md`
- `docs/composables.md`
- `docs/api.md`
- `docs/testing.md`
- `docs/design-system.md`

Do not duplicate architectural rules across documentation files.

When existing code and documented conventions disagree, prefer the documented convention unless the task explicitly changes the architecture.

If a mature existing implementation is identified by the documentation as a reference implementation, follow its structure where applicable.

---

## General Engineering Principles

Prefer:

- simple solutions
- explicit code
- feature locality
- small cohesive abstractions
- established project patterns
- strong TypeScript typing
- composition over large components
- incremental changes

Avoid:

- speculative abstractions
- premature generalization
- unnecessary wrappers
- unnecessary dependencies
- unrelated refactors
- duplicated business behavior
- giant components
- giant composables
- global state for local concerns

Do not create abstractions only because they might be useful in the future.

Create them when they solve an existing reuse, complexity, or maintainability problem.

---

## Feature Architecture

Feature-specific code belongs under:

`src/modules/<feature>/`

Reusable SafeOps application-level components belong under:

`src/components/`

Cross-feature reusable logic belongs under:

`src/shared/`

Application infrastructure belongs under:

`src/app/`

Layouts belong under:

`src/layouts/`

Keep feature-specific code inside its feature unless genuine cross-feature reuse exists.

Do not move code into `shared` only because it could theoretically be reused.

---

## Vue

Use Composition API.

Use:

`<script setup lang="ts">`

Do not use Options API unless required by an existing integration.

Vue Single File Components should use this order:

1. `<script setup lang="ts">`
2. `<template>`
3. `<style scoped lang="scss">`

Component names use PascalCase.

Composable names begin with `use`.

Do not create a composable merely to reduce the number of lines in a Vue component.

---

## UI Components and Styling

PrimeVue 4 in Styled Mode is the official SafeOps UI component library.

Before implementing a generic control, search PrimeVue for an existing component. Use PrimeVue directly for generic controls such as buttons, inputs, selects, dialogs, tables, menus, tabs, toasts, and tags.

Do not create wrappers such as `AppButton`, `AppInput`, or `AppSelect` merely to rename PrimeVue components, proxy their APIs, or theoretically isolate the dependency.

Create a SafeOps component only when it adds meaningful domain semantics, behavior, composition, reuse, or presentation logic.

Prefer styling in this order:

1. PrimeVue semantic design tokens
2. PrimeVue component design tokens
3. application-level SCSS
4. component-scoped SCSS

Do not globally override PrimeVue internal CSS classes when design tokens can express the change. Do not introduce another UI component library without an explicit architectural decision.

Detailed UI and theming rules are documented in `docs/design-system.md`.

---

## TypeScript

Keep TypeScript strict.

Do not use `any` unless interacting with an unavoidable untyped external API.

When `any` is unavoidable, keep its scope minimal and document why.

Prefer:

- domain-specific types
- `type` for domain models, DTOs, component props, aliases, unions, and ordinary object shapes
- explicit public function types
- `import type`
- union types where appropriate
- inferred local types when obvious

Avoid:

- unnecessary type assertions
- unnecessary non-null assertions
- duplicated types representing the same API model
- broad generic types when a domain type exists

Use `interface` only when declaration merging or an intentionally interface-specific extension pattern is required.

---

## HTTP

Vue components must not call the raw HTTP client directly.

HTTP communication belongs in:

`src/modules/<feature>/api/`

or, for cross-feature infrastructure:

`src/shared/api/`

API modules may know:

- endpoint URLs
- request DTOs
- response DTOs
- HTTP methods

API modules must not know:

- Vue Router
- component state
- modal state
- notifications
- Vue `ref`, `computed`, or `watch`

---

## State Management

Use component-local state when the state belongs to one component.

Use composables for cohesive reactive behavior.

Use Pinia for application state shared across unrelated screens or features.

Do not put ordinary local state in Pinia.

Examples that normally remain local:

- modal visibility
- password visibility
- form fields
- page loading state
- page-local filters
- selected tab

---

## Security

The backend is authoritative for authorization.

Frontend permission checks are only for user experience.

Never weaken backend security assumptions because an action is hidden in the frontend.

Never:

- expose secrets in frontend code
- commit credentials
- log passwords
- log authentication tokens
- store refresh tokens in JavaScript-accessible storage

Authentication behavior must follow the existing SafeOps backend contract.

---

## Scope of Changes

Keep changes focused on the requested task.

Do not perform unrelated refactors.

Before creating a new:

- component
- composable
- utility
- store
- API abstraction
- shared type

search the repository for an existing equivalent or established pattern.

Before creating a generic interactive or visual component, also check whether PrimeVue already provides it.

Prefer extending an existing convention over introducing a new competing pattern.

---

## Dependencies

Before adding a dependency:

1. Check whether the current stack already solves the problem.
2. Check whether the browser or Vue provides the required functionality.
3. Confirm the dependency has a clear architectural benefit.
4. Prefer actively maintained packages.
5. Avoid adding a package for trivial functionality.

Another UI component library requires an explicit architectural decision.

Do not add a new dependency silently when implementing an unrelated task.

---

## Agent Workflow

Before implementing:

1. Understand the requested behavior.
2. Inspect the relevant module.
3. Read the relevant convention documents.
4. Search for an existing implementation serving the same role.
5. Determine the smallest coherent change.

While implementing:

1. Follow existing project conventions.
2. Keep feature code local to its module.
3. Keep pages focused on orchestration.
4. Keep components focused on UI.
5. Keep HTTP details in API modules.
6. Keep reusable reactive behavior in composables.
7. Avoid speculative abstractions.

After implementing:

1. Review the complete diff.
2. Remove accidental or unrelated modifications.
3. Run the relevant validation commands.
4. Fix failures introduced by the change.
5. Summarize what changed.

---

## Verification

Use the project scripts defined in `package.json`.

Expected checks include:

```bash
npm lint
npm type-check
```

When the change can affect the production bundle, also run:

```bash
npm build
```

Do not claim a command passed unless it was actually executed.

If a command cannot be executed, clearly state why.

Do not silently fix unrelated pre-existing failures.

---

## Definition of Done

A task is complete when:

- the requested behavior is implemented
- the architecture is respected
- relevant loading states are handled
- relevant error states are handled
- TypeScript types are correct
- unnecessary duplication was not introduced
- relevant tests were created or updated when appropriate
- lint and type checking pass for the changed code
- no unrelated files were modified

---

## Core Rule

Consistency with SafeOps conventions is more important than introducing another valid Vue pattern.

When multiple approaches are technically valid, prefer the approach already established by this repository.
