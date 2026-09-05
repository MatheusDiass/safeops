## Feature Module Conventions

SafeOps organizes domain functionality under:

```text
src/modules/<feature>/
```

Examples:

```text
src/modules/auth/
src/modules/organization/
src/modules/site/
src/modules/incident/
```

A module may contain:

```text
<feature>/
├── api/
├── components/
├── composables/
├── pages/
└── types/
```

Only create directories currently required by the feature.

Example:

```text
incident/
├── api/
│   └── incident.api.ts
├── components/
│   ├── IncidentForm.vue
│   ├── IncidentTable.vue
│   ├── IncidentFilters.vue
│   └── IncidentStatusBadge.vue
├── composables/
│   ├── useIncidentList.ts
│   ├── useIncidentDetails.ts
│   └── useIncidentForm.ts
├── pages/
│   ├── IncidentListPage.vue
│   ├── IncidentDetailsPage.vue
│   ├── CreateIncidentPage.vue
│   └── EditIncidentPage.vue
└── types/
    └── incident.types.ts
```

PrimeVue is an implementation detail used by feature components, not a module architecture layer. Do not add PrimeVue-specific folders to feature modules.

---

## Responsibilities

### `api/`

Contains communication with backend endpoints belonging to the feature.

Example:

```text
incidents/api/incident.api.ts
```

### `components/`

Contains UI components specific to the feature.

Example:

```text
IncidentCard.vue
IncidentForm.vue
IncidentStatusBadge.vue
```

### `composables/`

Contains cohesive reactive behavior.

Example:

```text
useIncidentList.ts
useIncidentDetails.ts
useIncidentForm.ts
```

### `pages/`

Contains route-level screens.

Example:

```text
IncidentListPage.vue
IncidentDetailsPage.vue
CreateIncidentPage.vue
EditIncidentPage.vue
```

### `types/`

Contains feature-specific TypeScript types.

Example:

```text
incident.types.ts
```

---

## Module Independence

A module should not import internal implementation details from another feature without a clear architectural reason.

When cross-feature information is genuinely shared, prefer an explicit shared abstraction or application-level state.

Do not move code to `shared` merely to avoid an import between two modules without understanding the ownership of that behavior.

---

## Naming

Use domain terminology.

Good:

```text
IncidentForm.vue
useIncidentList.ts
incident.api.ts
incident.types.ts
```

Avoid unnecessarily generic names:

```text
Form.vue
List.vue
service.ts
helpers.ts
```

unless the surrounding context makes the responsibility unambiguous.

---

## Standard CRUD Shape

When applicable, CRUD-like modules should use predictable page naming:

```text
XListPage.vue
XDetailsPage.vue
CreateXPage.vue
EditXPage.vue
```

and predictable composable naming:

```text
useXList.ts
useXDetails.ts
useXForm.ts
```

Do not force every module to have every file.

Auth, for example, may naturally have:

```text
LoginPage.vue
RegisterPage.vue
ForgotPasswordPage.vue
```

instead of CRUD page names.

---

## Reference Implementations

When a mature feature already implements a pattern correctly, use it as a structural reference.

Copy the architectural pattern, not feature-specific behavior.

Do not mechanically duplicate files purely for symmetry.
