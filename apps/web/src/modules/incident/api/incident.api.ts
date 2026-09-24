import { http } from '../../../shared/api/http';
import type { Incident } from '../types/incident.types';

export const incidentApi = {
  async list(organizationId: string): Promise<Incident[]> {
    const response = await http.get<Incident[]>(`/organizations/${organizationId}/incidents`);

    return response.data;
  },
};
