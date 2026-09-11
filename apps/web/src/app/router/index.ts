import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../../modules/auth/pages/LoginPage.vue';
import RegisterPage from '../../modules/auth/pages/RegisterPage.vue';

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', component: LoginPage },
    { path: '/register', component: RegisterPage },
  ],
});
