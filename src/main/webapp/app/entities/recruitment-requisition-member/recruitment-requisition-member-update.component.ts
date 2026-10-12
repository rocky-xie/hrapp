import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentRequisitionService from '@/entities/recruitment-requisition/recruitment-requisition.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { type IRecruitmentRequisitionMember, RecruitmentRequisitionMember } from '@/shared/model/recruitment-requisition-member.model';
import { type IRecruitmentRequisition } from '@/shared/model/recruitment-requisition.model';

import RecruitmentRequisitionMemberService from './recruitment-requisition-member.service';

export default defineComponent({
  name: 'RecruitmentRequisitionMemberUpdate',
  setup() {
    const recruitmentRequisitionMemberService = inject(
      'recruitmentRequisitionMemberService',
      () => new RecruitmentRequisitionMemberService(),
    );
    const alertService = inject('alertService', () => useAlertService(), true);

    const recruitmentRequisitionMember: Ref<IRecruitmentRequisitionMember> = ref(new RecruitmentRequisitionMember());

    const recruitmentRequisitionService = inject('recruitmentRequisitionService', () => new RecruitmentRequisitionService());

    const recruitmentRequisitions: Ref<IRecruitmentRequisition[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveRecruitmentRequisitionMember = async recruitmentRequisitionMemberId => {
      try {
        const res = await recruitmentRequisitionMemberService().find(recruitmentRequisitionMemberId);
        res.createdAt = new Date(res.createdAt);
        res.removedAt = new Date(res.removedAt);
        recruitmentRequisitionMember.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentRequisitionMemberId) {
      retrieveRecruitmentRequisitionMember(route.params.recruitmentRequisitionMemberId);
    }

    const initRelationships = () => {
      recruitmentRequisitionService()
        .retrieve()
        .then(res => {
          recruitmentRequisitions.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      username: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      memberType: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 30 characters.', 30),
      },
      active: {
        required: validations.required('This field is required.'),
      },
      createdAt: {
        required: validations.required('This field is required.'),
      },
      removedAt: {},
      requisition: {},
    };
    const v$ = useVuelidate(validationRules, recruitmentRequisitionMember as any);
    v$.value.$validate();

    return {
      recruitmentRequisitionMemberService,
      alertService,
      recruitmentRequisitionMember,
      previousState,
      isSaving,
      currentLanguage,
      recruitmentRequisitions,
      v$,
      ...useDateFormat({ entityRef: recruitmentRequisitionMember }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.recruitmentRequisitionMember.id) {
        this.recruitmentRequisitionMemberService()
          .update(this.recruitmentRequisitionMember)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A RecruitmentRequisitionMember is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.recruitmentRequisitionMemberService()
          .create(this.recruitmentRequisitionMember)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A RecruitmentRequisitionMember is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
