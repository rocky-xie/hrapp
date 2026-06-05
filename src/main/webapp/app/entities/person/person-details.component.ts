import { type Ref, defineComponent, inject, ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPerson } from '@/shared/model/person.model';

import PersonService from './person.service';

export default defineComponent({
  name: 'PersonDetails',
  setup() {
    const personService = inject('personService', () => new PersonService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const person: Ref<IPerson> = ref({});
    const trainingRecords = ref<any[]>([]);
    const trustObservations = ref<any[]>([]);
    const trainingLoading = ref(false);
    const trustLoading = ref(false);

    const retrievePerson = async personId => {
      try {
        const res = await personService().find(personId);
        person.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const loadTrainingHistory = async personId => {
      trainingLoading.value = true;
      try {
        const res = await axios.get(`api/reports/person-training-history/${personId}`);
        trainingRecords.value = res.data;
      } catch {
        trainingRecords.value = [];
      } finally {
        trainingLoading.value = false;
      }
    };

    const loadTrustObservations = async personId => {
      trustLoading.value = true;
      try {
        const res = await axios.get('api/trust-observations', {
          params: { 'personId.equals': personId, sort: ['observationDate,desc'] },
        });
        trustObservations.value = res.data;
      } catch {
        trustObservations.value = [];
      } finally {
        trustLoading.value = false;
      }
    };

    const personId = route.params?.personId;
    if (personId) {
      retrievePerson(personId);
      loadTrainingHistory(personId);
      loadTrustObservations(personId);
    }

    const trustStageLabel = (stage: string): string => {
      const labels: Record<string, string> = {
        S0_UNOBSERVED: 'Unobserved',
        S1_BASIC_TRUST: 'Basic Trust',
        S2_TASK_TRUST: 'Task Trust',
        S3_RESPONSIBILITY_TRUST: 'Responsibility Trust',
        S4_KEY_TRUST: 'Key Trust',
      };
      return labels[stage] || stage;
    };

    return {
      alertService,
      person,
      trainingRecords,
      trustObservations,
      trainingLoading,
      trustLoading,

      ...dataUtils,

      previousState,
      trustStageLabel,
    };
  },
});
