import { http } from '../../../shared/api/http';
import type { Organization } from '../types/organization.types';

export async function listOrganizations(): Promise<Organization[]> {
  const response = await http.get<Organization[]>('/organizations');

  return response.data;
}
