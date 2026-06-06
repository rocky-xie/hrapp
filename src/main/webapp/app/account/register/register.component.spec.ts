import { beforeEach, describe, expect, it, vi } from 'vitest';
import { computed } from 'vue';

import { createTestingPinia } from '@pinia/testing';
import { shallowMount } from '@vue/test-utils';
import axios from 'axios';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from '@/shared/jhipster/error.constants';

import Register from './register.vue';

type RegisterComponentType = InstanceType<typeof Register>;

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
};

describe('Register Component (disabled)', () => {
  let register: RegisterComponentType;
  const filledRegisterAccount = {
    email: 'jhi@pster.net',
    langKey: 'en',
    login: 'jhi',
    password: 'jhipster',
  };

  beforeEach(() => {
    axiosStub.get.mockResolvedValue({});
    axiosStub.post.mockReset();

    const wrapper = shallowMount(Register, {
      global: {
        plugins: [createTestingPinia()],
        provide: {
          currentLanguage: computed(() => 'en'),
        },
      },
    });
    register = wrapper.vm;
  });

  it('should set all default values correctly', () => {
    expect(register.success).toBe(false);
    expect(register.error).toBe('');
    expect(register.errorEmailExists).toBe('');
    expect(register.errorUserExists).toBe('');
    expect(register.confirmPassword).toBe(null);
  });

  it('should fail register when backend returns 400 (self-registration disabled)', async () => {
    const error = { response: { status: 400, data: { type: 'about:blank' } } };
    axiosStub.post.mockRejectedValue(error);
    register.registerAccount = filledRegisterAccount;
    register.confirmPassword = filledRegisterAccount.password;
    register.register();
    await new Promise(resolve => setTimeout(resolve, 0));
    await register.$nextTick();

    expect(axiosStub.post).toHaveBeenCalledWith('api/register', {
      email: 'jhi@pster.net',
      langKey: 'en',
      login: 'jhi',
      password: 'jhipster',
    });
    expect(register.success).toBeNull();
    expect(register.error).toBe('ERROR');
  });
});
