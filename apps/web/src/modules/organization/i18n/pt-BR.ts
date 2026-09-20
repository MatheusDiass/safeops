export default {
  organizations: {
    title: 'Organizações',
    workspace: 'Espaço de trabalho',
    summary: {
      singular: 'Você pertence a {count} organização.',
      plural: 'Você pertence a {count} organizações.',
    },
    actions: {
      create: 'Criar organização',
      new: 'Nova organização',
      delete: 'Excluir organização',
    },
    search: {
      placeholder: 'Buscar organizações',
      label: 'Buscar organizações por nome',
      noResultsTitle: 'Nenhuma organização encontrada',
      noResultsDescription: 'Tente buscar outro nome de organização.',
    },
    empty: {
      title: 'Crie sua primeira organização',
      description:
        'As organizações reúnem seus locais e operações de segurança em um único espaço de trabalho.',
    },
    create: {
      title: 'Criar organização',
      description:
        'Adicione uma organização para começar a gerenciar seus locais e operações de segurança.',
    },
    edit: {
      title: 'Editar organização',
      description: 'Atualize o nome e a disponibilidade da organização em seu espaço de trabalho.',
      detailsTitle: 'Detalhes da organização',
      detailsDescription: 'Gerencie os dados de identificação e o status atual desta organização.',
    },
    fields: {
      name: {
        label: 'Nome da organização',
        hint: 'Use entre 3 e 150 caracteres.',
      },
      status: {
        label: 'Status',
        hint: 'Organizações desabilitadas não ficam disponíveis para operações de segurança ativas.',
      },
      selector: {
        label: 'Organização',
      },
    },
    validation: {
      nameRequired: 'O nome da organização é obrigatório.',
      nameMin: 'O nome da organização deve ter pelo menos 3 caracteres.',
      nameMax: 'O nome da organização deve ter no máximo 150 caracteres.',
      statusRequired: 'O status da organização é obrigatório.',
    },
    status: {
      ACTIVE: 'Ativa',
      DISABLED: 'Desabilitada',
    },
    sites: {
      singular: '{count} local',
      plural: '{count} locais',
    },
    loading: 'Carregando organização...',
    loadingLabel: 'Carregando organização',
    errors: {
      notFound: 'Organização não encontrada.',
      load: 'Não foi possível carregar a organização. Tente novamente.',
      create: 'Não foi possível criar a organização. Tente novamente.',
      update: 'Não foi possível atualizar a organização. Tente novamente.',
    },
    dangerZone: {
      title: 'Zona de perigo',
      description: 'Excluir uma organização é permanente e não pode ser desfeito.',
      unavailable: 'Ainda não disponível',
    },
    aria: {
      list: 'Organizações',
      edit: 'Editar {name}',
    },
  },
};
