import { http } from '../../../shared/api/http';
import type { CreateSiteRequest, Site } from '../types/site.types';

export const siteApi = {
  async list(organizationId: string): Promise<Site[]> {
    const response = await http.get<Site[]>(`/organizations/${organizationId}/sites`);

    return response.data;
  },

  async create(organizationId: string, request: CreateSiteRequest): Promise<void> {
    await http.post<void>(`/organizations/${organizationId}/sites`, request);
  },
};
