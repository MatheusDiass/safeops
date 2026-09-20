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
    fields: {
      name: {
        label: 'Nome do local',
        hint: 'Use entre 3 e 150 caracteres.',
      },
    },
    validation: {
      nameRequired: 'O nome do local é obrigatório.',
      nameMin: 'O nome do local deve ter pelo menos 3 caracteres.',
      nameMax: 'O nome do local deve ter no máximo 150 caracteres.',
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
      create: 'Não foi possível criar o local. Tente novamente.',
      load: 'Não foi possível carregar os locais. Tente novamente.',
    },
    aria: {
      list: 'Locais',
    },
  },
};
