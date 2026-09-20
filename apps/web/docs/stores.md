# Store Conventions

## Purpose

This document defines how SafeOps uses Pinia stores.

Pinia represents application state shared across unrelated screens, layouts, or workflows. It is not the default location for every reactive value or API request.

Composable conventions are documented separately in `docs/composables.md`.

---

## When to create a store

Create a feature store when at least one of these requirements exists:

- unrelated screens depend on the same state;
- state must survive the unmounting of the current page;
- layouts or application initialization require the state;
- multiple workflows must mutate one shared source of truth;
- shared state must be cleared consistently during logout or context changes.

Keep state in a component or composable when it belongs only to one page or workflow.

Do not create stores merely to keep feature directories symmetrical.

---

## Store responsibilities

A feature store may own:

- shared domain state;
- selected application context;
- derived state used across screens;
- actions that call a feature API and synchronize shared state;
- internal replacement or upsert operations;
- reset behavior for logout or application-context changes;
- protection against stale or duplicated in-flight operations when required.

A feature store must not own ordinary page concerns such as:

- page-specific `isLoading`;
- display-ready page error messages;
- form values;
- PrimeVue Forms state;
- page-local search and filters;
- modal visibility;
- route navigation;
- success notifications.

Those concerns belong to pages, components, or composables.

---

## Store data flow

When shared state is involved, the preferred flow is:

```text
Page
  → Composable, when it adds page behavior
    → Feature store
      → Feature API
        → Shared HTTP client
```

Pinia actions may call feature API methods directly. A composable may call a store action when it adds loading state, error presentation, filters, watchers, lifecycle coordination, or form submission behavior.

Do not create a composable merely to forward the same arguments to a store action.

---

## Site module

The Site module currently does not require a Pinia store.

Site list, details, and form request state are page-scoped and use feature composables that call `siteApi` directly:

```text
Page → Site composable → siteApi
```

Do not add a Site store unless site state later becomes shared across unrelated screens, layouts, or application-wide workflows.

---

## Organization store

Organization state is application-wide because it is used by:

- authenticated application initialization;
- onboarding;
- the navigation organization selector;
- organization pages;
- unrelated screens that require the selected organization context.

The Organization flow is:

```text
Page
  → Organization composable
    → Organization store
      → organizationApi
```

The Organization store owns:

- `organizations`;
- `selectedOrganizationId`;
- derived selected organization state;
- actions that refresh the list or load details;
- create and update actions that synchronize shared state;
- internal replacement and upsert behavior;
- clear/reset behavior for logout.

Organization composables own page-specific loading, error messages, filters, watchers, and submission state. Their naming and return conventions are defined in `docs/composables.md`.

---

## Organization freshness

Do not use a permanent `hasLoadedOrganizations` guard that treats the first list response as valid for the complete authenticated session.

Organization memberships may be changed externally. A refresh action must be able to call `organizationApi.list()` again.

It is acceptable to reuse a request only while the same request is still in flight. This is concurrent-request deduplication, not a persistent data cache. After completion, a later refresh must be allowed to reach the API.

Refresh organizations at appropriate application boundaries, including:

- authenticated initialization;
- entering a screen that requires a current organization list;
- opening a control that requires current membership information;
- completing an operation that changes organization availability.

Real-time external membership updates require an explicit mechanism such as polling, SSE, or another event strategy. Removing a permanent cache does not make changes appear without a new refresh trigger.

---

## Selection consistency

Whenever the available organization list is replaced:

1. preserve `selectedOrganizationId` when that organization remains available;
2. otherwise select the first available organization;
3. use `null` when no organization is available.

The store may expose derived selected organization state:

```ts
const selectedOrganization = computed<Organization | null>(() => {
  return organizations.value.find(({ id }) => id === selectedOrganizationId.value) ?? null;
});
```

The nullable type is intentional because initialization, onboarding, logout, and membership changes can leave the application without an available organization.

Operations that require an organization should accept a non-null `string`. The calling page must narrow the nullable application state before invoking them.

---

## Logout and stale responses

Clearing a store must prevent a request started before logout from repopulating state afterward.

Use request cancellation or an internal revision/generation value when this race is possible. Clearing the store invalidates earlier request generations before removing shared state.

Do not expose this internal concurrency mechanism as public UI state.

---

## Decision checklist

Before adding a store, ask:

1. Is this state used by unrelated screens or layouts?
2. Must it survive the unmounting of the current page?
3. Is there a genuine shared source-of-truth requirement?
4. Does application initialization or logout need to manage it?

If the answers are no, keep the state in a component or composable.

Before adding state to an existing store, ask:

1. Is the state global or only related to one page?
2. Must other screens observe the same value?
3. Would keeping it local avoid unnecessary coupling?

Keep page-specific state local unless a demonstrated shared-state requirement exists.
