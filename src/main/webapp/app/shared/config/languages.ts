export interface LanguageOption {
  key: string;
  name: string;
}

export const SUPPORTED_LANGUAGES: LanguageOption[] = [
  { key: 'en', name: 'English' },
  { key: 'zh-cn', name: '简体中文' },
  { key: 'ja', name: '日本語' },
];

export const DEFAULT_LANGUAGE = 'en';

export function normalizeLanguage(language?: string): string {
  const normalizedLanguage = language?.toLowerCase();

  if (!normalizedLanguage) {
    return DEFAULT_LANGUAGE;
  }

  if (normalizedLanguage.startsWith('zh')) {
    return 'zh-cn';
  }

  if (normalizedLanguage.startsWith('ja')) {
    return 'ja';
  }

  return SUPPORTED_LANGUAGES.some(supportedLanguage => supportedLanguage.key === normalizedLanguage)
    ? normalizedLanguage
    : DEFAULT_LANGUAGE;
}
