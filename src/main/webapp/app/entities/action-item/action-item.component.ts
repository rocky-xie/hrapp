import { defineComponent, ref, onMounted } from 'vue';
import ActionItemService from './action-item.service';

export default defineComponent({
  name: 'ActionItem',
  setup() {
    const service = new ActionItemService();
    const items = ref<any[]>([]);
    const loading = ref(false);
    const showForm = ref(false);
    const editingItem = ref<any>(null);
    const form = ref<any>({});
    const saving = ref(false);
    const error = ref<string | null>(null);

    const loadAll = async () => {
      loading.value = true;
      try {
        items.value = await service.getAll({ size: 100 });
      } finally {
        loading.value = false;
      }
    };

    const openCreate = () => {
      editingItem.value = null;
      form.value = { sourceType: 'MANUAL', priority: 'P2_MEDIUM' };
      error.value = null;
      showForm.value = true;
    };

    const openEdit = (item: any) => {
      editingItem.value = item;
      form.value = { ...item };
      error.value = null;
      showForm.value = true;
    };

    const closeForm = () => {
      showForm.value = false;
      editingItem.value = null;
      form.value = {};
      error.value = null;
    };

    const save = async () => {
      saving.value = true;
      error.value = null;
      try {
        if (editingItem.value) {
          await service.update(editingItem.value.id, form.value);
        } else {
          await service.create(form.value);
        }
        closeForm();
        await loadAll();
      } catch (e: any) {
        error.value = e.response?.data?.title || e.message || 'Save failed';
      } finally {
        saving.value = false;
      }
    };

    const start = async (item: any) => {
      try {
        await service.start(item.id);
        await loadAll();
      } catch (e: any) {
        error.value = e.response?.data?.title || e.message || 'Start failed';
      }
    };

    const complete = async (item: any) => {
      try {
        await service.complete(item.id);
        await loadAll();
      } catch (e: any) {
        error.value = e.response?.data?.title || e.message || 'Complete failed';
      }
    };

    const cancel = async (item: any) => {
      try {
        await service.cancel(item.id);
        await loadAll();
      } catch (e: any) {
        error.value = e.response?.data?.title || e.message || 'Cancel failed';
      }
    };

    const remove = async (item: any) => {
      try {
        await service.delete(item.id);
        await loadAll();
      } catch (e: any) {
        error.value = e.response?.data?.title || e.message || 'Delete failed';
      }
    };

    const priorityBadge = (p: string) => {
      if (p === 'P0_CRITICAL') return 'bg-danger';
      if (p === 'P1_HIGH') return 'bg-warning';
      if (p === 'P2_MEDIUM') return 'bg-info';
      return 'bg-secondary';
    };

    const statusBadge = (s: string) => {
      if (s === 'OPEN') return 'badge bg-primary';
      if (s === 'IN_PROGRESS') return 'badge bg-warning';
      if (s === 'COMPLETED') return 'badge bg-success';
      return 'badge bg-secondary';
    };

    onMounted(loadAll);

    return {
      items,
      loading,
      showForm,
      editingItem,
      form,
      saving,
      error,
      openCreate,
      openEdit,
      closeForm,
      save,
      start,
      complete,
      cancel,
      remove,
      priorityBadge,
      statusBadge,
    };
  },
});
