export const SITE_STATUSES = ['ACTIVE', 'DISABLED'] as const;

export type SiteStatus = (typeof SITE_STATUSES)[number];

export type Site = {
  id: string;
  name: string;
  status: SiteStatus;
  createdAt: string;
  updatedAt: string | null;
};

export type CreateSiteRequest = {
  name: string;
};
