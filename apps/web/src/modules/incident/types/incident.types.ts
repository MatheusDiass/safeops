export const INCIDENT_STATUSES = ['REPORTED', 'UNDER_REVIEW', 'ACTION_REQUIRED', 'CLOSED'] as const;

export const INCIDENT_SEVERITIES = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'] as const;

export const INCIDENT_TYPES = [
  'ACCIDENT',
  'NEAR_MISS',
  'UNSAFE_CONDITION',
  'ENVIRONMENTAL',
] as const;

export type IncidentStatus = (typeof INCIDENT_STATUSES)[number];
export type IncidentSeverity = (typeof INCIDENT_SEVERITIES)[number];
export type IncidentType = (typeof INCIDENT_TYPES)[number];

export type IncidentReporter = {
  id: string;
  name: string;
};

export type IncidentSite = {
  id: string;
  name: string;
};

export type Incident = {
  id: string;
  title: string;
  description: string;
  type: IncidentType;
  status: IncidentStatus;
  severity: IncidentSeverity | null;
  occurredAt: string;
  location: string | null;
  immediateActions: string | null;
  reportedBy: IncidentReporter;
  site: IncidentSite;
  closedAt: string | null;
  createdAt: string;
  updatedAt: string;
};
