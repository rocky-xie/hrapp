import { describe, expect, it, vi, beforeEach } from 'vitest';
import i18n from '@/shared/config/i18n';

describe('locale.service', () => {
  beforeEach(() => {
    i18n.global.locale.value = 'en';
  });

  it('should change language via changeLanguage and update i18n locale', async () => {
    const { useLocale } = await import('./locale.service');
    const { changeLanguage } = useLocale();

    changeLanguage('zh-cn');
    expect(i18n.global.locale.value).toBe('zh-cn');
  });

  it('should normalize language and change to ja', async () => {
    const { useLocale } = await import('./locale.service');
    const { changeLanguage } = useLocale();

    changeLanguage('ja');
    expect(i18n.global.locale.value).toBe('ja');
  });

  it('should fallback to default for unsupported language', async () => {
    const { useLocale } = await import('./locale.service');
    const { changeLanguage } = useLocale();

    changeLanguage('fr');
    expect(i18n.global.locale.value).toBe('en');
  });

  it('should load locale via loadLocale and update i18n locale', async () => {
    const { loadLocale } = await import('./locale.service');

    loadLocale(i18n, 'ja');
    expect(i18n.global.locale.value).toBe('ja');
  });

  it('should not re-set locale if already set to same value', async () => {
    const { useLocale } = await import('./locale.service');
    const { changeLanguage, currentLanguage } = useLocale();

    i18n.global.locale.value = 'en';
    changeLanguage('en');
    expect(i18n.global.locale.value).toBe('en');
  });
});
