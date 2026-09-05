## Composable Conventions

A composable encapsulates cohesive reactive Vue behavior.

Composable names must begin with:

```text
use
```

Examples:

```text
useIncidentList
useIncidentDetails
useIncidentForm
useIncidentStatus
useDebounce
```

---

## When to Create a Composable

Create one when:

- reactive logic is reused
- page logic becomes substantial
- a clear reactive behavior deserves an abstraction
- multiple components participate in the same reactive workflow

A composable often uses:

```text
ref
reactive
computed
watch
lifecycle hooks
Vue Router
Pinia
```

depending on its responsibility.

---

## When Not to Create a Composable

Do not create one just to reduce component line count.

Do not use composables as generic service classes.

Do not create a composable merely to wrap trivial PrimeVue state owned by one component. For example, keep this state in the component instead of creating `useDialog()`:

```ts
const visible = ref(false);
```

Avoid:

```text
useIncidents
```

if it eventually contains:

```text
list
get
create
update
changeStatus
filters
form
modal
attachments
comments
```

Prefer composables grouped by cohesive behavior.

---

## `useXList`

Represents collection behavior.

Typical responsibilities:

- collection state
- loading state
- error state
- loading the collection
- refreshing
- list-specific reactive filtering when appropriate

Typical API:

```ts
return {
  items,
  isLoading,
  error,
  load,
  refresh,
};
```

Using a domain-specific name instead of `items` is also valid:

```ts
return {
  incidents,
  isLoading,
  error,
  load,
  refresh,
};
```

Choose one style and remain consistent in equivalent composables.

---

## `useXDetails`

Represents one resource.

Typical responsibilities:

```text
resource state
loading
error
load
clear
```

Example API:

```ts
return {
  incident,
  isLoading,
  error,
  load,
  clear,
};
```

Do not turn `useXDetails` into a container for every action related to the resource.

---

## `useXForm`

Represents reactive form behavior.

Typical responsibilities:

- form state
- validation-related computed state
- reset
- initial values
- setValues
- presentation-specific field behavior

Example shape:

```ts
return {
  form,
  isValid,
  reset,
  setValues,
};
```

By default, routing should remain outside the form composable.

By default, create/update HTTP orchestration should remain explicit at the page level.

Encapsulate submission only when there is enough repeated or complex workflow to justify it.

---

## Feature-Specific Composables

Use descriptive behavioral names.

Examples:

```text
useIncidentStatus
useIncidentFilters
useSiteSelector
```

Do not create equivalent composables in every feature merely for structural symmetry.

---

## Composable vs Utility

If a function does not require Vue reactivity or lifecycle behavior, it usually should not be a composable.

Instead of:

```ts
useFormatIncidentStatus();
```

prefer:

```ts
formatIncidentStatus();
```

in a utility or domain-specific helper.

---

## Composable vs Pinia

Composable:

```text
reactive behavior or workflow
```

Pinia:

```text
state shared across unrelated application areas
```

A composable does not automatically require a store.

---

## Responsibility Boundaries

```text
Composable
→ reactive Vue behavior

Utility
→ pure reusable function

API
→ HTTP communication

Pinia
→ genuinely shared application state
```

PrimeVue does not change these boundaries. Keep component-local interaction state local unless reuse or workflow complexity provides a concrete reason to extract it.
