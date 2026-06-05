import { type ComputedRef, type Ref, computed, defineComponent, inject, ref } from 'vue';

import { useVuelidate } from '@vuelidate/core';
import { email, maxLength, minLength, required } from '@vuelidate/validators';
import axios from 'axios';

import { SUPPORTED_LANGUAGES, normalizeLanguage } from '@/shared/config/languages';
import { EMAIL_ALREADY_USED_TYPE } from '@/shared/jhipster/error.constants';
import { useStore } from '@/store';

const validations = {
  settingsAccount: {
    firstName: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(50),
    },
    lastName: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(50),
    },
    email: {
      required,
      email,
      minLength: minLength(5),
      maxLength: maxLength(254),
    },
  },
};

export default defineComponent({
  name: 'Settings',
  validations,
  setup() {
    const store = useStore();

    const success: Ref<string> = ref(null);
    const error: Ref<string> = ref(null);
    const errorEmailExists: Ref<string> = ref(null);

    const settingsAccount = computed(() => store.account);
    const username = inject<ComputedRef<string>>('currentUsername', () => computed(() => store.account?.login), true);
    const languages = SUPPORTED_LANGUAGES;

    return {
      success,
      error,
      errorEmailExists,
      settingsAccount,
      username,
      languages,
      v$: useVuelidate(),
    };
  },
  methods: {
    save() {
      this.error = null;
      this.errorEmailExists = null;
      this.settingsAccount.langKey = normalizeLanguage(this.settingsAccount.langKey);
      return axios
        .post('api/account', this.settingsAccount)
        .then(() => {
          this.error = null;
          this.success = 'OK';
          this.errorEmailExists = null;
        })
        .catch(ex => {
          this.success = null;
          this.error = 'ERROR';
          if (ex.response.status === 400 && ex.response.data.type === EMAIL_ALREADY_USED_TYPE) {
            this.errorEmailExists = 'ERROR';
            this.error = null;
          }
        });
    },
  },
});
