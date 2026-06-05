import { type Ref, computed, defineComponent, inject, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionService from '@/entities/position/position.service';
import SkillService from '@/entities/skill/skill.service';
import SkillLevelService from '@/entities/skill-level/skill-level.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import i18n from '@/shared/config/i18n';
import useDataUtils from '@/shared/data/data-utils.service';
import { PlanStatus } from '@/shared/model/enumerations/plan-status.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';
import { type ISkill } from '@/shared/model/skill.model';
import { type ITrainingGoal, TrainingGoal } from '@/shared/model/training-goal.model';

import TrainingGoalService from './training-goal.service';

export default defineComponent({
  name: 'TrainingGoalUpdate',
  setup() {
    const trainingGoalService = inject('trainingGoalService', () => new TrainingGoalService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const trainingGoal: Ref<ITrainingGoal> = ref(new TrainingGoal());
    const selectedPeople: Ref<IPerson[]> = ref([]);
    const personToAdd: Ref<IPerson | null> = ref(null);
    const siblingGoals: Ref<ITrainingGoal[]> = ref([]);

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const availablePeople = computed(() => people.value.filter(p => !selectedPeople.value.some(sp => sp.id === p.id)));

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);

    const skillService = inject('skillService', () => new SkillService());

    const skills: Ref<ISkill[]> = ref([]);

    const skillLevelService = inject('skillLevelService', () => new SkillLevelService());

    const skillLevels: Ref<ISkillLevel[]> = ref([]);
    const planStatusValues: Ref<string[]> = ref(Object.keys(PlanStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTrainingGoal = async trainingGoalId => {
      try {
        const res = await trainingGoalService().find(trainingGoalId);
        trainingGoal.value = res;
        selectedPeople.value = res.person ? [res.person] : [];
        siblingGoals.value = [];

        if (res.goalName) {
          const siblingsRes = await trainingGoalService().retrieve({
            'goalName.equals': res.goalName,
            page: 0,
            size: 100,
          });
          const siblings = (siblingsRes.data ?? []) as ITrainingGoal[];
          siblingGoals.value = siblings;
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

    if (route.params?.trainingGoalId) {
      retrieveTrainingGoal(route.params.trainingGoalId);
    }

    const initRelationships = () => {
      personService()
        .retrieve()
        .then(res => {
          people.value = res.data;
        });
      positionService()
        .retrieve()
        .then(res => {
          positions.value = res.data;
        });
      skillService()
        .retrieve()
        .then(res => {
          skills.value = res.data;
        });
      skillLevelService()
        .retrieve()
        .then(res => {
          skillLevels.value = res.data;
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
      goalName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      goalDescription: {},
      targetLevelDescription: {},
      startDate: {},
      targetDate: {},
      status: {
        required: validations.required('This field is required.'),
      },
      person: {},
      position: {},
      skill: {},
      targetLevel: {},
    };
    const v$ = useVuelidate(validationRules, trainingGoal as any);
    v$.value.$validate();

    return {
      trainingGoalService,
      alertService,
      trainingGoal,
      selectedPeople,
      personToAdd,
      availablePeople,
      siblingGoals,
      previousState,
      planStatusValues,
      isSaving,
      currentLanguage,
      people,
      positions,
      skills,
      skillLevels,
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
    trainingGoalPayloads(): ITrainingGoal[] {
      return this.selectedPeople.map(person => {
        const existing = this.siblingGoals?.find(s => s.person?.id === person.id);
        const common = { ...this.trainingGoal, person };
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
      const toDelete = (this.siblingGoals ?? []).filter(s => s.id && s.person && !selectedIds.has(s.person!.id));
      const payloads = this.trainingGoalPayloads();
      let operations: Promise<any>[] = payloads.map(payload =>
        payload.id ? this.trainingGoalService().update(payload) : this.trainingGoalService().create(payload),
      );
      operations = operations.concat(toDelete.map(s => this.trainingGoalService().delete(s.id!)));
      Promise.all(operations)
        .then(results => {
          this.isSaving = false;
          this.previousState();
          if (this.trainingGoal.id) {
            this.alertService.showInfo(`A TrainingGoal is updated with identifier ${this.trainingGoal.id}`);
          } else {
            this.alertService.showSuccess(`TrainingGoals created for ${this.selectedPeople.length} people`);
          }
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService.showHttpError(error.response);
        });
    },
  },
});
