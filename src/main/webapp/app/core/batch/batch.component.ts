import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import BatchService from './batch.service';

export default defineComponent({
  name: 'BatchImportExport',
  setup() {
    const batchService = inject('batchService', () => new BatchService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const i18n = useI18n();

    const entities = [
      { key: 'persons', label: i18n.t('entity.person.detail.title') },
      { key: 'positions', label: i18n.t('entity.position.detail.title') },
      { key: 'skills', label: i18n.t('entity.skill.detail.title') },
    ];

    const isExporting = ref(false);
    const isImporting = ref(false);
    const importResult: Ref<string | null> = ref(null);

    const exportEntity = async (entity: string) => {
      isExporting.value = true;
      try {
        let blob: Blob;
        switch (entity) {
          case 'persons':
            blob = await batchService().exportPersons();
            break;
          case 'positions':
            blob = await batchService().exportPositions();
            break;
          case 'skills':
            blob = await batchService().exportSkills();
            break;
          default:
            return;
        }
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${entity}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
        alertService.showInfo(`Exported ${entity} successfully`);
      } catch (error) {
        alertService.showHttpError(error.response);
      } finally {
        isExporting.value = false;
      }
    };

    const onImportFileChange = async (event: Event, entity: string) => {
      const target = event.target as HTMLInputElement;
      if (!target.files || target.files.length === 0) return;
      const file = target.files[0];
      isImporting.value = true;
      importResult.value = null;
      try {
        let result: string;
        switch (entity) {
          case 'persons':
            result = await batchService().importPersons(file);
            break;
          case 'positions':
            result = await batchService().importPositions(file);
            break;
          case 'skills':
            result = await batchService().importSkills(file);
            break;
          default:
            return;
        }
        importResult.value = result;
        alertService.showInfo(result);
      } catch (error) {
        alertService.showHttpError(error.response);
      } finally {
        isImporting.value = false;
        target.value = '';
      }
    };

    return {
      entities,
      isExporting,
      isImporting,
      importResult,
      exportEntity,
      onImportFileChange,
    };
  },
});
