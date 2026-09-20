export default {
  organizations: {
    title: 'Organizations',
    workspace: 'Workspace',
    summary: {
      singular: 'You belong to {count} organization.',
      plural: 'You belong to {count} organizations.',
    },
    actions: {
      create: 'Create organization',
      new: 'New organization',
      delete: 'Delete organization',
    },
    search: {
      placeholder: 'Search organizations',
      label: 'Search organizations by name',
      noResultsTitle: 'No organizations found',
      noResultsDescription: 'Try searching for a different organization name.',
    },
    empty: {
      title: 'Create your first organization',
      description:
        'Organizations bring your sites and safety operations together in one workspace.',
    },
    create: {
      title: 'Create organization',
      description: 'Add an organization to start managing its sites and safety operations.',
    },
    edit: {
      title: 'Edit organization',
      description: 'Update the organization name and availability across your workspace.',
      detailsTitle: 'Organization details',
      detailsDescription: 'Manage the identifying details and current status of this organization.',
    },
    fields: {
      name: {
        label: 'Organization name',
        hint: 'Use between 3 and 150 characters.',
      },
      status: {
        label: 'Status',
        hint: 'Disabled organizations are unavailable for active safety operations.',
      },
      selector: {
        label: 'Organization',
      },
    },
    validation: {
      nameRequired: 'Organization name is required.',
      nameMin: 'Organization name must have at least 3 characters.',
      nameMax: 'Organization name must have at most 150 characters.',
      statusRequired: 'Organization status is required.',
    },
    status: {
      ACTIVE: 'Active',
      DISABLED: 'Disabled',
    },
    sites: {
      singular: '{count} site',
      plural: '{count} sites',
    },
    loading: 'Loading organization...',
    loadingLabel: 'Loading organization',
    errors: {
      notFound: 'Organization not found.',
      load: 'Unable to load the organization. Please try again.',
      create: 'Unable to create the organization. Please try again.',
      update: 'Unable to update the organization. Please try again.',
    },
    dangerZone: {
      title: 'Danger zone',
      description: 'Deleting an organization is permanent and cannot be undone.',
      unavailable: 'Not available yet',
    },
    aria: {
      list: 'Organizations',
      edit: 'Edit {name}',
    },
  },
};
