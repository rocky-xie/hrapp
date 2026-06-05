import { decimal, helpers, integer, maxLength, maxValue, minLength, minValue, required, sameAs } from '@vuelidate/validators';

import i18n from '@/shared/config/i18n';

const requiredMessage = 'This field is required.';
const integerMessage = 'This field should be a number.';

const translate = (key: string, fallback: string, params?: Record<string, number>) => {
  const translated = i18n.global.t(key, params ?? {});
  return translated === key ? fallback : translated;
};

export const useValidation = () => {
  return {
    required: (message: string) => helpers.withMessage(() => translate('global.validation.required', message || requiredMessage), required),
    decimal: (message: string) => helpers.withMessage(message, decimal),
    integer: (message: string) => helpers.withMessage(() => translate('global.validation.integer', message || integerMessage), integer),
    sameAs: (message: string, ...args: Parameters<typeof sameAs>) => helpers.withMessage(message, sameAs(...args)),
    minLength: (message: string, ...args: Parameters<typeof minLength>) => helpers.withMessage(message, minLength(...args)),
    maxLength: (message: string, ...args: Parameters<typeof maxLength>) =>
      helpers.withMessage(() => translate('global.validation.maxLength', message, { max: Number(args[0]) }), maxLength(...args)),
    minValue: (message: string, ...args: Parameters<typeof minValue>) =>
      helpers.withMessage(() => translate('global.validation.minValue', message, { min: Number(args[0]) }), minValue(...args)),
    maxValue: (message: string, ...args: Parameters<typeof maxValue>) =>
      helpers.withMessage(() => translate('global.validation.maxValue', message, { max: Number(args[0]) }), maxValue(...args)),
  };
};
