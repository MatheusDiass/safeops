import { createRouter, createWebHistory } from 'vue-router';
import AppLayout from '../../layouts/AppLayout.vue';
import LoginPage from '../../modules/auth/pages/LoginPage.vue';
import RegisterPage from '../../modules/auth/pages/RegisterPage.vue';
import CreateOrganizationPage from '../../modules/organization/pages/CreateOrganizationPage.vue';
import EditOrganizationPage from '../../modules/organization/pages/EditOrganizationPage.vue';
import OrganizationsPage from '../../modules/organization/pages/OrganizationsPage.vue';
import CreateSitePage from '../../modules/site/pages/CreateSitePage.vue';
import EditSitePage from '../../modules/site/pages/EditSitePage.vue';
import SitesPage from '../../modules/site/pages/SitesPage.vue';
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
          path: 'organization/:organizationId/edit',
          name: 'edit-organization',
          component: EditOrganizationPage,
          meta: {
            title: 'Edit organization',
            description: 'Update your organization details and status.',
          },
        },
        {
          path: 'sites',
          name: 'sites',
          component: SitesPage,
          meta: {
            title: 'Sites',
            description: 'View and manage the sites connected to this organization.',
          },
        },
        {
          path: 'sites/new',
          name: 'create-site',
          component: CreateSitePage,
          meta: {
            title: 'Create site',
            description: 'Create a new site for the selected organization.',
          },
        },
        {
          path: 'sites/:siteId/edit',
          name: 'edit-site',
          component: EditSitePage,
          meta: {
            title: 'Edit site',
            description: 'Update the selected site details and status.',
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
