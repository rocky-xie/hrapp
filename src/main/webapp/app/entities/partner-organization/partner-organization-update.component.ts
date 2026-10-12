import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPartnerOrganization, PartnerOrganization } from '@/shared/model/partner-organization.model';

import PartnerOrganizationService from './partner-organization.service';

export default defineComponent({
  name: 'PartnerOrganizationUpdate',
  setup() {
    const partnerOrganizationService = inject('partnerOrganizationService', () => new PartnerOrganizationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const partnerOrganization: Ref<IPartnerOrganization> = ref(new PartnerOrganization());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePartnerOrganization = async partnerOrganizationId => {
      try {
        const res = await partnerOrganizationService().find(partnerOrganizationId);
        partnerOrganization.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.partnerOrganizationId) {
      retrievePartnerOrganization(route.params.partnerOrganizationId);
    }

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      partnerCode: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 40 characters.', 40),
      },
      name: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      rolesJson: {
        required: validations.required('This field is required.'),
      },
      dispatchEnabled: {
        required: validations.required('This field is required.'),
      },
      defaultObservationDays: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      active: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, partnerOrganization as any);
    v$.value.$validate();

    return {
      partnerOrganizationService,
      alertService,
      partnerOrganization,
      previousState,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.partnerOrganization.id) {
        this.partnerOrganizationService()
          .update(this.partnerOrganization)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A PartnerOrganization is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.partnerOrganizationService()
          .create(this.partnerOrganization)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A PartnerOrganization is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
