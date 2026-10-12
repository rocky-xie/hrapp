<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.recruitmentRequisition.home.createOrEditLabel" data-cy="RecruitmentRequisitionCreateUpdateHeading">
          {{ $t('entity.recruitmentRequisition.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="recruitmentRequisition.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="recruitmentRequisition.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.requisitionCode') }}</label>
            <input
              type="text"
              class="form-control"
              name="requisitionCode"
              id="recruitment-requisition-requisitionCode"
              data-cy="requisitionCode"
              :class="{ valid: !v$.requisitionCode.$invalid, invalid: v$.requisitionCode.$invalid }"
              v-model="v$.requisitionCode.$model"
              required
            />
            <div v-if="v$.requisitionCode.$anyDirty && v$.requisitionCode.$invalid">
              <small class="form-text text-danger" v-for="error of v$.requisitionCode.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.title') }}</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="recruitment-requisition-title"
              data-cy="title"
              :class="{ valid: !v$.title.$invalid, invalid: v$.title.$invalid }"
              v-model="v$.title.$model"
              required
            />
            <div v-if="v$.title.$anyDirty && v$.title.$invalid">
              <small class="form-text text-danger" v-for="error of v$.title.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.department') }}</label>
            <input
              type="text"
              class="form-control"
              name="department"
              id="recruitment-requisition-department"
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
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.positionId') }}</label>
            <input
              type="number"
              class="form-control"
              name="positionId"
              id="recruitment-requisition-positionId"
              data-cy="positionId"
              :class="{ valid: !v$.positionId.$invalid, invalid: v$.positionId.$invalid }"
              v-model.number="v$.positionId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.recruitmentType') }}</label>
            <select
              class="form-control"
              name="recruitmentType"
              :class="{ valid: !v$.recruitmentType.$invalid, invalid: v$.recruitmentType.$invalid }"
              v-model="v$.recruitmentType.$model"
              id="recruitment-requisition-recruitmentType"
              data-cy="recruitmentType"
              required
            >
              <option v-for="recruitmentType in recruitmentTypeValues" :key="recruitmentType" :value="recruitmentType">
                {{ $t('recruitment.enums.' + recruitmentType) }}
              </option>
            </select>
            <div v-if="v$.recruitmentType.$anyDirty && v$.recruitmentType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.recruitmentType.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.lifecycle') }}</label>
            <select
              class="form-control"
              name="lifecycle"
              :class="{ valid: !v$.lifecycle.$invalid, invalid: v$.lifecycle.$invalid }"
              v-model="v$.lifecycle.$model"
              id="recruitment-requisition-lifecycle"
              data-cy="lifecycle"
              required
            >
              <option v-for="requisitionLifecycle in requisitionLifecycleValues" :key="requisitionLifecycle" :value="requisitionLifecycle">
                {{ $t('recruitment.enums.' + requisitionLifecycle) }}
              </option>
            </select>
            <div v-if="v$.lifecycle.$anyDirty && v$.lifecycle.$invalid">
              <small class="form-text text-danger" v-for="error of v$.lifecycle.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.editStatus') }}</label>
            <select
              class="form-control"
              name="editStatus"
              :class="{ valid: !v$.editStatus.$invalid, invalid: v$.editStatus.$invalid }"
              v-model="v$.editStatus.$model"
              id="recruitment-requisition-editStatus"
              data-cy="editStatus"
              required
            >
              <option
                v-for="requisitionEditStatus in requisitionEditStatusValues"
                :key="requisitionEditStatus"
                :value="requisitionEditStatus"
              >
                {{ $t('recruitment.enums.' + requisitionEditStatus) }}
              </option>
            </select>
            <div v-if="v$.editStatus.$anyDirty && v$.editStatus.$invalid">
              <small class="form-text text-danger" v-for="error of v$.editStatus.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.plannedHeadcount') }}</label>
            <input
              type="number"
              class="form-control"
              name="plannedHeadcount"
              id="recruitment-requisition-plannedHeadcount"
              data-cy="plannedHeadcount"
              :class="{ valid: !v$.plannedHeadcount.$invalid, invalid: v$.plannedHeadcount.$invalid }"
              v-model.number="v$.plannedHeadcount.$model"
              required
            />
            <div v-if="v$.plannedHeadcount.$anyDirty && v$.plannedHeadcount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.plannedHeadcount.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.hiredCount') }}</label>
            <input
              type="number"
              class="form-control"
              name="hiredCount"
              id="recruitment-requisition-hiredCount"
              data-cy="hiredCount"
              :class="{ valid: !v$.hiredCount.$invalid, invalid: v$.hiredCount.$invalid }"
              v-model.number="v$.hiredCount.$model"
              required
            />
            <div v-if="v$.hiredCount.$anyDirty && v$.hiredCount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.hiredCount.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.checkedInCount') }}</label>
            <input
              type="number"
              class="form-control"
              name="checkedInCount"
              id="recruitment-requisition-checkedInCount"
              data-cy="checkedInCount"
              :class="{ valid: !v$.checkedInCount.$invalid, invalid: v$.checkedInCount.$invalid }"
              v-model.number="v$.checkedInCount.$model"
              required
            />
            <div v-if="v$.checkedInCount.$anyDirty && v$.checkedInCount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.checkedInCount.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.priority') }}</label>
            <select
              class="form-control"
              name="priority"
              id="recruitment-requisition-priority"
              data-cy="priority"
              v-model="v$.priority.$model"
            >
              <option :value="null"></option>
              <option v-for="priorityValue in ['HIGH', 'MEDIUM', 'LOW']" :key="priorityValue" :value="priorityValue">
                {{ $t('recruitment.enums.' + priorityValue) }}
              </option>
            </select>
            <div v-if="v$.priority.$anyDirty && v$.priority.$invalid">
              <small class="form-text text-danger" v-for="error of v$.priority.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.reason') }}</label>
            <textarea
              class="form-control"
              name="reason"
              id="recruitment-requisition-reason"
              data-cy="reason"
              :class="{ valid: !v$.reason.$invalid, invalid: v$.reason.$invalid }"
              v-model="v$.reason.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.targetJoinDate') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="recruitment-requisition-targetJoinDate"
                  v-model="v$.targetJoinDate.$model"
                  name="targetJoinDate"
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
                id="recruitment-requisition-targetJoinDate"
                data-cy="targetJoinDate"
                type="text"
                class="form-control"
                name="targetJoinDate"
                :class="{ valid: !v$.targetJoinDate.$invalid, invalid: v$.targetJoinDate.$invalid }"
                v-model="v$.targetJoinDate.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.desiredStartDate') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="recruitment-requisition-desiredStartDate"
                  v-model="v$.desiredStartDate.$model"
                  name="desiredStartDate"
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
                id="recruitment-requisition-desiredStartDate"
                data-cy="desiredStartDate"
                type="text"
                class="form-control"
                name="desiredStartDate"
                :class="{ valid: !v$.desiredStartDate.$invalid, invalid: v$.desiredStartDate.$invalid }"
                v-model="v$.desiredStartDate.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.desiredEndDate') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="recruitment-requisition-desiredEndDate"
                  v-model="v$.desiredEndDate.$model"
                  name="desiredEndDate"
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
                id="recruitment-requisition-desiredEndDate"
                data-cy="desiredEndDate"
                type="text"
                class="form-control"
                name="desiredEndDate"
                :class="{ valid: !v$.desiredEndDate.$invalid, invalid: v$.desiredEndDate.$invalid }"
                v-model="v$.desiredEndDate.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.currentVersion') }}</label>
            <input
              type="number"
              class="form-control"
              name="currentVersion"
              id="recruitment-requisition-currentVersion"
              data-cy="currentVersion"
              :class="{ valid: !v$.currentVersion.$invalid, invalid: v$.currentVersion.$invalid }"
              v-model.number="v$.currentVersion.$model"
              required
            />
            <div v-if="v$.currentVersion.$anyDirty && v$.currentVersion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.currentVersion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.lockedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-requisition-lockedAt"
                data-cy="lockedAt"
                type="datetime-local"
                class="form-control"
                name="lockedAt"
                :class="{ valid: !v$.lockedAt.$invalid, invalid: v$.lockedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.lockedAt.$model)"
                @change="updateInstantField('lockedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.lockedBy') }}</label>
            <input
              type="text"
              class="form-control"
              name="lockedBy"
              id="recruitment-requisition-lockedBy"
              data-cy="lockedBy"
              :class="{ valid: !v$.lockedBy.$invalid, invalid: v$.lockedBy.$invalid }"
              v-model="v$.lockedBy.$model"
            />
            <div v-if="v$.lockedBy.$anyDirty && v$.lockedBy.$invalid">
              <small class="form-text text-danger" v-for="error of v$.lockedBy.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.unlockedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-requisition-unlockedAt"
                data-cy="unlockedAt"
                type="datetime-local"
                class="form-control"
                name="unlockedAt"
                :class="{ valid: !v$.unlockedAt.$invalid, invalid: v$.unlockedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.unlockedAt.$model)"
                @change="updateInstantField('unlockedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.unlockReason') }}</label>
            <textarea
              class="form-control"
              name="unlockReason"
              id="recruitment-requisition-unlockReason"
              data-cy="unlockReason"
              :class="{ valid: !v$.unlockReason.$invalid, invalid: v$.unlockReason.$invalid }"
              v-model="v$.unlockReason.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-requisition">{{ $t('recruitment.fields.requirementsJson') }}</label>
            <textarea
              class="form-control"
              name="requirementsJson"
              id="recruitment-requisition-requirementsJson"
              data-cy="requirementsJson"
              :class="{ valid: !v$.requirementsJson.$invalid, invalid: v$.requirementsJson.$invalid }"
              v-model="v$.requirementsJson.$model"
            ></textarea>
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
<script lang="ts" src="./recruitment-requisition-update.component.ts"></script>
