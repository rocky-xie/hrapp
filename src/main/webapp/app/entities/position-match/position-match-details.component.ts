import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPositionMatch } from '@/shared/model/position-match.model';

import PositionMatchService from './position-match.service';

export default defineComponent({
  name: 'PositionMatchDetails',
  setup() {
    const positionMatchService = inject('positionMatchService', () => new PositionMatchService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const positionMatch: Ref<IPositionMatch> = ref({});

    const retrievePositionMatch = async positionMatchId => {
      try {
        const res = await positionMatchService().find(positionMatchId);
        positionMatch.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionMatchId) {
      retrievePositionMatch(route.params.positionMatchId);
    }

    return {
      alertService,
      positionMatch,

      ...dataUtils,

      previousState,
    };
  },
});
