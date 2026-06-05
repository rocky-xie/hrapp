import { type Ref, defineComponent, inject, onMounted, ref, watch } from 'vue';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISuccessionCandidate } from '@/shared/model/succession-candidate.model';

import SuccessionCandidateService from './succession-candidate.service';

export default defineComponent({
  name: 'SuccessionCandidate',
  setup() {
    const dataUtils = useDataUtils();
    const successionCandidateService = inject('successionCandidateService', () => new SuccessionCandidateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);

    const successionCandidates: Ref<ISuccessionCandidate[]> = ref([]);

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

    const retrieveSuccessionCandidates = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };
        const res = await successionCandidateService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        successionCandidates.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSuccessionCandidates();
    };

    onMounted(async () => {
      await retrieveSuccessionCandidates();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISuccessionCandidate) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSuccessionCandidate = async () => {
      try {
        await successionCandidateService().delete(removeId.value);
        const message = `A SuccessionCandidate is deleted with identifier ${removeId.value}`;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSuccessionCandidates();
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
        await retrieveSuccessionCandidates();
      } else {
        // reset the pagination
        clear();
      }
    });

    // Whenever page changes, switch to the new page.
    watch(page, async () => {
      await retrieveSuccessionCandidates();
    });

    return {
      successionCandidates,
      handleSyncList,
      isFetching,
      retrieveSuccessionCandidates,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSuccessionCandidate,
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
