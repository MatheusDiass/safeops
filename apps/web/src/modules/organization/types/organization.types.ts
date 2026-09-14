export type OrganizationStatus = 'ACTIVE' | 'DISABLED';

export type CreateOrganizationRequest = {
  name: string;
};

export type Organization = {
  id: string;
  name: string;
  status: OrganizationStatus;
  siteCount: number;
  createdAt: string;
  updatedAt: string | null;
};
