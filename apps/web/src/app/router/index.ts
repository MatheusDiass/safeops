import { createRouter, createWebHistory } from 'vue-router';
import AppLayout from '../../layouts/AppLayout.vue';
import LoginPage from '../../modules/auth/pages/LoginPage.vue';
import RegisterPage from '../../modules/auth/pages/RegisterPage.vue';
import CreateOrganizationPage from '../../modules/organization/pages/CreateOrganizationPage.vue';
import OrganizationsPage from '../../modules/organization/pages/OrganizationsPage.vue';
import WorkspacePage from '../../modules/workspace/pages/WorkspacePage.vue';

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: AppLayout,
      meta: { requiresAuth: true },
      children: [
        { path: '', redirect: { name: 'dashboard' } },
        {
          path: 'dashboard',
          name: 'dashboard',
          component: WorkspacePage,
          meta: {
            title: 'Dashboard',
            description: 'Monitor safety performance across your organization and sites.',
          },
        },
        {
          path: 'organization',
          name: 'organization',
          component: OrganizationsPage,
          meta: {
            title: 'Organizations',
            description: 'Manage the organizations available to your account.',
          },
        },
        {
          path: 'organization/new',
          name: 'create-organization',
          component: CreateOrganizationPage,
          meta: {
            title: 'Create organization',
            description: 'Create a new organization for your safety operations.',
          },
        },
        {
          path: 'sites',
          name: 'sites',
          component: WorkspacePage,
          meta: {
            title: 'Sites',
            description: 'View and manage the sites connected to this organization.',
          },
        },
        {
          path: 'incidents',
          name: 'incidents',
          component: WorkspacePage,
          meta: {
            title: 'Incidents',
            description: 'Review reported incidents and follow their response progress.',
          },
        },
        {
          path: 'account',
          name: 'account',
          component: WorkspacePage,
          meta: {
            title: 'Account',
            description: 'Manage your personal account and preferences.',
          },
        },
      ],
    },
    { path: '/login', name: 'login', component: LoginPage, meta: { guestOnly: true } },
    { path: '/register', name: 'register', component: RegisterPage, meta: { guestOnly: true } },
  ],
});
