<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.partnerOrganization.home.createOrEditLabel" data-cy="PartnerOrganizationCreateUpdateHeading">
          {{ $t('entity.partnerOrganization.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="partnerOrganization.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="partnerOrganization.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="partner-organization">{{ $t('recruitment.fields.partnerCode') }}</label>
            <input
              type="text"
              class="form-control"
              name="partnerCode"
              id="partner-organization-partnerCode"
              data-cy="partnerCode"
              :class="{ valid: !v$.partnerCode.$invalid, invalid: v$.partnerCode.$invalid }"
              v-model="v$.partnerCode.$model"
              required
            />
            <div v-if="v$.partnerCode.$anyDirty && v$.partnerCode.$invalid">
              <small class="form-text text-danger" v-for="error of v$.partnerCode.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="partner-organization">{{ $t('recruitment.fields.name') }}</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="partner-organization-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="partner-organization">{{ $t('recruitment.fields.rolesJson') }}</label>
            <textarea
              class="form-control"
              name="rolesJson"
              id="partner-organization-rolesJson"
              data-cy="rolesJson"
              :class="{ valid: !v$.rolesJson.$invalid, invalid: v$.rolesJson.$invalid }"
              v-model="v$.rolesJson.$model"
              required
            ></textarea>
            <div v-if="v$.rolesJson.$anyDirty && v$.rolesJson.$invalid">
              <small class="form-text text-danger" v-for="error of v$.rolesJson.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="partner-organization">{{ $t('recruitment.fields.dispatchEnabled') }}</label>
            <input
              type="checkbox"
              class="form-control"
              name="dispatchEnabled"
              id="partner-organization-dispatchEnabled"
              data-cy="dispatchEnabled"
              :class="{ valid: !v$.dispatchEnabled.$invalid, invalid: v$.dispatchEnabled.$invalid }"
              v-model="v$.dispatchEnabled.$model"
              required
            />
            <div v-if="v$.dispatchEnabled.$anyDirty && v$.dispatchEnabled.$invalid">
              <small class="form-text text-danger" v-for="error of v$.dispatchEnabled.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="partner-organization">{{ $t('recruitment.fields.defaultObservationDays') }}</label>
            <input
              type="number"
              class="form-control"
              name="defaultObservationDays"
              id="partner-organization-defaultObservationDays"
              data-cy="defaultObservationDays"
              :class="{ valid: !v$.defaultObservationDays.$invalid, invalid: v$.defaultObservationDays.$invalid }"
              v-model.number="v$.defaultObservationDays.$model"
            />
            <div v-if="v$.defaultObservationDays.$anyDirty && v$.defaultObservationDays.$invalid">
              <small class="form-text text-danger" v-for="error of v$.defaultObservationDays.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="partner-organization">{{ $t('recruitment.fields.active') }}</label>
            <input
              type="checkbox"
              class="form-control"
              name="active"
              id="partner-organization-active"
              data-cy="active"
              :class="{ valid: !v$.active.$invalid, invalid: v$.active.$invalid }"
              v-model="v$.active.$model"
              required
            />
            <div v-if="v$.active.$anyDirty && v$.active.$invalid">
              <small class="form-text text-danger" v-for="error of v$.active.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.cancel') }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.save') }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./partner-organization-update.component.ts"></script>
