export type OrganizationStatus = 'ACTIVE' | 'DISABLED';

export type Organization = {
  id: string;
  name: string;
  status: OrganizationStatus;
  createdAt: string;
  updatedAt: string | null;
};
