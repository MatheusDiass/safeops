export default {
  sites: {
    title: 'Sites',
    summary: {
      singular: '{count} site in {organization}.',
      plural: '{count} sites in {organization}.',
    },
    actions: {
      create: 'Create site',
      new: 'New site',
    },
    search: {
      placeholder: 'Search sites',
      label: 'Search sites by name',
      noResultsTitle: 'No sites found',
      noResultsDescription: 'Try searching for a different site name.',
    },
    empty: {
      title: 'Create your first site',
      description: 'Add the first site for {organization} to start managing it.',
    },
    create: {
      title: 'Create site',
      description: 'Add a site to {organization} to manage its safety operations.',
    },
    edit: {
      title: 'Edit site',
      description: 'Update the site name and availability across your workspace.',
      detailsTitle: 'Site details',
      detailsDescription: 'Manage the identifying details and current status of this site.',
    },
    fields: {
      name: {
        label: 'Site name',
        hint: 'Use between 3 and 150 characters.',
      },
      status: {
        label: 'Status',
        hint: 'Disabled sites are unavailable for active safety operations.',
      },
    },
    validation: {
      nameRequired: 'Site name is required.',
      nameMin: 'Site name must have at least 3 characters.',
      nameMax: 'Site name must have at most 150 characters.',
      statusRequired: 'Site status is required.',
    },
    selectOrganization: {
      title: 'Select an organization',
      description: 'Select an organization from the navigation to view its sites.',
    },
    status: {
      ACTIVE: 'Active',
      DISABLED: 'Disabled',
    },
    createdAt: 'Created {date}',
    loading: 'Loading sites...',
    loadingLabel: 'Loading sites',
    errors: {
      notFound: 'Site not found.',
      create: 'Unable to create the site. Please try again.',
      load: 'Unable to load the sites. Please try again.',
      update: 'Unable to update the site. Please try again.',
    },
    aria: {
      list: 'Sites',
      edit: 'Edit {name}',
    },
  },
};
