import { http } from '../../../shared/api/http';
import type {
  CreateOrganizationRequest,
  Organization,
  UpdateOrganizationRequest,
} from '../types/organization.types';

export const organizationApi = {
  async create(request: CreateOrganizationRequest): Promise<Organization> {
    const response = await http.post<Organization>('/organizations', request);

    return response.data;
  },

  async list(): Promise<Organization[]> {
    const response = await http.get<Organization[]>('/organizations');

    return response.data;
  },

  async get(organizationId: string): Promise<Organization> {
    const response = await http.get<Organization>(`/organizations/${organizationId}`);

    return response.data;
  },

  async update(
    organizationId: string,
    request: UpdateOrganizationRequest,
  ): Promise<Organization> {
    const response = await http.patch<Organization>(`/organizations/${organizationId}`, request);

    return response.data;
  },
};
