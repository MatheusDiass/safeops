import { defineStore } from 'pinia';
import { ref } from 'vue';
import { organizationApi } from '../api/organization.api';
import type {
  CreateOrganizationRequest,
  Organization,
  UpdateOrganizationRequest,
} from '../types/organization.types';

export const useOrganizationStore = defineStore('organization', () => {
  const organizations = ref<Organization[]>([]);
  const selectedOrganizationId = ref<string | null>(null);
  let organizationsRequest: Promise<Organization[]> | undefined;

  async function loadOrganizations(): Promise<Organization[]> {
    organizationsRequest ??= organizationApi.list();
    const request = organizationsRequest;

    try {
      const availableOrganizations = await request;
      organizations.value = availableOrganizations;

      const selectionIsAvailable = availableOrganizations.some(
        (organization) => organization.id === selectedOrganizationId.value,
      );
      if (!selectionIsAvailable) {
        selectedOrganizationId.value = availableOrganizations[0]?.id ?? null;
      }

      return availableOrganizations;
    } finally {
      if (organizationsRequest === request) {
        organizationsRequest = undefined;
      }
    }
  }

  async function createOrganization(request: CreateOrganizationRequest): Promise<void> {
    const organization = await organizationApi.create(request);

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
    const organization = await organizationApi.get(organizationId);
    upsertOrganization(organization);

    return organization;
  }

  async function updateOrganization(
    organizationId: string,
    request: UpdateOrganizationRequest,
  ): Promise<void> {
    const organization = await organizationApi.update(organizationId, request);
    upsertOrganization(organization);
  }

  function clear(): void {
    organizationsRequest = undefined;
    organizations.value = [];
    selectedOrganizationId.value = null;
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
