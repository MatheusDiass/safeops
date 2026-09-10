<template>
  <main class="auth-layout">
    <section
      class="auth-layout__content"
      aria-labelledby="page-title"
    >
      <div class="auth-layout__form-column">
        <a
          class="brand"
          href="/"
          aria-label="SafeOps home"
          ><span
            class="brand__mark"
            aria-hidden="true"
            >✓</span
          ><span>SafeOps</span></a
        >
        <h1 id="page-title"><slot name="title" /></h1>
        <p class="auth-layout__subtitle"><slot name="subtitle" /></p>
        <div class="auth-layout__body"><slot /></div>
        <footer
          v-if="$slots.footer"
          class="auth-layout__footer"
        >
          <slot name="footer" />
        </footer>
      </div>
    </section>
    <aside
      class="auth-layout__aside"
      aria-label="SafeOps overview"
    >
      <div class="auth-layout__aside-content">
        <div>
          <p class="eyebrow">Workplace safety, operationalised</p>
          <h2>Every site. Every incident. One accountable trail.</h2>
          <p class="auth-layout__aside-copy">
            SafeOps gives HSE teams a single operational view across organizations, sites and
            reported incidents — from first report to sign-off.
          </p>
        </div>
        <ul class="auth-layout__highlights">
          <li>
            <span
              class="highlight-icon"
              aria-hidden="true"
              >▦</span
            >12 organizations onboarded this quarter
          </li>
          <li>
            <span
              class="highlight-icon highlight-icon--success"
              aria-hidden="true"
              >⌁</span
            >4,812 incidents triaged in the last 12 months
          </li>
          <li>
            <span
              class="highlight-icon highlight-icon--warning"
              aria-hidden="true"
              >↗</span
            >31% faster critical response time
          </li>
        </ul>
      </div>
    </aside>
  </main>
</template>
