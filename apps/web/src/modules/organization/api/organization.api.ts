import { http } from '../../../shared/api/http';
import type { CreateOrganizationRequest, Organization } from '../types/organization.types';

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
