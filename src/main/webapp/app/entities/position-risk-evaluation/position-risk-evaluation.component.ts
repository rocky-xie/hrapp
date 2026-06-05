import { type Ref, defineComponent, inject, onMounted, ref, watch } from 'vue';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPositionRiskEvaluation } from '@/shared/model/position-risk-evaluation.model';

import PositionRiskEvaluationService from './position-risk-evaluation.service';

export default defineComponent({
  name: 'PositionRiskEvaluation',
  setup() {
    const dataUtils = useDataUtils();
    const positionRiskEvaluationService = inject('positionRiskEvaluationService', () => new PositionRiskEvaluationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);

    const positionRiskEvaluations: Ref<IPositionRiskEvaluation[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {
      page.value = 1;
    };

    const sort = (): Array<any> => {
      const result = [`${propOrder.value},${reverse.value ? 'desc' : 'asc'}`];
      if (propOrder.value !== 'id') {
        result.push('id');
      }
      return result;
    };

    const retrievePositionRiskEvaluations = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };
        const res = await positionRiskEvaluationService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        positionRiskEvaluations.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrievePositionRiskEvaluations();
    };

    onMounted(async () => {
      await retrievePositionRiskEvaluations();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IPositionRiskEvaluation) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removePositionRiskEvaluation = async () => {
      try {
        await positionRiskEvaluationService().delete(removeId.value);
        const message = `A PositionRiskEvaluation is deleted with identifier ${removeId.value}`;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrievePositionRiskEvaluations();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const changeOrder = (newOrder: string) => {
      if (propOrder.value === newOrder) {
        reverse.value = !reverse.value;
      } else {
        reverse.value = false;
      }
      propOrder.value = newOrder;
    };

    // Whenever order changes, reset the pagination
    watch([propOrder, reverse], async () => {
      if (page.value === 1) {
        // first page, retrieve new data
        await retrievePositionRiskEvaluations();
      } else {
        // reset the pagination
        clear();
      }
    });

    // Whenever page changes, switch to the new page.
    watch(page, async () => {
      await retrievePositionRiskEvaluations();
    });

    return {
      positionRiskEvaluations,
      handleSyncList,
      isFetching,
      retrievePositionRiskEvaluations,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removePositionRiskEvaluation,
      itemsPerPage,
      queryCount,
      page,
      propOrder,
      reverse,
      totalItems,
      changeOrder,
      ...dataUtils,
    };
  },
});
