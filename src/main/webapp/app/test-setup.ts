import { beforeAll } from 'vitest';

import axios from 'axios';
import { config } from '@vue/test-utils';

config.global.mocks = {
  ...(config.global.mocks ?? {}),
  $t: (key: string, params?: Record<string, unknown>) => (params ? `${key} ${JSON.stringify(params)}` : key),
};

beforeAll(() => {
  globalThis.location.href = 'https://jhipster.tech/';

  // Make sure axios is never executed.
  axios.interceptors.request.use(request => {
    throw new Error(`Error axios should be mocked ${request.url}`);
  });
});
