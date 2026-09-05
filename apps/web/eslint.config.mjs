import js from '@eslint/js';
import { globalIgnores } from 'eslint/config';
import pluginVue from 'eslint-plugin-vue';
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript';
import eslintConfigPrettier from 'eslint-config-prettier/flat';

export default defineConfigWithVueTs(
  globalIgnores([
    'dist/**',
    'public/**',
  ]),

  js.configs.recommended,

  pluginVue.configs['flat/recommended'],

  vueTsConfigs.recommendedTypeChecked,

  {
    files: ['**/*.{ts,vue}'],

    rules: {
      /*
       * JavaScript
       */
      eqeqeq: ['error', 'always'],
      camelcase: 'error',
      'prefer-const': 'error',
      'no-var': 'error',
      'no-duplicate-imports': 'error',
      'no-multi-assign': 'error',
      'prefer-template': 'error',
      'object-shorthand': ['error', 'always'],
      'arrow-body-style': ['error', 'as-needed'],
      'no-useless-catch': 'warn',

      'no-console': [
        'warn',
        {
          allow: ['warn', 'error'],
        },
      ],

      /*
       * TypeScript
       */
      'no-undef': 'off',
      'no-unused-vars': 'off',

      '@typescript-eslint/no-unused-vars': [
        'warn',
        {
          argsIgnorePattern: '^_',
          varsIgnorePattern: '^_',
          caughtErrorsIgnorePattern: '^_',
          ignoreRestSiblings: true,
        },
      ],

      '@typescript-eslint/prefer-optional-chain': 'error',

      /*
       * Vue
       */
      'vue/singleline-html-element-content-newline': 'off',
      'vue/multiline-html-element-content-newline': 'off',
    },
  },

  eslintConfigPrettier,
);