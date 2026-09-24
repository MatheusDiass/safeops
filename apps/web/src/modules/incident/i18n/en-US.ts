export default {
  incidents: {
    title: 'Incidents',
    description: 'Review reported incidents and follow their response progress.',
    actions: {
      report: 'Report incident',
      resetFilters: 'Reset filters',
    },
    summary: {
      label: 'Incident summary',
      reported: 'Reported',
      underReview: 'Under review',
      actionRequired: 'Action required',
      closed: 'Closed',
    },
    search: {
      placeholder: 'Search incidents',
      label: 'Search by title, location, site, or reporter',
      noResultsTitle: 'No incidents found',
      noResultsDescription: 'Try changing your search or filters.',
    },
    filters: {
      label: 'Incident filters',
      severity: 'Severity',
      status: 'Status',
      type: 'Type',
      allSeverities: 'All severities',
      allStatuses: 'All statuses',
      allTypes: 'All types',
    },
    results: {
      singular: '{count} incident',
      plural: '{count} incidents',
    },
    columns: {
      incident: 'Incident',
      severity: 'Severity',
      status: 'Status',
      type: 'Type',
      site: 'Site',
      occurredAt: 'Occurrence date',
      reporter: 'Reporter',
    },
    status: {
      REPORTED: 'Reported',
      UNDER_REVIEW: 'Under review',
      ACTION_REQUIRED: 'Action required',
      CLOSED: 'Closed',
    },
    severity: {
      LOW: 'Low',
      MEDIUM: 'Medium',
      HIGH: 'High',
      CRITICAL: 'Critical',
      notAssigned: 'Not assigned',
    },
    type: {
      ACCIDENT: 'Accident',
      NEAR_MISS: 'Near miss',
      UNSAFE_CONDITION: 'Unsafe condition',
      ENVIRONMENTAL: 'Environmental',
    },
    empty: {
      title: 'No incidents reported',
      description: 'Report the first incident for {organization} to begin tracking its response.',
    },
    selectOrganization: {
      title: 'Select an organization',
      description: 'Select an organization from the navigation to view its incidents.',
    },
    loading: 'Loading incidents...',
    loadingLabel: 'Loading incidents',
    errors: {
      load: 'Unable to load the incidents. Please try again.',
    },
    aria: {
      list: 'Incidents',
      open: 'Open incident: {title}',
    },
  },
};
