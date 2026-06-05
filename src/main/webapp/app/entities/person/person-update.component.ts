import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonSkillService from '@/entities/person-skill/person-skill.service';
import SkillService from '@/entities/skill/skill.service';
import SkillLevelService from '@/entities/skill-level/skill-level.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ConfidenceLevel } from '@/shared/model/enumerations/confidence-level.model';
import { EmploymentStatus } from '@/shared/model/enumerations/employment-status.model';
import { Gender } from '@/shared/model/enumerations/gender.model';
import { type IPerson, Person } from '@/shared/model/person.model';
import { type IPersonSkill, PersonSkill } from '@/shared/model/person-skill.model';
import { type ISkill } from '@/shared/model/skill.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';

import PersonService from './person.service';

export default defineComponent({
  name: 'PersonUpdate',
  setup() {
    const personService = inject('personService', () => new PersonService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const person: Ref<IPerson> = ref(new Person());
    const personSkillService = inject('personSkillService', () => new PersonSkillService());
    const personSkills: Ref<IPersonSkill[]> = ref([]);
    const deletedPersonSkillIds: Ref<number[]> = ref([]);
    const skillService = inject('skillService', () => new SkillService());
    const skills: Ref<ISkill[]> = ref([]);
    const skillLevelService = inject('skillLevelService', () => new SkillLevelService());
    const skillLevels: Ref<ISkillLevel[]> = ref([]);
    const confidenceLevelValues: Ref<string[]> = ref(Object.keys(ConfidenceLevel));
    const genderValues: Ref<string[]> = ref(Object.keys(Gender));
    const employmentStatusValues: Ref<string[]> = ref(Object.keys(EmploymentStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePerson = async personId => {
      try {
        const res = await personService().find(personId);
        person.value = res;
        await retrievePersonSkills(res.id);
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const retrievePersonSkills = async personId => {
      if (!personId) {
        return;
      }
      const res = await personSkillService().retrieve({ 'personId.equals': personId, size: 1000, sort: ['id,asc'] });
      personSkills.value = res.data ?? [];
    };

    if (route.params?.personId) {
      retrievePerson(route.params.personId);
    }

    const initRelationships = () => {
      skillService()
        .retrieve({ size: 1000 })
        .then(res => {
          skills.value = res.data ?? [];
        });
      skillLevelService()
        .retrieve({ size: 1000, sort: ['sortOrder,asc'] })
        .then(res => {
          skillLevels.value = res.data ?? [];
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      employeeCode: {
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      personName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      age: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
        max: validations.maxValue('This field cannot be more than 120.', 120),
      },
      gender: {},
      department: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      currentRole: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      employmentStatus: {
        required: validations.required('This field is required.'),
      },
      joinDate: {},
      mentorFlag: {
        required: validations.required('This field is required.'),
      },
      coreCandidateFlag: {
        required: validations.required('This field is required.'),
      },
      note: {},
    };
    const v$ = useVuelidate(validationRules, person as any);
    v$.value.$validate();

    return {
      personService,
      alertService,
      person,
      previousState,
      genderValues,
      employmentStatusValues,
      isSaving,
      currentLanguage,
      personSkillService,
      personSkills,
      deletedPersonSkillIds,
      skills,
      skillLevels,
      confidenceLevelValues,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    addPersonSkill(): void {
      const today = new Date().toISOString().slice(0, 10) as unknown as Date;
      this.personSkills.push(new PersonSkill(undefined, today, null, null, null, null, this.person));
    },
    removePersonSkill(index: number): void {
      const row = this.personSkills[index];
      if (row?.id) {
        this.deletedPersonSkillIds.push(row.id);
      }
      this.personSkills.splice(index, 1);
    },
    async syncPersonSkills(savedPerson: IPerson): Promise<void> {
      await Promise.all(this.deletedPersonSkillIds.map(id => this.personSkillService().delete(id)));
      const validRows = this.personSkills.filter(row => row.skill?.id && row.currentLevel?.id && row.assessmentDate);
      await Promise.all(
        validRows.map(row => {
          const payload = { ...row, person: savedPerson };
          return payload.id ? this.personSkillService().update(payload) : this.personSkillService().create(payload);
        }),
      );
      this.deletedPersonSkillIds = [];
    },
    save(): void {
      this.isSaving = true;
      if (this.person.id) {
        this.personService()
          .update(this.person)
          .then(async param => {
            await this.syncPersonSkills(param);
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Person is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.personService()
          .create(this.person)
          .then(async param => {
            await this.syncPersonSkills(param);
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Person is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
