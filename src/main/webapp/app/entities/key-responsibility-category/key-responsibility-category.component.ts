import { type Ref, defineComponent, inject, onMounted, ref, watch } from 'vue';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IKeyResponsibilityCategory } from '@/shared/model/key-responsibility-category.model';

import KeyResponsibilityCategoryService from './key-responsibility-category.service';

export default defineComponent({
  name: 'KeyResponsibilityCategory',
  setup() {
    const dataUtils = useDataUtils();
    const keyResponsibilityCategoryService = inject('keyResponsibilityCategoryService', () => new KeyResponsibilityCategoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);

    const keyResponsibilityCategories: Ref<IKeyResponsibilityCategory[]> = ref([]);

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

    const retrieveKeyResponsibilityCategorys = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };
        const res = await keyResponsibilityCategoryService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        keyResponsibilityCategories.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveKeyResponsibilityCategorys();
    };

    onMounted(async () => {
      await retrieveKeyResponsibilityCategorys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IKeyResponsibilityCategory) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeKeyResponsibilityCategory = async () => {
      try {
        await keyResponsibilityCategoryService().delete(removeId.value);
        const message = `A KeyResponsibilityCategory is deleted with identifier ${removeId.value}`;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveKeyResponsibilityCategorys();
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
        await retrieveKeyResponsibilityCategorys();
      } else {
        // reset the pagination
        clear();
      }
    });

    // Whenever page changes, switch to the new page.
    watch(page, async () => {
      await retrieveKeyResponsibilityCategorys();
    });

    return {
      keyResponsibilityCategories,
      handleSyncList,
      isFetching,
      retrieveKeyResponsibilityCategorys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeKeyResponsibilityCategory,
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
