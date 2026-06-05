import { type Ref, computed, defineComponent, inject, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionService from '@/entities/position/position.service';
import TrainingGoalService from '@/entities/training-goal/training-goal.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import i18n from '@/shared/config/i18n';
import useDataUtils from '@/shared/data/data-utils.service';
import { TrainingType } from '@/shared/model/enumerations/training-type.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ITrainingGoal } from '@/shared/model/training-goal.model';
import { type ITrainingRecord, TrainingRecord } from '@/shared/model/training-record.model';

import TrainingRecordService from './training-record.service';

export default defineComponent({
  name: 'TrainingRecordUpdate',
  setup() {
    const trainingRecordService = inject('trainingRecordService', () => new TrainingRecordService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const trainingRecord: Ref<ITrainingRecord> = ref(new TrainingRecord());
    const selectedPeople: Ref<IPerson[]> = ref([]);
    const personToAdd: Ref<IPerson | null> = ref(null);
    const siblingRecords: Ref<ITrainingRecord[]> = ref([]);

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const availablePeople = computed(() => people.value.filter(p => !selectedPeople.value.some(sp => sp.id === p.id)));

    const trainingGoalService = inject('trainingGoalService', () => new TrainingGoalService());

    const trainingGoals: Ref<ITrainingGoal[]> = ref([]);

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);
    const trainingTypeValues: Ref<string[]> = ref(Object.keys(TrainingType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTrainingRecord = async trainingRecordId => {
      try {
        const res = await trainingRecordService().find(trainingRecordId);
        trainingRecord.value = res;
        selectedPeople.value = res.person ? [res.person] : [];
        siblingRecords.value = [];

        if (res.topic) {
          const siblingsRes = await trainingRecordService().retrieve({
            'topic.equals': res.topic,
            page: 0,
            size: 100,
          });
          const siblings = (siblingsRes.data ?? []) as ITrainingRecord[];
          siblingRecords.value = siblings;
          const persons = siblings.filter(s => s.id !== res.id && s.person).map(s => s.person!);
          for (const p of persons) {
            if (!selectedPeople.value.some(sp => sp.id === p.id)) {
              selectedPeople.value.push(p);
            }
          }
        }
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.trainingRecordId) {
      retrieveTrainingRecord(route.params.trainingRecordId);
    }

    const initRelationships = () => {
      personService()
        .retrieve()
        .then(res => {
          people.value = res.data;
        });
      trainingGoalService()
        .retrieve()
        .then(res => {
          trainingGoals.value = res.data;
        });
      positionService()
        .retrieve()
        .then(res => {
          positions.value = res.data;
        });
    };

    initRelationships();

    watch(people, () => {
      if (people.value.length > 0 && selectedPeople.value.length > 0) {
        selectedPeople.value = selectedPeople.value.map(p => {
          const full = people.value.find(fp => fp.id === p.id);
          return full ?? p;
        });
      }
    });

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      trainingDate: {
        required: validations.required('This field is required.'),
      },
      trainingType: {
        required: validations.required('This field is required.'),
      },
      topic: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      taskDescription: {},
      resultDescription: {},
      evidence: {},
      nextAction: {},
      person: {
        required: validations.required('This field is required.'),
      },
      trainingGoal: {},
      position: {},
      mentor: {},
    };
    const v$ = useVuelidate(validationRules, trainingRecord as any);
    v$.value.$validate();

    return {
      trainingRecordService,
      alertService,
      trainingRecord,
      selectedPeople,
      personToAdd,
      availablePeople,
      siblingRecords,
      previousState,
      trainingTypeValues,
      isSaving,
      currentLanguage,
      people,
      trainingGoals,
      positions,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    addPerson(): void {
      if (this.personToAdd && !this.selectedPeople.some(p => p.id === this.personToAdd!.id)) {
        this.selectedPeople.push(this.personToAdd);
      }
      this.personToAdd = null;
    },
    removePerson(index: number): void {
      this.selectedPeople.splice(index, 1);
    },
    trainingRecordPayloads(): ITrainingRecord[] {
      return this.selectedPeople.map(person => {
        const existing = this.siblingRecords?.find(s => s.person?.id === person.id);
        const common = { ...this.trainingRecord, person };
        if (existing?.id) {
          common.id = existing.id;
        } else {
          delete common.id;
        }
        return common;
      });
    },
    save(): void {
      if (this.selectedPeople.length === 0) {
        this.alertService.showError(i18n.global.t('global.validation.personRequired'));
        return;
      }
      this.isSaving = true;
      const selectedIds = new Set(this.selectedPeople.map(p => p.id));
      const toDelete = (this.siblingRecords ?? []).filter(s => s.id && s.person && !selectedIds.has(s.person!.id));
      const payloads = this.trainingRecordPayloads();
      let operations: Promise<any>[] = payloads.map(payload =>
        payload.id ? this.trainingRecordService().update(payload) : this.trainingRecordService().create(payload),
      );
      operations = operations.concat(toDelete.map(s => this.trainingRecordService().delete(s.id!)));
      Promise.all(operations)
        .then(results => {
          this.isSaving = false;
          this.previousState();
          if (this.trainingRecord.id) {
            this.alertService.showInfo(`A TrainingRecord is updated with identifier ${this.trainingRecord.id}`);
          } else {
            this.alertService.showSuccess(`TrainingRecords created for ${this.selectedPeople.length} people`);
          }
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService.showHttpError(error.response);
        });
    },
  },
});
