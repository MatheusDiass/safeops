import Aura from '@primeuix/themes/aura';
import { definePreset } from '@primeuix/themes';

export const SafeOpsPreset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '#DBEAFE',
      100: '#DBEAFE',
      200: '#DBEAFE',
      300: '#3B82F6',
      400: '#3B82F6',
      500: '#2563EB',
      600: '#2563EB',
      700: '#1D4ED8',
      800: '#1D4ED8',
      900: '#1D4ED8',
      950: '#0D1318',
    },
    colorScheme: {
      light: {
        surface: {
          0: '#FFFFFF',
          50: '#F8FAFC',
          100: '#F8FAFC',
          200: '#E2E8F0',
          300: '#E2E8F0',
          400: '#9498A2',
          500: '#607585',
          600: '#607585',
          700: '#3E4653',
          800: '#3E4653',
          900: '#0D1318',
          950: '#0D1318',
        },
      },
    },
  },
});
