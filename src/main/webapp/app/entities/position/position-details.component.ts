import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPosition } from '@/shared/model/position.model';

import PositionService from './position.service';

export default defineComponent({
  name: 'PositionDetails',
  setup() {
    const positionService = inject('positionService', () => new PositionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const position: Ref<IPosition> = ref({});

    const retrievePosition = async positionId => {
      try {
        const res = await positionService().find(positionId);
        position.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionId) {
      retrievePosition(route.params.positionId);
    }

    return {
      alertService,
      position,

      ...dataUtils,

      previousState,
    };
  },
});
