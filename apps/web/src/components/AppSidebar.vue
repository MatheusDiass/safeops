<script setup lang="ts">
import {
  mdiAccountCircleOutline,
  mdiAlertCircleOutline,
  mdiClose,
  mdiMapMarkerMultipleOutline,
  mdiOfficeBuildingOutline,
  mdiViewDashboardOutline,
} from '@mdi/js';
import Select from 'primevue/select';
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import SafeOpsMark from './SafeOpsMark.vue';

type Organization = {
  id: string;
  initials: string;
  name: string;
  siteCount: number;
};

type NavigationItem = {
  label: string;
  routeName: string;
  icon: string;
};

type Props = {
  showCloseButton?: boolean;
};

withDefaults(defineProps<Props>(), {
  showCloseButton: false,
});

const emit = defineEmits<{
  close: [];
}>();

const route = useRoute();

const defaultOrganization: Organization = {
  id: 'northstar-manufacturing',
  initials: 'NM',
  name: 'Northstar Manufacturing',
  siteCount: 8,
};

const organizations: Organization[] = [
  defaultOrganization,
  {
    id: 'atlas-construction',
    initials: 'AC',
    name: 'Atlas Construction',
    siteCount: 5,
  },
  {
    id: 'meridian-logistics',
    initials: 'ML',
    name: 'Meridian Logistics',
    siteCount: 12,
  },
];

const selectedOrganization = ref<Organization>(defaultOrganization);

const navigationItems: NavigationItem[] = [
  { label: 'Dashboard', routeName: 'dashboard', icon: mdiViewDashboardOutline },
  { label: 'Organization', routeName: 'organization', icon: mdiOfficeBuildingOutline },
  { label: 'Sites', routeName: 'sites', icon: mdiMapMarkerMultipleOutline },
  { label: 'Incidents', routeName: 'incidents', icon: mdiAlertCircleOutline },
  { label: 'Account', routeName: 'account', icon: mdiAccountCircleOutline },
];

function isActive(routeName: string): boolean {
  return route.name === routeName;
}
</script>

<template>
  <aside class="app-sidebar">
    <header class="app-sidebar__header">
      <RouterLink
        class="sidebar-brand"
        :to="{ name: 'dashboard' }"
        aria-label="SafeOps dashboard"
        @click="emit('close')"
      >
        <SafeOpsMark />
        <span class="sidebar-brand__copy">
          <strong>SafeOps</strong>
          <span>Safety operations</span>
        </span>
      </RouterLink>

      <button
        v-if="showCloseButton"
        class="app-sidebar__close"
        type="button"
        aria-label="Close navigation"
        @click="emit('close')"
      >
        <svg
          viewBox="0 0 24 24"
          aria-hidden="true"
        >
          <path :d="mdiClose" />
        </svg>
      </button>
    </header>

    <div class="organization-field">
      <label for="organization-selector">Organization</label>
      <Select
        v-model="selectedOrganization"
        input-id="organization-selector"
        :options="organizations"
        option-label="name"
        data-key="id"
        class="organization-select"
        fluid
      >
        <template #value="{ value }">
          <div
            v-if="value"
            class="organization-option"
          >
            <span class="organization-option__initials">{{ value.initials }}</span>
            <span class="organization-option__copy">
              <strong>{{ value.name }}</strong>
              <span>{{ value.siteCount }} sites</span>
            </span>
          </div>
        </template>
        <template #option="{ option }">
          <div class="organization-option">
            <span class="organization-option__initials">{{ option.initials }}</span>
            <span class="organization-option__copy">
              <strong>{{ option.name }}</strong>
              <span>{{ option.siteCount }} sites</span>
            </span>
          </div>
        </template>
      </Select>
    </div>

    <nav
      class="workspace-navigation"
      aria-label="Workspace"
    >
      <p class="workspace-navigation__title">Workspace</p>
      <ul>
        <li
          v-for="item in navigationItems"
          :key="item.routeName"
        >
          <RouterLink
            class="workspace-navigation__link"
            :class="{ 'workspace-navigation__link--active': isActive(item.routeName) }"
            :to="{ name: item.routeName }"
            :aria-current="isActive(item.routeName) ? 'page' : undefined"
            @click="emit('close')"
          >
            <svg
              viewBox="0 0 24 24"
              aria-hidden="true"
            >
              <path :d="item.icon" />
            </svg>
            <span>{{ item.label }}</span>
          </RouterLink>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<style scoped lang="scss">
.app-sidebar {
  display: flex;
  width: 100%;
  height: 100%;
  min-height: 100svh;
  flex-direction: column;
  padding: 1.5rem 1.25rem;
  border-right: 1px solid var(--p-content-border-color);
  background: var(--p-content-background);
}

.app-sidebar__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 0 0.5rem;
}

.sidebar-brand {
  display: inline-flex;
  min-width: 0;
  align-items: center;
  gap: 0.75rem;
  color: var(--p-text-color);
}

.sidebar-brand:hover {
  text-decoration: none;
}

.sidebar-brand__copy {
  display: grid;
  min-width: 0;
  gap: 0.1rem;
}

.sidebar-brand__copy strong {
  font-size: 1rem;
  line-height: 1.25;
  letter-spacing: -0.02em;
}

.sidebar-brand__copy span {
  overflow: hidden;
  color: var(--p-text-muted-color);
  font-size: 0.75rem;
  font-weight: 400;
  line-height: 1.25;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.app-sidebar__close {
  display: grid;
  width: 2.25rem;
  height: 2.25rem;
  flex: 0 0 auto;
  place-items: center;
  border: 0;
  border-radius: var(--p-border-radius-md);
  background: transparent;
  color: var(--p-text-muted-color);
  cursor: pointer;
}

.app-sidebar__close:hover {
  background: var(--p-surface-100);
  color: var(--p-text-color);
}

.app-sidebar__close:focus-visible {
  outline: 2px solid var(--p-primary-color);
  outline-offset: 2px;
}

.app-sidebar__close svg,
.workspace-navigation__link svg {
  width: 1.25rem;
  height: 1.25rem;
  fill: currentcolor;
}

.organization-field {
  display: grid;
  gap: 0.5rem;
  margin-top: 2rem;
}

.organization-field label {
  color: var(--p-text-muted-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.organization-select {
  width: 100%;
}

.organization-option {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 0.65rem;
}

.organization-option__initials {
  display: grid;
  width: 2rem;
  height: 2rem;
  flex: 0 0 auto;
  place-items: center;
  border-radius: var(--p-border-radius-md);
  background: var(--p-primary-50);
  color: var(--p-primary-700);
  font-size: 0.7rem;
  font-weight: 700;
}

.organization-option__copy {
  display: grid;
  min-width: 0;
  gap: 0.1rem;
}

.organization-option__copy strong,
.organization-option__copy span {
  overflow: hidden;
  line-height: 1.2;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.organization-option__copy strong {
  color: var(--p-text-color);
  font-size: 0.8rem;
  font-weight: 600;
}

.organization-option__copy span {
  color: var(--p-text-muted-color);
  font-size: 0.7rem;
  font-weight: 400;
}

.workspace-navigation {
  margin-top: 2rem;
}

.workspace-navigation__title {
  padding: 0 0.75rem;
  margin: 0 0 0.625rem;
  color: var(--p-text-muted-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.workspace-navigation ul {
  display: grid;
  gap: 0.25rem;
  padding: 0;
  margin: 0;
  list-style: none;
}

.workspace-navigation__link {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  border-radius: var(--p-border-radius-md);
  padding: 0.7rem 0.75rem;
  color: var(--p-text-muted-color);
  font-size: 0.875rem;
  font-weight: 600;
  transition:
    background-color 150ms ease,
    color 150ms ease;
}

.workspace-navigation__link:hover {
  background: var(--p-surface-100);
  color: var(--p-text-color);
  text-decoration: none;
}

.workspace-navigation__link--active,
.workspace-navigation__link--active:hover {
  background: var(--p-primary-50);
  color: var(--p-primary-700);
}

.workspace-navigation__link:focus-visible {
  outline: 2px solid var(--p-primary-color);
  outline-offset: 2px;
}
</style>
