import { createApp } from 'vue';
import { createPinia } from 'pinia';
import PrimeVue from 'primevue/config';
import './style.css';
import App from './App.vue';
import { initializeAuthenticatedApplication } from './app/initializeAuthenticatedApplication';
import { router } from './app/router';
import { SafeOpsPreset } from './app/theme/safeops.preset';
import { useAuthStore } from './modules/auth/stores/auth.store';
import { configureAccessTokenProvider } from './shared/api/http';

const app = createApp(App);
const pinia = createPinia();

app
  .use(PrimeVue, {
    theme: {
      preset: SafeOpsPreset,
      options: {
        darkModeSelector: 'none',
      },
    },
  })
  .use(pinia);

configureAccessTokenProvider(() => useAuthStore(pinia).accessToken);
await initializeAuthenticatedApplication(pinia);

router.beforeEach((to) => {
  const isAuthenticated = Boolean(useAuthStore(pinia).authenticatedUser);

  if (to.meta.requiresAuth && !isAuthenticated) {
    return { name: 'login' };
  }

  if (to.meta.guestOnly && isAuthenticated) {
    return { name: 'dashboard' };
  }
});

app.use(router);
await router.isReady();
app.mount('#app');
