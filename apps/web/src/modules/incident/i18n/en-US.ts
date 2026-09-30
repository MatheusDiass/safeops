export default {
  incidents: {
    title: 'Incidents',
    description: 'Review reported incidents and follow their response progress.',
    actions: {
      report: 'Report incident',
      resetFilters: 'Reset filters',
    },
    create: {
      title: 'Report an incident',
      description: 'Capture what happened while it is fresh. You can update the record later.',
      sections: {
        location: 'Where it happened',
        details: 'What happened',
        reporter: 'Reporter',
      },
      fields: {
        site: {
          label: 'Site',
          placeholder: 'Select a site',
        },
        location: {
          label: 'Exact location · Optional',
          hint: 'Building, line, room or equipment tag.',
        },
        title: {
          label: 'Title',
          placeholder: 'Short summary of the event',
        },
        type: {
          label: 'Incident type',
          placeholder: 'Select an incident type',
        },
        occurredAt: {
          label: 'Occurred at',
        },
        severity: {
          label: 'Severity · Optional',
          hint: 'Severity helps prioritize review and response.',
        },
        description: {
          label: 'Description',
          placeholder: 'Describe the event objectively...',
          hint: 'What happened, who was involved, and the sequence of events.',
        },
        immediateActions: {
          label: 'Immediate actions taken · Optional',
          placeholder: 'Containment, first aid, lockout, area isolation...',
        },
      },
      reporter: {
        label: 'Reported by',
      },
      sites: {
        loading: 'Loading sites...',
        empty: 'This organization has no active sites available for incident reporting.',
      },
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
      create: 'Unable to report the incident. Please try again.',
    },
    validation: {
      siteRequired: 'Select a site.',
      locationMax: 'Exact location must be at most 150 characters.',
      titleRequired: 'Enter a title.',
      titleMax: 'Title must be at most 150 characters.',
      typeRequired: 'Select an incident type.',
      occurredAtRequired: 'Select when the incident occurred.',
      occurredAtFuture: 'Occurrence date cannot be in the future.',
      descriptionRequired: 'Enter a description.',
      descriptionMax: 'Description must be at most 3,000 characters.',
      immediateActionsMax: 'Immediate actions must be at most 5,000 characters.',
    },
    aria: {
      list: 'Incidents',
      open: 'Open incident: {title}',
    },
  },
};
