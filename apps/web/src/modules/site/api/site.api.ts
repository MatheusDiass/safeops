import { http } from '../../../shared/api/http';
import type { CreateSiteRequest, Site, UpdateSiteRequest } from '../types/site.types';

export const siteApi = {
  async list(organizationId: string): Promise<Site[]> {
    const response = await http.get<Site[]>(`/organizations/${organizationId}/sites`);

    return response.data;
  },

  async get(organizationId: string, siteId: string): Promise<Site> {
    const response = await http.get<Site>(`/organizations/${organizationId}/sites/${siteId}`);

    return response.data;
  },

  async create(organizationId: string, request: CreateSiteRequest): Promise<void> {
    await http.post<void>(`/organizations/${organizationId}/sites`, request);
  },

  async update(organizationId: string, siteId: string, request: UpdateSiteRequest): Promise<void> {
    await http.patch<void>(`/organizations/${organizationId}/sites/${siteId}`, request);
  },
};
