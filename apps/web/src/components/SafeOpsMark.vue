<template>
  <span
    class="safeops-mark"
    aria-hidden="true"
  >
    ✓
  </span>
</template>

<style scoped lang="scss">
.safeops-mark {
  display: grid;
  width: 2.25rem;
  height: 2.25rem;
  flex: 0 0 auto;
  place-items: center;
  border-radius: 0.7rem;
  background: var(--p-primary-color);
  box-shadow: 0 0.5rem 1.25rem var(--p-primary-50);
  color: var(--p-primary-contrast-color);
  font-size: 1.25rem;
  font-weight: 700;
}
</style>
