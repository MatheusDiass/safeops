import { http } from '../../../shared/api/http';
import type { CreateIncidentRequest, Incident } from '../types/incident.types';

export const incidentApi = {
  async list(organizationId: string): Promise<Incident[]> {
    const response = await http.get<Incident[]>(`/organizations/${organizationId}/incidents`);

    return response.data;
  },

  async create(organizationId: string, request: CreateIncidentRequest): Promise<void> {
    await http.post<void>(`/organizations/${organizationId}/incidents`, request);
  },
};
