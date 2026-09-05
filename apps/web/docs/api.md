## API Conventions

HTTP communication is isolated from Vue components.

```text
Page / Component
    ↓
Composable
    ↓
Feature API
    ↓
src/shared/api/http.ts
    ↓
SafeOps Backend
```

PrimeVue must never appear in API modules.

Feature endpoints belong under:

```text
src/modules/<feature>/api/
```

Example:

```text
src/modules/incident/api/incident.api.ts
```

Shared HTTP infrastructure belongs under:

```text
src/shared/api/
```

Example:

```text
src/shared/api/http.ts
```

---

## Feature API Modules

A feature API module may expose:

```ts
listIncidents();
getIncident();
createIncident();
updateIncident();
changeIncidentStatus();
```

Grouping endpoint operations in one feature API file is acceptable while the file remains cohesive.

Split it only when the feature becomes large enough that separate API concerns are clearer.

---

## API Responsibilities

API modules may contain:

- endpoint construction
- HTTP method selection
- request types
- response types
- query parameter construction
- HTTP-client usage

They must not contain:

- Vue refs
- Vue computed values
- router navigation
- modal state
- notification rendering
- page-specific state

---

## HTTP Client

Use the centralized project HTTP client.

Do not create Axios/fetch instances inside feature files.

The shared client owns cross-cutting HTTP behavior such as:

- base URL
- credentials configuration
- common headers
- common interceptors when required
- standard response/error infrastructure

---

## Authentication

Authentication must follow the backend contract.

Do not implement independent token behavior inside feature modules.

Refresh-token handling belongs to shared authentication infrastructure.

Do not persist refresh tokens in localStorage, sessionStorage, or other JavaScript-accessible storage when the backend uses secure HttpOnly cookies.

---

## Errors

API modules should expose failures in a form that higher layers can handle consistently.

Do not display UI notifications from API modules.

Mapping technical HTTP errors into application-level errors may live in shared API infrastructure when useful.

Avoid duplicating status-code handling in every page.

---

## Types

Prefer explicit request and response types.

Example:

```ts
type CreateIncidentRequest = {
  title: string;
  description: string;
};
```

Avoid using backend response objects as loosely typed dictionaries.

Do not use `any` for HTTP responses.

---

## URLs

Do not duplicate route fragments unnecessarily.

When endpoint construction becomes complex, prefer small explicit helpers rather than generic URL-building frameworks.

Readable endpoint code is preferred over excessive abstraction.
