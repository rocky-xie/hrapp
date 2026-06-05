import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionService from '@/entities/position/position.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPerson } from '@/shared/model/person.model';
import { type IPositionAssignment, PositionAssignment } from '@/shared/model/position-assignment.model';
import { type IPosition } from '@/shared/model/position.model';

import PositionAssignmentService from './position-assignment.service';

export default defineComponent({
  name: 'PositionAssignmentUpdate',
  setup() {
    const positionAssignmentService = inject('positionAssignmentService', () => new PositionAssignmentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const positionAssignment: Ref<IPositionAssignment> = ref(new PositionAssignment());

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePositionAssignment = async positionAssignmentId => {
      try {
        const res = await positionAssignmentService().find(positionAssignmentId);
        positionAssignment.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionAssignmentId) {
      retrievePositionAssignment(route.params.positionAssignmentId);
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
      primaryOwner: {
        required: validations.required('This field is required.'),
      },
      startDate: {},
      endDate: {},
      responsibilityScope: {},
      active: {
        required: validations.required('This field is required.'),
      },
      person: {
        required: validations.required('This field is required.'),
      },
      position: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, positionAssignment as any);
    v$.value.$validate();

    return {
      positionAssignmentService,
      alertService,
      positionAssignment,
      previousState,
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
      if (this.positionAssignment.id) {
        this.positionAssignmentService()
          .update(this.positionAssignment)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A PositionAssignment is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.positionAssignmentService()
          .create(this.positionAssignment)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A PositionAssignment is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
