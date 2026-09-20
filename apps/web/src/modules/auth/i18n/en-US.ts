export default {
  auth: {
    fields: {
      email: 'Work email',
      password: 'Password',
    },
    login: {
      title: 'Sign in to SafeOps',
      subtitle: 'Use your work account to access your organizations and incident queue.',
      actions: {
        forgotPassword: 'Forgot password?',
        submit: 'Sign in',
      },
      registrationPrompt: "Don't have an account?",
      registrationLink: 'Create one',
      errors: {
        generic: 'Unable to sign in. Please try again.',
      },
    },
    registration: {
      title: 'Create your SafeOps account',
      subtitle: 'Set up your work account to start managing safety operations.',
      fields: {
        name: 'Full name',
        confirmPassword: 'Confirm password',
      },
      passwordHint: 'Use between 15 and 128 characters.',
      passwordMismatch: 'Passwords do not match.',
      success: 'Your account has been created. You can now sign in.',
      actions: {
        submit: 'Create account',
      },
      loginPrompt: 'Already have an account?',
      loginLink: 'Sign in',
      errors: {
        generic: 'Unable to create your account. Please try again.',
      },
    },
  },
};
