import { type Ref, ref } from 'vue';
import type { I18n } from 'vue-i18n';

import dayjs from 'dayjs';

import { DEFAULT_LANGUAGE, SUPPORTED_LANGUAGES, normalizeLanguage } from '@/shared/config/languages';
import i18n from '@/shared/config/i18n';

const currentLanguage: Ref<string> = ref(DEFAULT_LANGUAGE);

export function useLocale(): { currentLanguage: Ref<string>; changeLanguage: (lang: string) => void } {
  function changeLanguage(lang: string): void {
    const normalizedLang = normalizeLanguage(lang);
    if (currentLanguage.value === normalizedLang) return;
    currentLanguage.value = normalizedLang;
    i18n.global.locale = normalizedLang;
    dayjs.locale(normalizedLang);
  }

  return {
    currentLanguage,
    changeLanguage,
  };
}

export function loadLocale(i18n: I18n, locale: string): void {
  const normalized = normalizeLanguage(locale);
  i18n.global.locale = normalized;
  currentLanguage.value = normalized;
  dayjs.locale(normalized);
}
