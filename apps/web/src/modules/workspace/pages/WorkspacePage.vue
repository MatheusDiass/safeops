<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const title = computed(() =>
  typeof route.meta.title === 'string' ? route.meta.title : 'Workspace',
);
const description = computed(() =>
  typeof route.meta.description === 'string' ? route.meta.description : '',
);
</script>

<template>
  <main class="workspace-page">
    <p class="workspace-page__eyebrow">Workspace</p>
    <h1>{{ title }}</h1>
    <p>{{ description }}</p>
  </main>
</template>

<style scoped lang="scss">
.workspace-page {
  width: min(100%, 75rem);
  padding: 3rem clamp(1.25rem, 4vw, 4rem);
}

.workspace-page__eyebrow {
  margin: 0 0 0.5rem;
  color: var(--p-primary-color);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.workspace-page h1 {
  margin: 0;
}

.workspace-page > p:last-child {
  max-width: 42rem;
  margin: 0.75rem 0 0;
  color: var(--p-text-muted-color);
  line-height: 1.6;
}
</style>
