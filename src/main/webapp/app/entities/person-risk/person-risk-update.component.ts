import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionService from '@/entities/position/position.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { RiskLevel } from '@/shared/model/enumerations/risk-level.model';
import { RiskType } from '@/shared/model/enumerations/risk-type.model';
import { type IPersonRisk, PersonRisk } from '@/shared/model/person-risk.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';

import PersonRiskService from './person-risk.service';

export default defineComponent({
  name: 'PersonRiskUpdate',
  setup() {
    const personRiskService = inject('personRiskService', () => new PersonRiskService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const personRisk: Ref<IPersonRisk> = ref(new PersonRisk());

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);
    const riskTypeValues: Ref<string[]> = ref(Object.keys(RiskType));
    const riskLevelValues: Ref<string[]> = ref(Object.keys(RiskLevel));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePersonRisk = async personRiskId => {
      try {
        const res = await personRiskService().find(personRiskId);
        personRisk.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.personRiskId) {
      retrievePersonRisk(route.params.personRiskId);
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
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      riskType: {
        required: validations.required('This field is required.'),
      },
      riskLevel: {
        required: validations.required('This field is required.'),
      },
      riskDescription: {},
      improvementAction: {},
      identifiedDate: {
        required: validations.required('This field is required.'),
      },
      targetDate: {},
      closedDate: {},
      person: {
        required: validations.required('This field is required.'),
      },
      position: {},
    };
    const v$ = useVuelidate(validationRules, personRisk as any);
    v$.value.$validate();

    return {
      personRiskService,
      alertService,
      personRisk,
      previousState,
      riskTypeValues,
      riskLevelValues,
      isSaving,
      currentLanguage,
      people,
      positions,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.personRisk.id) {
        this.personRiskService()
          .update(this.personRisk)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A PersonRisk is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.personRiskService()
          .create(this.personRisk)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A PersonRisk is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
