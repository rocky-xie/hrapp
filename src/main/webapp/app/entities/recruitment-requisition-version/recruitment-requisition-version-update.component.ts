import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentRequisitionService from '@/entities/recruitment-requisition/recruitment-requisition.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IRecruitmentRequisitionVersion, RecruitmentRequisitionVersion } from '@/shared/model/recruitment-requisition-version.model';
import { type IRecruitmentRequisition } from '@/shared/model/recruitment-requisition.model';

import RecruitmentRequisitionVersionService from './recruitment-requisition-version.service';

export default defineComponent({
  name: 'RecruitmentRequisitionVersionUpdate',
  setup() {
    const recruitmentRequisitionVersionService = inject(
      'recruitmentRequisitionVersionService',
      () => new RecruitmentRequisitionVersionService(),
    );
    const alertService = inject('alertService', () => useAlertService(), true);

    const recruitmentRequisitionVersion: Ref<IRecruitmentRequisitionVersion> = ref(new RecruitmentRequisitionVersion());

    const recruitmentRequisitionService = inject('recruitmentRequisitionService', () => new RecruitmentRequisitionService());

    const recruitmentRequisitions: Ref<IRecruitmentRequisition[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveRecruitmentRequisitionVersion = async recruitmentRequisitionVersionId => {
      try {
        const res = await recruitmentRequisitionVersionService().find(recruitmentRequisitionVersionId);
        res.lockedAt = new Date(res.lockedAt);
        recruitmentRequisitionVersion.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentRequisitionVersionId) {
      retrieveRecruitmentRequisitionVersion(route.params.recruitmentRequisitionVersionId);
    }

    const initRelationships = () => {
      recruitmentRequisitionService()
        .retrieve()
        .then(res => {
          recruitmentRequisitions.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      versionNumber: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 1.', 1),
      },
      snapshotJson: {
        required: validations.required('This field is required.'),
      },
      lockedAt: {
        required: validations.required('This field is required.'),
      },
      lockedBy: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      changeReason: {},
      applicationses: {},
      requisition: {},
    };
    const v$ = useVuelidate(validationRules, recruitmentRequisitionVersion as any);
    v$.value.$validate();

    return {
      recruitmentRequisitionVersionService,
      alertService,
      recruitmentRequisitionVersion,
      previousState,
      isSaving,
      currentLanguage,
      recruitmentRequisitions,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: recruitmentRequisitionVersion }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.recruitmentRequisitionVersion.id) {
        this.recruitmentRequisitionVersionService()
          .update(this.recruitmentRequisitionVersion)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A RecruitmentRequisitionVersion is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.recruitmentRequisitionVersionService()
          .create(this.recruitmentRequisitionVersion)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A RecruitmentRequisitionVersion is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
