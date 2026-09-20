import { http } from '../../../shared/api/http';
import type { Site } from '../types/site.types';

export const siteApi = {
  async list(organizationId: string): Promise<Site[]> {
    const response = await http.get<Site[]>(`/organizations/${organizationId}/sites`);

    return response.data;
  },
};
