import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IDispatchAssignment } from '@/shared/model/dispatch-assignment.model';

import DispatchAssignmentService from './dispatch-assignment.service';

export default defineComponent({
  name: 'DispatchAssignmentDetails',
  setup() {
    const dispatchAssignmentService = inject('dispatchAssignmentService', () => new DispatchAssignmentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const dispatchAssignment: Ref<IDispatchAssignment> = ref({});

    const retrieveDispatchAssignment = async dispatchAssignmentId => {
      try {
        const res = await dispatchAssignmentService().find(dispatchAssignmentId);
        dispatchAssignment.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.dispatchAssignmentId) {
      retrieveDispatchAssignment(route.params.dispatchAssignmentId);
    }

    return {
      alertService,
      dispatchAssignment,

      ...dataUtils,

      previousState,
    };
  },
});
