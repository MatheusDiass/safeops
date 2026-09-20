export default {
  sites: {
    title: 'Locais',
    summary: {
      singular: '{count} local em {organization}.',
      plural: '{count} locais em {organization}.',
    },
    actions: {
      create: 'Criar local',
      new: 'Novo local',
    },
    search: {
      placeholder: 'Buscar locais',
      label: 'Buscar locais por nome',
      noResultsTitle: 'Nenhum local encontrado',
      noResultsDescription: 'Tente buscar outro nome de local.',
    },
    empty: {
      title: 'Crie seu primeiro local',
      description: 'Adicione o primeiro local de {organization} para começar a gerenciá-lo.',
    },
    create: {
      title: 'Criar local',
      description: 'Adicione um local a {organization} para gerenciar suas operações de segurança.',
    },
    edit: {
      title: 'Editar local',
      description: 'Atualize o nome e a disponibilidade do local em seu espaço de trabalho.',
      detailsTitle: 'Detalhes do local',
      detailsDescription: 'Gerencie os dados de identificação e o status atual deste local.',
    },
    fields: {
      name: {
        label: 'Nome do local',
        hint: 'Use entre 3 e 150 caracteres.',
      },
      status: {
        label: 'Status',
        hint: 'Locais desabilitados não ficam disponíveis para operações de segurança ativas.',
      },
    },
    validation: {
      nameRequired: 'O nome do local é obrigatório.',
      nameMin: 'O nome do local deve ter pelo menos 3 caracteres.',
      nameMax: 'O nome do local deve ter no máximo 150 caracteres.',
      statusRequired: 'O status do local é obrigatório.',
    },
    selectOrganization: {
      title: 'Selecione uma organização',
      description: 'Selecione uma organização na navegação para visualizar seus locais.',
    },
    status: {
      ACTIVE: 'Ativo',
      DISABLED: 'Desabilitado',
    },
    createdAt: 'Criado em {date}',
    loading: 'Carregando locais...',
    loadingLabel: 'Carregando locais',
    errors: {
      notFound: 'Local não encontrado.',
      create: 'Não foi possível criar o local. Tente novamente.',
      load: 'Não foi possível carregar os locais. Tente novamente.',
      update: 'Não foi possível atualizar o local. Tente novamente.',
    },
    aria: {
      list: 'Locais',
      edit: 'Editar {name}',
    },
  },
};
