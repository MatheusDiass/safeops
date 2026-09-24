export default {
  incidents: {
    title: 'Incidentes',
    description: 'Revise os incidentes reportados e acompanhe o andamento das respostas.',
    actions: {
      report: 'Reportar incidente',
      resetFilters: 'Limpar filtros',
    },
    summary: {
      label: 'Resumo de incidentes',
      reported: 'Reportados',
      underReview: 'Em análise',
      actionRequired: 'Ação necessária',
      closed: 'Encerrados',
    },
    search: {
      placeholder: 'Buscar incidentes',
      label: 'Buscar por título, local, unidade ou relator',
      noResultsTitle: 'Nenhum incidente encontrado',
      noResultsDescription: 'Tente alterar a busca ou os filtros.',
    },
    filters: {
      label: 'Filtros de incidentes',
      severity: 'Severidade',
      status: 'Status',
      type: 'Tipo',
      allSeverities: 'Todas as severidades',
      allStatuses: 'Todos os status',
      allTypes: 'Todos os tipos',
    },
    results: {
      singular: '{count} incidente',
      plural: '{count} incidentes',
    },
    columns: {
      incident: 'Incidente',
      severity: 'Severidade',
      status: 'Status',
      type: 'Tipo',
      site: 'Unidade',
      occurredAt: 'Data da ocorrência',
      reporter: 'Relator',
    },
    status: {
      REPORTED: 'Reportado',
      UNDER_REVIEW: 'Em análise',
      ACTION_REQUIRED: 'Ação necessária',
      CLOSED: 'Encerrado',
    },
    severity: {
      LOW: 'Baixa',
      MEDIUM: 'Média',
      HIGH: 'Alta',
      CRITICAL: 'Crítica',
      notAssigned: 'Não atribuída',
    },
    type: {
      ACCIDENT: 'Acidente',
      NEAR_MISS: 'Quase acidente',
      UNSAFE_CONDITION: 'Condição insegura',
      ENVIRONMENTAL: 'Ambiental',
    },
    empty: {
      title: 'Nenhum incidente reportado',
      description:
        'Reporte o primeiro incidente de {organization} para começar a acompanhar a resposta.',
    },
    selectOrganization: {
      title: 'Selecione uma organização',
      description: 'Selecione uma organização na navegação para visualizar seus incidentes.',
    },
    loading: 'Carregando incidentes...',
    loadingLabel: 'Carregando incidentes',
    errors: {
      load: 'Não foi possível carregar os incidentes. Tente novamente.',
    },
    aria: {
      list: 'Incidentes',
      open: 'Abrir incidente: {title}',
    },
  },
};
