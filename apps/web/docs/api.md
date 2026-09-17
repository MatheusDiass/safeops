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

## Feature API Objects

Group a feature's HTTP operations in a single `<feature>Api` object.

The API object is responsible only for HTTP communication. Business rules, application state, routing, notifications, and UI behavior should remain in higher layers such as Pinia stores, composables, or pages.

Use concise method names because the feature is already identified by the API object.

For standard CRUD operations, prefer:

- `list`
- `get`
- `create`
- `update`
- `remove`

For operations that are not standard CRUD operations, use domain-specific names such as:

- `changeStatus`
- `close`
- `reopen`
- `inviteMember`

Example:

```ts
import { http } from '../../../shared/api/http';
import type {
  CreateOrganizationRequest,
  Organization,
  UpdateOrganizationRequest,
} from '../types/organization.types';

export const organizationApi = {
  async list(): Promise<Organization[]> {
    const response = await http.get<Organization[]>('/organizations');

    return response.data;
  },

  async get(organizationId: string): Promise<Organization> {
    const response = await http.get<Organization>(`/organizations/${organizationId}`);

    return response.data;
  },

  async create(request: CreateOrganizationRequest): Promise<Organization> {
    const response = await http.post<Organization>('/organizations', request);

    return response.data;
  },

  async update(organizationId: string, request: UpdateOrganizationRequest): Promise<Organization> {
    const response = await http.patch<Organization>(`/organizations/${organizationId}`, request);

    return response.data;
  },
};
```

Consumers should call HTTP operations through the feature API object:

```ts
await organizationApi.list();
await organizationApi.get(organizationId);
await organizationApi.create(request);
await organizationApi.update(organizationId, request);
```

Higher layers may keep domain-specific action names while delegating HTTP communication to the API object.

Example in a Pinia store:

```ts
async function createOrganization(request: CreateOrganizationRequest): Promise<void> {
  const organization = await organizationApi.create(request);

  upsertOrganization(organization);
  selectedOrganizationId.value = organization.id;
}

async function updateOrganization(
  organizationId: string,
  request: UpdateOrganizationRequest,
): Promise<void> {
  const organization = await organizationApi.update(organizationId, request);

  upsertOrganization(organization);
}
```

Responsibilities should remain clear:

```text
organizationApi.create()
→ HTTP communication

organizationStore.createOrganization()
→ application state and behavior

page / composable
→ UI behavior and user interaction
```

Do not export standalone feature-prefixed HTTP functions such as:

```ts
createOrganization();
updateOrganization();
getOrganization();
```

when they would conflict with store actions or other higher-level functions.

Avoid import aliases such as:

```ts
import { createOrganization as createOrganizationRequest } from './organization.api';
```

Prefer:

```ts
import { organizationApi } from './organization.api';

await organizationApi.create(request);
```

Do not place application state, Pinia logic, routing, notifications, or business rules inside API objects.

The API object's responsibility should remain limited to:

```text
build the HTTP request
→ send the request
→ return the typed response
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
