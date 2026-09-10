import { createApp } from 'vue';
import PrimeVue from 'primevue/config';
import './style.css';
import App from './App.vue';
import { SafeOpsPreset } from './app/theme/safeops.preset';

createApp(App)
  .use(PrimeVue, {
    theme: {
      preset: SafeOpsPreset,
      options: {
        darkModeSelector: 'none',
      },
    },
  })
  .mount('#app');
