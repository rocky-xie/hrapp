<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.dispatchAssignment.home.createOrEditLabel" data-cy="DispatchAssignmentCreateUpdateHeading">
          {{ $t('entity.dispatchAssignment.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="dispatchAssignment.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dispatchAssignment.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.candidateId') }}</label>
            <input
              type="number"
              class="form-control"
              name="candidateId"
              id="dispatch-assignment-candidateId"
              data-cy="candidateId"
              :class="{ valid: !v$.candidateId.$invalid, invalid: v$.candidateId.$invalid }"
              v-model.number="v$.candidateId.$model"
              required
            />
            <div v-if="v$.candidateId.$anyDirty && v$.candidateId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.candidateId.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.personId') }}</label>
            <input
              type="number"
              class="form-control"
              name="personId"
              id="dispatch-assignment-personId"
              data-cy="personId"
              :class="{ valid: !v$.personId.$invalid, invalid: v$.personId.$invalid }"
              v-model.number="v$.personId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.partnerOrganizationId') }}</label>
            <input
              type="number"
              class="form-control"
              name="partnerOrganizationId"
              id="dispatch-assignment-partnerOrganizationId"
              data-cy="partnerOrganizationId"
              :class="{ valid: !v$.partnerOrganizationId.$invalid, invalid: v$.partnerOrganizationId.$invalid }"
              v-model.number="v$.partnerOrganizationId.$model"
              required
            />
            <div v-if="v$.partnerOrganizationId.$anyDirty && v$.partnerOrganizationId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.partnerOrganizationId.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.department') }}</label>
            <input
              type="text"
              class="form-control"
              name="department"
              id="dispatch-assignment-department"
              data-cy="department"
              :class="{ valid: !v$.department.$invalid, invalid: v$.department.$invalid }"
              v-model="v$.department.$model"
              required
            />
            <div v-if="v$.department.$anyDirty && v$.department.$invalid">
              <small class="form-text text-danger" v-for="error of v$.department.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.positionId') }}</label>
            <input
              type="number"
              class="form-control"
              name="positionId"
              id="dispatch-assignment-positionId"
              data-cy="positionId"
              :class="{ valid: !v$.positionId.$invalid, invalid: v$.positionId.$invalid }"
              v-model.number="v$.positionId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.startDate') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="dispatch-assignment-startDate"
                  v-model="v$.startDate.$model"
                  name="startDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="dispatch-assignment-startDate"
                data-cy="startDate"
                type="text"
                class="form-control"
                name="startDate"
                :class="{ valid: !v$.startDate.$invalid, invalid: v$.startDate.$invalid }"
                v-model="v$.startDate.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.expectedEndDate') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="dispatch-assignment-expectedEndDate"
                  v-model="v$.expectedEndDate.$model"
                  name="expectedEndDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="dispatch-assignment-expectedEndDate"
                data-cy="expectedEndDate"
                type="text"
                class="form-control"
                name="expectedEndDate"
                :class="{ valid: !v$.expectedEndDate.$invalid, invalid: v$.expectedEndDate.$invalid }"
                v-model="v$.expectedEndDate.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.observationDays') }}</label>
            <input
              type="number"
              class="form-control"
              name="observationDays"
              id="dispatch-assignment-observationDays"
              data-cy="observationDays"
              :class="{ valid: !v$.observationDays.$invalid, invalid: v$.observationDays.$invalid }"
              v-model.number="v$.observationDays.$model"
            />
            <div v-if="v$.observationDays.$anyDirty && v$.observationDays.$invalid">
              <small class="form-text text-danger" v-for="error of v$.observationDays.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.status') }}</label>
            <input
              type="text"
              class="form-control"
              name="status"
              id="dispatch-assignment-status"
              data-cy="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              required
            />
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.finalDecision') }}</label>
            <input
              type="text"
              class="form-control"
              name="finalDecision"
              id="dispatch-assignment-finalDecision"
              data-cy="finalDecision"
              :class="{ valid: !v$.finalDecision.$invalid, invalid: v$.finalDecision.$invalid }"
              v-model="v$.finalDecision.$model"
            />
            <div v-if="v$.finalDecision.$anyDirty && v$.finalDecision.$invalid">
              <small class="form-text text-danger" v-for="error of v$.finalDecision.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.note') }}</label>
            <textarea
              class="form-control"
              name="note"
              id="dispatch-assignment-note"
              data-cy="note"
              :class="{ valid: !v$.note.$invalid, invalid: v$.note.$invalid }"
              v-model="v$.note.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="dispatch-assignment">{{ $t('recruitment.fields.application') }}</label>
            <select
              class="form-control"
              id="dispatch-assignment-application"
              data-cy="application"
              name="application"
              v-model="dispatchAssignment.application"
            >
              <option :value="null"></option>
              <option
                :value="
                  dispatchAssignment.application && recruitmentApplicationOption.id === dispatchAssignment.application.id
                    ? dispatchAssignment.application
                    : recruitmentApplicationOption
                "
                v-for="recruitmentApplicationOption in recruitmentApplications"
                :key="recruitmentApplicationOption.id"
              >
                {{ recruitmentApplicationOption.applicationCode }}
              </option>
            </select>
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
<script lang="ts" src="./dispatch-assignment-update.component.ts"></script>
