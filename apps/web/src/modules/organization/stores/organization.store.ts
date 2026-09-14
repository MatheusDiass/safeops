import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
  createOrganization as createOrganizationRequest,
  listOrganizations,
} from '../api/organization.api';
import type { CreateOrganizationRequest, Organization } from '../types/organization.types';

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

    organizations.value.push(organization);
    selectedOrganizationId.value = organization.id;
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
    loadOrganizations,
  };
});
