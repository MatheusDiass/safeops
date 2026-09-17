export type OrganizationStatus = 'ACTIVE' | 'DISABLED';

export type CreateOrganizationRequest = {
  name: string;
};

export type UpdateOrganizationRequest = {
  name?: string;
  status?: OrganizationStatus;
};

export type Organization = {
  id: string;
  name: string;
  status: OrganizationStatus;
  siteCount: number;
  createdAt: string;
  updatedAt: string | null;
};
