import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionService from '@/entities/position/position.service';
import PositionAssignmentService from '@/entities/position-assignment/position-assignment.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';
import { type IPositionAssignment } from '@/shared/model/position-assignment.model';
import { type IStaffSubstitution, StaffSubstitution } from '@/shared/model/staff-substitution.model';

import StaffSubstitutionService from './staff-substitution.service';

export default defineComponent({
  name: 'StaffSubstitutionUpdate',
  setup() {
    const staffSubstitutionService = inject('staffSubstitutionService', () => new StaffSubstitutionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const staffSubstitution: Ref<IStaffSubstitution> = ref(new StaffSubstitution());
    staffSubstitution.value.thresholdRate = 80;

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const positionAssignmentService = inject('positionAssignmentService', () => new PositionAssignmentService());

    const positionAssignments: Ref<IPositionAssignment[]> = ref([]);
    const isSaving = ref(false);
    const isRefreshing = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveStaffSubstitution = async staffSubstitutionId => {
      try {
        const res = await staffSubstitutionService().find(staffSubstitutionId);
        staffSubstitution.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.staffSubstitutionId) {
      retrieveStaffSubstitution(route.params.staffSubstitutionId);
    }

    const initRelationships = () => {
      positionService()
        .retrieve()
        .then(res => {
          positions.value = res.data ?? [];
        });
      personService()
        .retrieve()
        .then(res => {
          people.value = res.data ?? [];
        });
      positionAssignmentService()
        .retrieve({ size: 1000 })
        .then(res => {
          positionAssignments.value = res.data ?? [];
        });
    };

    initRelationships();

    const candidatePeople = computed(() => {
      const positionId = staffSubstitution.value.position?.id;
      if (!positionId) {
        return people.value;
      }

      const currentOwnerIds = new Set(
        positionAssignments.value
          .filter(assignment => assignment.active && assignment.position?.id === positionId && assignment.person?.id)
          .map(assignment => assignment.person!.id),
      );

      return people.value.filter(person => !person.id || !currentOwnerIds.has(person.id));
    });

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      coverageRate: {
        min: validations.minValue('This field should be at least 0.', 0),
        max: validations.maxValue('This field cannot be more than 100.', 100),
      },
      thresholdRate: {
        required: validations.required('This field is required.'),
        min: validations.minValue('This field should be at least 0.', 0),
        max: validations.maxValue('This field cannot be more than 100.', 100),
      },
      totalSkillCount: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      coveredSkillCount: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      missingSkills: {},
      substitutable: {},
      evaluationDate: {},
      reason: {},
      position: {
        required: validations.required('This field is required.'),
      },
      candidatePerson: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, staffSubstitution as any);
    v$.value.$validate();

    return {
      staffSubstitutionService,
      alertService,
      staffSubstitution,
      previousState,
      isSaving,
      isRefreshing,
      currentLanguage,
      positions,
      people,
      candidatePeople,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    clearCalculatedFields(): void {
      this.staffSubstitution.coverageRate = undefined;
      this.staffSubstitution.totalSkillCount = undefined;
      this.staffSubstitution.coveredSkillCount = undefined;
      this.staffSubstitution.missingSkills = null;
      this.staffSubstitution.substitutable = false;
      this.staffSubstitution.evaluationDate = undefined;
      this.staffSubstitution.reason = null;
    },
    triggerCalculate(): Promise<void> | void {
      const positionId = this.staffSubstitution.position?.id;
      const candidatePersonId = this.staffSubstitution.candidatePerson?.id;
      if (!positionId || !candidatePersonId) {
        return;
      }
      return this.staffSubstitutionService()
        .calculate(positionId, candidatePersonId, this.staffSubstitution.thresholdRate)
        .then(res => {
          this.staffSubstitution.coverageRate = res.coverageRate;
          this.staffSubstitution.totalSkillCount = res.totalSkillCount;
          this.staffSubstitution.coveredSkillCount = res.coveredSkillCount;
          this.staffSubstitution.missingSkills = res.missingSkills;
          this.staffSubstitution.substitutable = res.substitutable;
          this.staffSubstitution.evaluationDate = res.evaluationDate;
          this.staffSubstitution.reason = res.reason;
        })
        .catch(error => {});
    },
    onPositionChange(): void {
      if (!this.staffSubstitution.id) {
        this.staffSubstitution.candidatePerson = undefined;
        this.clearCalculatedFields();
      } else {
        this.staffSubstitution.candidatePerson = undefined;
        this.clearCalculatedFields();
      }
    },
    onCandidatePersonChange(): void {
      this.clearCalculatedFields();
      this.triggerCalculate();
    },
    onThresholdRateChange(): void {
      this.triggerCalculate();
    },
    recalculate(): void {
      this.isRefreshing = true;
      const result = this.triggerCalculate();
      if (result) {
        result.finally(() => {
          this.isRefreshing = false;
        });
      } else {
        this.isRefreshing = false;
      }
    },
    save(): void {
      this.isSaving = true;
      if (this.staffSubstitution.id) {
        this.staffSubstitutionService()
          .update(this.staffSubstitution)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A StaffSubstitution is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        const positionId = this.staffSubstitution.position?.id;
        const candidatePersonId = this.staffSubstitution.candidatePerson?.id;
        if (!positionId || !candidatePersonId) {
          this.isSaving = false;
          return;
        }

        this.staffSubstitutionService()
          .calculate(positionId, candidatePersonId, this.staffSubstitution.thresholdRate)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A StaffSubstitution is calculated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
