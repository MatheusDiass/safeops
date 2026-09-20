export default {
  auth: {
    fields: {
      email: 'E-mail corporativo',
      password: 'Senha',
    },
    login: {
      title: 'Entre no SafeOps',
      subtitle: 'Use sua conta corporativa para acessar suas organizações e fila de incidentes.',
      actions: {
        forgotPassword: 'Esqueceu a senha?',
        submit: 'Entrar',
      },
      registrationPrompt: 'Ainda não tem uma conta?',
      registrationLink: 'Crie uma',
      errors: {
        generic: 'Não foi possível entrar. Tente novamente.',
      },
    },
    registration: {
      title: 'Crie sua conta SafeOps',
      subtitle: 'Configure sua conta corporativa para começar a gerenciar operações de segurança.',
      fields: {
        name: 'Nome completo',
        confirmPassword: 'Confirme a senha',
      },
      passwordHint: 'Use entre 15 e 128 caracteres.',
      passwordMismatch: 'As senhas não coincidem.',
      success: 'Sua conta foi criada. Agora você pode entrar.',
      actions: {
        submit: 'Criar conta',
      },
      loginPrompt: 'Já tem uma conta?',
      loginLink: 'Entrar',
      errors: {
        generic: 'Não foi possível criar sua conta. Tente novamente.',
      },
    },
  },
};
