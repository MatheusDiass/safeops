import { http } from '../../../shared/api/http';
import type {
  CreateOrganizationRequest,
  Organization,
  UpdateOrganizationRequest,
} from '../types/organization.types';

export async function createOrganization(
  request: CreateOrganizationRequest,
): Promise<Organization> {
  const response = await http.post<Organization>('/organizations', request);

  return response.data;
}

export async function listOrganizations(): Promise<Organization[]> {
  const response = await http.get<Organization[]>('/organizations');

  return response.data;
}

export async function getOrganization(organizationId: string): Promise<Organization> {
  const response = await http.get<Organization>(`/organizations/${organizationId}`);

  return response.data;
}

export async function updateOrganization(
  organizationId: string,
  request: UpdateOrganizationRequest,
): Promise<Organization> {
  const response = await http.patch<Organization>(`/organizations/${organizationId}`, request);

  return response.data;
}
