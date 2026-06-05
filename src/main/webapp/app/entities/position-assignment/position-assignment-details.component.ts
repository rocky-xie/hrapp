import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPositionAssignment } from '@/shared/model/position-assignment.model';

import PositionAssignmentService from './position-assignment.service';

export default defineComponent({
  name: 'PositionAssignmentDetails',
  setup() {
    const positionAssignmentService = inject('positionAssignmentService', () => new PositionAssignmentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const positionAssignment: Ref<IPositionAssignment> = ref({});

    const retrievePositionAssignment = async positionAssignmentId => {
      try {
        const res = await positionAssignmentService().find(positionAssignmentId);
        positionAssignment.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionAssignmentId) {
      retrievePositionAssignment(route.params.positionAssignmentId);
    }

    return {
      alertService,
      positionAssignment,

      ...dataUtils,

      previousState,
    };
  },
});
