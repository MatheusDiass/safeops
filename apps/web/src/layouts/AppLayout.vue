<script setup lang="ts">
import { mdiBellOutline, mdiLogoutVariant, mdiMagnify, mdiMenu } from '@mdi/js';
import Button from 'primevue/button';
import Drawer from 'primevue/drawer';
import InputText from 'primevue/inputtext';
import { ref } from 'vue';
import AppSidebar from '../components/AppSidebar.vue';
import SafeOpsMark from '../components/SafeOpsMark.vue';

const isNavigationOpen = ref(false);
const searchQuery = ref('');
</script>

<template>
  <div class="app-layout">
    <div class="app-layout__desktop-sidebar">
      <AppSidebar />
    </div>

    <Drawer
      v-model:visible="isNavigationOpen"
      position="left"
      :show-close-icon="false"
      block-scroll
      :pt="{ root: { style: 'width: min(19rem, 90vw)' } }"
    >
      <template #container="{ closeCallback }">
        <AppSidebar
          show-close-button
          @close="closeCallback"
        />
      </template>
    </Drawer>

    <div class="app-layout__body">
      <header class="app-layout__topbar">
        <button
          class="app-layout__menu-button"
          type="button"
          aria-label="Open navigation"
          @click="isNavigationOpen = true"
        >
          <svg
            viewBox="0 0 24 24"
            aria-hidden="true"
          >
            <path :d="mdiMenu" />
          </svg>
        </button>

        <div class="topbar-mobile-brand">
          <SafeOpsMark />
          <span class="topbar-mobile-brand__copy">
            <strong>SafeOps</strong>
            <span>Safety operations</span>
          </span>
        </div>

        <div class="topbar-search">
          <svg
            viewBox="0 0 24 24"
            aria-hidden="true"
          >
            <path :d="mdiMagnify" />
          </svg>
          <InputText
            v-model="searchQuery"
            class="topbar-search__input"
            type="search"
            placeholder="Search incidents, sites, people"
            aria-label="Search incidents, sites, people"
          />
        </div>

        <div class="topbar-actions">
          <Button
            type="button"
            severity="secondary"
            text
            rounded
            aria-label="Notifications"
          >
            <svg
              viewBox="0 0 24 24"
              aria-hidden="true"
            >
              <path :d="mdiBellOutline" />
            </svg>
          </Button>

          <Button
            type="button"
            severity="secondary"
            outlined
            size="small"
            aria-label="Logout"
          >
            <svg
              viewBox="0 0 24 24"
              aria-hidden="true"
            >
              <path :d="mdiLogoutVariant" />
            </svg>
            <span class="topbar-actions__logout-label">Logout</span>
          </Button>
        </div>
      </header>

      <RouterView />
    </div>
  </div>
</template>

<style scoped lang="scss">
.app-layout {
  display: grid;
  min-height: 100svh;
  grid-template-columns: 19rem minmax(0, 1fr);
  background: var(--p-surface-50);
}

.app-layout__desktop-sidebar {
  position: sticky;
  top: 0;
  height: 100svh;
}

.app-layout__body {
  min-width: 0;
}

.app-layout__topbar {
  position: sticky;
  z-index: 10;
  top: 0;
  display: flex;
  min-height: 4.5rem;
  align-items: center;
  gap: 1rem;
  padding: 0 2rem;
  border-bottom: 1px solid var(--p-content-border-color);
  background: var(--p-content-background);
}

.app-layout__menu-button {
  display: none;
}

.topbar-mobile-brand {
  display: none;
}

.topbar-search {
  position: relative;
  width: auto;
  min-width: 8rem;
  max-width: 28rem;
  flex: 1 1 28rem;
  margin-right: auto;
}

.topbar-search > svg {
  position: absolute;
  z-index: 1;
  top: 50%;
  left: 0.875rem;
  width: 1.125rem;
  height: 1.125rem;
  fill: var(--p-text-muted-color);
  pointer-events: none;
  transform: translateY(-50%);
}

.topbar-search__input {
  width: 100%;
  padding-left: 2.6rem;
}

.topbar-actions {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  gap: 0.5rem;
}

.topbar-actions svg {
  width: 1.2rem;
  height: 1.2rem;
  fill: currentcolor;
}

@media (max-width: 900px) {
  .app-layout {
    display: block;
  }

  .app-layout__desktop-sidebar {
    display: none;
  }

  .app-layout__topbar {
    height: 4rem;
    gap: 0.75rem;
    padding: 0 1.25rem;
  }

  .app-layout__menu-button {
    display: grid;
    width: 2.5rem;
    height: 2.5rem;
    place-items: center;
    border: 0;
    border-radius: var(--p-border-radius-md);
    background: transparent;
    color: var(--p-text-color);
    cursor: pointer;
  }

  .app-layout__menu-button:hover {
    background: var(--p-surface-100);
  }

  .app-layout__menu-button:focus-visible {
    outline: 2px solid var(--p-primary-color);
    outline-offset: 2px;
  }

  .app-layout__menu-button svg {
    width: 1.5rem;
    height: 1.5rem;
    fill: currentcolor;
  }

  .topbar-mobile-brand {
    display: inline-flex;
    min-width: 0;
    align-items: center;
    gap: 0.75rem;
    margin-right: auto;
    color: var(--p-text-color);
  }

  .topbar-mobile-brand__copy {
    display: grid;
    min-width: 0;
    gap: 0.1rem;
  }

  .topbar-mobile-brand__copy strong {
    font-size: 1rem;
    line-height: 1.25;
    letter-spacing: -0.02em;
  }

  .topbar-mobile-brand__copy span {
    overflow: hidden;
    color: var(--p-text-muted-color);
    font-size: 0.75rem;
    font-weight: 400;
    line-height: 1.25;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .topbar-search {
    display: none;
  }
}

@media (max-width: 560px) {
  .app-layout__topbar {
    gap: 0.5rem;
    padding: 0 0.75rem;
  }

  .topbar-actions {
    gap: 0.25rem;
  }

  .topbar-actions__logout-label {
    display: none;
  }
}
</style>
