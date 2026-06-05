import { createI18n } from 'vue-i18n';
import en from '../../../i18n/en/global.json';
import zhCn from '../../../i18n/zh-cn/global.json';
import ja from '../../../i18n/ja/global.json';

export default createI18n<false>({
  locale: 'en',
  fallbackLocale: 'en',
  messages: {
    en,
    'zh-cn': zhCn,
    ja,
  },
});
