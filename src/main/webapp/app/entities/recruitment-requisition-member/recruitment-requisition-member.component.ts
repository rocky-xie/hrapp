import { type Ref, defineComponent, inject, onMounted, ref, watch } from 'vue';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type IRecruitmentRequisitionMember } from '@/shared/model/recruitment-requisition-member.model';

import RecruitmentRequisitionMemberService from './recruitment-requisition-member.service';

export default defineComponent({
  name: 'RecruitmentRequisitionMember',
  setup() {
    const dateFormat = useDateFormat();
    const recruitmentRequisitionMemberService = inject(
      'recruitmentRequisitionMemberService',
      () => new RecruitmentRequisitionMemberService(),
    );
    const alertService = inject('alertService', () => useAlertService(), true);

    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);

    const recruitmentRequisitionMembers: Ref<IRecruitmentRequisitionMember[]> = ref([]);

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

    const retrieveRecruitmentRequisitionMembers = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };
        const res = await recruitmentRequisitionMemberService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        recruitmentRequisitionMembers.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveRecruitmentRequisitionMembers();
    };

    onMounted(async () => {
      await retrieveRecruitmentRequisitionMembers();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IRecruitmentRequisitionMember) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeRecruitmentRequisitionMember = async () => {
      try {
        await recruitmentRequisitionMemberService().delete(removeId.value);
        const message = `A RecruitmentRequisitionMember is deleted with identifier ${removeId.value}`;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveRecruitmentRequisitionMembers();
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
        await retrieveRecruitmentRequisitionMembers();
      } else {
        // reset the pagination
        clear();
      }
    });

    // Whenever page changes, switch to the new page.
    watch(page, async () => {
      await retrieveRecruitmentRequisitionMembers();
    });

    return {
      recruitmentRequisitionMembers,
      handleSyncList,
      isFetching,
      retrieveRecruitmentRequisitionMembers,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeRecruitmentRequisitionMember,
      itemsPerPage,
      queryCount,
      page,
      propOrder,
      reverse,
      totalItems,
      changeOrder,
    };
  },
});
