export default {
  incidents: {
    title: 'Incidentes',
    description: 'Revise os incidentes reportados e acompanhe o andamento das respostas.',
    actions: {
      report: 'Reportar incidente',
      resetFilters: 'Limpar filtros',
    },
    create: {
      title: 'Reportar um incidente',
      description:
        'Registre o que aconteceu enquanto os detalhes estão recentes. Você poderá atualizar o registro depois.',
      sections: {
        location: 'Onde aconteceu',
        details: 'O que aconteceu',
        reporter: 'Relator',
      },
      fields: {
        site: {
          label: 'Local',
          placeholder: 'Selecione um local',
        },
        location: {
          label: 'Local exato · Opcional',
          hint: 'Prédio, linha, sala ou identificação do equipamento.',
        },
        title: {
          label: 'Título',
          placeholder: 'Resumo breve do evento',
        },
        type: {
          label: 'Tipo de incidente',
          placeholder: 'Selecione um tipo de incidente',
        },
        occurredAt: {
          label: 'Data e hora da ocorrência',
        },
        severity: {
          label: 'Severidade · Opcional',
          hint: 'A severidade ajuda a priorizar a análise e a resposta.',
        },
        description: {
          label: 'Descrição',
          placeholder: 'Descreva o evento de forma objetiva...',
          hint: 'O que aconteceu, quem estava envolvido e a sequência dos acontecimentos.',
        },
        immediateActions: {
          label: 'Ações imediatas tomadas · Opcional',
          placeholder: 'Contenção, primeiros socorros, bloqueio, isolamento da área...',
        },
      },
      reporter: {
        label: 'Reportado por',
      },
      sites: {
        loading: 'Carregando locais...',
        empty: 'Esta organização não possui locais ativos disponíveis para reportar incidentes.',
      },
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
      create: 'Não foi possível reportar o incidente. Tente novamente.',
    },
    validation: {
      siteRequired: 'Selecione um local.',
      locationMax: 'O local exato deve ter no máximo 150 caracteres.',
      titleRequired: 'Informe um título.',
      titleMax: 'O título deve ter no máximo 150 caracteres.',
      typeRequired: 'Selecione um tipo de incidente.',
      occurredAtRequired: 'Selecione quando o incidente aconteceu.',
      occurredAtFuture: 'A data da ocorrência não pode estar no futuro.',
      descriptionRequired: 'Informe uma descrição.',
      descriptionMax: 'A descrição deve ter no máximo 3.000 caracteres.',
      immediateActionsMax: 'As ações imediatas devem ter no máximo 5.000 caracteres.',
    },
    aria: {
      list: 'Incidentes',
      open: 'Abrir incidente: {title}',
    },
  },
};
