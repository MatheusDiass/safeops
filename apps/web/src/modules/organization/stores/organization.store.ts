import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
  createOrganization as createOrganizationRequest,
  getOrganization as getOrganizationRequest,
  listOrganizations,
  updateOrganization as updateOrganizationRequest,
} from '../api/organization.api';
import type {
  CreateOrganizationRequest,
  Organization,
  UpdateOrganizationRequest,
} from '../types/organization.types';

export const useOrganizationStore = defineStore('organization', () => {
  const organizations = ref<Organization[]>([]);
  const selectedOrganizationId = ref<string | null>(null);
  let hasLoadedOrganizations = false;
  let organizationsRequest: Promise<Organization[]> | undefined;

  async function loadOrganizations(): Promise<Organization[]> {
    if (hasLoadedOrganizations) {
      return organizations.value;
    }

    organizationsRequest ??= listOrganizations();

    try {
      const availableOrganizations = await organizationsRequest;
      organizations.value = availableOrganizations;
      hasLoadedOrganizations = true;

      const selectionIsAvailable = availableOrganizations.some(
        (organization) => organization.id === selectedOrganizationId.value,
      );
      if (!selectionIsAvailable) {
        selectedOrganizationId.value = availableOrganizations[0]?.id ?? null;
      }

      return availableOrganizations;
    } finally {
      organizationsRequest = undefined;
    }
  }

  async function createOrganization(request: CreateOrganizationRequest): Promise<void> {
    const organization = await createOrganizationRequest(request);

    upsertOrganization(organization);
    selectedOrganizationId.value = organization.id;
  }

  function upsertOrganization(organization: Organization): void {
    const organizationIndex = organizations.value.findIndex(({ id }) => id === organization.id);

    if (organizationIndex === -1) {
      organizations.value.push(organization);
      return;
    }

    organizations.value.splice(organizationIndex, 1, organization);
  }

  async function loadOrganization(organizationId: string): Promise<Organization> {
    const organization = await getOrganizationRequest(organizationId);
    upsertOrganization(organization);

    return organization;
  }

  async function updateOrganization(
    organizationId: string,
    request: UpdateOrganizationRequest,
  ): Promise<void> {
    const organization = await updateOrganizationRequest(organizationId, request);
    upsertOrganization(organization);
  }

  function clear(): void {
    organizations.value = [];
    selectedOrganizationId.value = null;
    hasLoadedOrganizations = false;
  }

  return {
    organizations,
    selectedOrganizationId,
    clear,
    createOrganization,
    loadOrganization,
    loadOrganizations,
    updateOrganization,
  };
});
