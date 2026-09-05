## Testing Conventions

Tests should protect meaningful application behavior without duplicating implementation details.

The test strategy may include:

- Vitest
- Vue Test Utils
- Playwright

Use the tooling configured in the repository.

---

## Test Priority

Prioritize tests for:

- important user workflows
- reusable reactive behavior
- form validation behavior
- conditional UI behavior
- permission-dependent UI
- regressions
- complex transformations
- critical authentication behavior

Do not write tests solely to increase coverage percentages.

---

## Component Tests

Component tests should focus on observable behavior.

Test:

- rendered state
- interaction
- emitted events
- relevant conditional rendering

Avoid testing:

- Vue internals
- PrimeVue internals
- private implementation details
- exact internal variable names

PrimeVue is responsible for testing its generic components. Test SafeOps behavior around PrimeVue components instead of asserting how PrimeVue internally renders a `Button`, `Select`, `Dialog`, `DataTable`, or another primitive.

Examples worth testing include:

- `IncidentForm` emits or submits the expected SafeOps data
- changing severity updates form state
- `IncidentStatusBadge` maps SafeOps statuses correctly
- permission-based actions are hidden or disabled appropriately
- important workflows built with PrimeVue components behave correctly

---

## Composable Tests

Test composables when they contain meaningful behavior.

Good candidates:

```text
useIncidentFilters
useIncidentForm
useIncidentStatus
```

Simple wrappers around trivial state may not require dedicated tests.

---

## Page Tests

Do not test every page merely because it exists.

Test pages when they coordinate meaningful flows that cannot be adequately covered by component or composable tests.

---

## End-to-End Tests

Use E2E tests for critical application flows.

Examples:

```text
login
registration
incident creation
incident status changes
critical authorization flows
```

Prefer a small number of reliable end-to-end flows over a large fragile suite.

---

## API Mocking

Tests should not depend on the production backend.

Use the project's established API mocking strategy.

Keep mocks close to the API contract.

Avoid mocking internal implementation details unnecessarily.

---

## Naming

Test names should describe behavior.

Prefer:

```text
shows an error when authentication fails
emits submit when the incident form is valid
disables status actions without permission
```

Avoid vague names:

```text
works correctly
test incident
test button
```

---

## Verification

Before considering relevant frontend work complete, run the applicable project checks:

```bash
npm lint
npm type-check
npm test
```

For production-impacting changes:

```bash
npm build
```

Run E2E tests when the changed behavior is covered by critical end-to-end flows.
