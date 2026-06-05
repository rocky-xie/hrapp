import { defineComponent, ref, onMounted } from 'vue';
import ActionItemService from './action-item.service';

export default defineComponent({
  name: 'ActionItem',
  setup() {
    const service = new ActionItemService();
    const items = ref<any[]>([]);
    const loading = ref(false);

    const loadAll = async () => {
      loading.value = true;
      try {
        items.value = await service.getAll({ size: 100 });
      } finally {
        loading.value = false;
      }
    };

    const start = async (item: any) => {
      await service.start(item.id);
      await loadAll();
    };

    const complete = async (item: any) => {
      await service.complete(item.id);
      await loadAll();
    };

    const cancel = async (item: any) => {
      await service.cancel(item.id);
      await loadAll();
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

    return { items, loading, start, complete, cancel, priorityBadge, statusBadge };
  },
});
