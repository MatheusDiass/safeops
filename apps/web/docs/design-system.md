# SafeOps Design System

## Purpose

SafeOps uses a clean, professional enterprise SaaS visual language appropriate for safety-management workflows.

The UI should be:

- clean
- professional
- consistent
- accessible
- responsive
- information-focused
- modern without unnecessary decoration

Visual decisions should help users understand operational information and complete safety workflows efficiently.

---

## Foundation

PrimeVue 4 in Styled Mode is the official SafeOps UI component library.

PrimeVue provides:

- generic UI primitives
- accessible interaction behavior
- component styling
- theme infrastructure

Use PrimeVue directly for generic controls such as `Button`, `InputText`, `Password`, `Textarea`, `Select`, `AutoComplete`, `Checkbox`, `RadioButton`, `DatePicker`, `Dialog`, `Drawer`, `DataTable`, `Paginator`, `Menu`, `Tabs`, `Toast`, and `Tag`.

SafeOps should not recreate these primitives unnecessarily or introduce another UI component library without an explicit architectural decision.

The UI composition direction is:

```text
SafeOps Theme
    ↓
PrimeVue Components
    ↓
SafeOps Feature Components
    ↓
Pages
```

---

## Theme

SafeOps uses a custom PrimeVue Styled preset based on an official PrimeVue preset. The global configuration belongs under:

```text
src/app/theme/
```

For example:

```text
src/app/theme/safeops.preset.ts
```

Customize the preset primarily through PrimeVue design tokens:

```text
primitive tokens
→ foundational values such as color palettes

semantic tokens
→ application-wide visual meaning such as primary, surface, and state colors

component tokens
→ customization specific to one PrimeVue component
```

Prefer semantic tokens for application-wide meaning. Use component tokens only when customization is specific to that component.

Avoid globally overriding PrimeVue internal CSS classes when design tokens can produce the same result.

---

## Components

Use PrimeVue components directly for generic controls.

Do not create components such as `AppButton`, `AppInput`, `AppSelect`, `AppAutocomplete`, or `AppDialog` solely to rename PrimeVue components, forward the same props and events, or theoretically isolate the dependency.

Create a custom component when it represents a SafeOps concept or a reusable application composition and adds meaningful:

- domain semantics
- behavior
- composition
- reuse
- presentation logic

Valid examples include:

```text
IncidentForm
IncidentTable
IncidentFilters
IncidentStatusBadge
SiteSelector
OrganizationSelector
UserInviteForm
PageHeader
EmptyState
```

For example, `IncidentStatusBadge` may compose PrimeVue `Tag` because it maps SafeOps incident statuses to consistent presentation. An `AppButton` that only proxies PrimeVue `Button` is not useful.

---

## Styling Strategy

Use this customization priority:

1. PrimeVue semantic tokens
2. PrimeVue component tokens
3. global or application-level SCSS
4. scoped component SCSS

SCSS remains part of the project. Use it primarily for:

- application shell
- page layouts
- feature layouts
- responsive behavior
- custom SafeOps components
- styles not represented appropriately by PrimeVue tokens

Do not manually recreate PrimeVue component styling.

---

## Forms

Use PrimeVue form controls consistently.

A field may contain:

- label
- required indicator
- control
- helper text
- validation error

Place validation errors near the related field. Do not communicate validation only through color; use explanatory text and appropriate accessible state as well.

Keep alignment, spacing, required-state presentation, and error behavior consistent across forms.

---

## Actions

Use actions consistently according to intent:

- primary actions represent the main next step
- secondary actions support or defer the main action
- destructive actions clearly communicate risk

Avoid several competing primary actions within the same action group. Labels should describe the outcome rather than use vague wording.

---

## Tables

Prefer PrimeVue `DataTable` when structured tabular functionality such as sorting, pagination, selection, or filtering is useful.

Do not use `DataTable` when a simpler card or list interface communicates the information more clearly.

Desktop tables must have an intentional responsive strategy. Depending on the information and workflow, this may include priority columns, horizontal scrolling, responsive stacking, or a simpler small-screen presentation.

---

## Dialogs

Use PrimeVue `Dialog` for focused secondary workflows.

Do not place large or multi-step workflows in dialogs when a dedicated page would provide clearer navigation and more space. Destructive actions should require clear confirmation when the impact warrants it.

---

## Loading

Choose loading feedback based on the scope of the operation:

- use the PrimeVue `Button` loading state for submitted actions
- use skeletons or suitable loading indicators for initial content
- use local loading states when only part of a page updates

Avoid unnecessarily blocking the entire interface for a local operation. Prevent accidental duplicate submissions where appropriate.

---

## Empty States

An empty state should explain:

1. what is empty
2. why it matters
3. what the user can do next

Offer a relevant action when the user has permission and a clear next step exists.

---

## Responsive Design

All feature screens should consider desktop and smartphone layouts.

Prefer responsive composition over maintaining separate desktop and mobile feature implementations unless the interactions genuinely differ. Preserve task priority, readable content, and accessible controls as available space changes.

---

## Accessibility

Use PrimeVue accessibility behavior whenever possible and preserve it when composing SafeOps components.

- maintain visible keyboard focus
- give icon-only actions accessible labels
- associate labels and validation messages with form controls
- preserve keyboard navigation
- do not communicate status, validation, or severity through color alone
- use appropriate text, icons, or other non-color indicators

Accessibility is part of the component behavior and must be considered during implementation and testing.
