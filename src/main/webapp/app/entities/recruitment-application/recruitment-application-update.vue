<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.recruitmentApplication.home.createOrEditLabel" data-cy="RecruitmentApplicationCreateUpdateHeading">
          {{ $t('entity.recruitmentApplication.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="recruitmentApplication.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="recruitmentApplication.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.applicationCode') }}</label>
            <input
              type="text"
              class="form-control"
              name="applicationCode"
              id="recruitment-application-applicationCode"
              data-cy="applicationCode"
              :class="{ valid: !v$.applicationCode.$invalid, invalid: v$.applicationCode.$invalid }"
              v-model="v$.applicationCode.$model"
              required
            />
            <div v-if="v$.applicationCode.$anyDirty && v$.applicationCode.$invalid">
              <small class="form-text text-danger" v-for="error of v$.applicationCode.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.recruitmentType') }}</label>
            <select
              class="form-control"
              name="recruitmentType"
              :class="{ valid: !v$.recruitmentType.$invalid, invalid: v$.recruitmentType.$invalid }"
              v-model="v$.recruitmentType.$model"
              id="recruitment-application-recruitmentType"
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
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.currentStage') }}</label>
            <select
              class="form-control"
              name="currentStage"
              :class="{ valid: !v$.currentStage.$invalid, invalid: v$.currentStage.$invalid }"
              v-model="v$.currentStage.$model"
              id="recruitment-application-currentStage"
              data-cy="currentStage"
            >
              <option v-for="applicationStageType in applicationStageTypeValues" :key="applicationStageType" :value="applicationStageType">
                {{ $t('recruitment.enums.' + applicationStageType) }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.status') }}</label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="recruitment-application-status"
              data-cy="status"
              required
            >
              <option v-for="applicationStatus in applicationStatusValues" :key="applicationStatus" :value="applicationStatus">
                {{ $t('recruitment.enums.' + applicationStatus) }}
              </option>
            </select>
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.decision') }}</label>
            <select
              class="form-control"
              name="decision"
              :class="{ valid: !v$.decision.$invalid, invalid: v$.decision.$invalid }"
              v-model="v$.decision.$model"
              id="recruitment-application-decision"
              data-cy="decision"
            >
              <option v-for="applicationDecision in applicationDecisionValues" :key="applicationDecision" :value="applicationDecision">
                {{ $t('recruitment.enums.' + applicationDecision) }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.sourceType') }}</label>
            <select
              class="form-control"
              name="sourceType"
              id="recruitment-application-sourceType"
              data-cy="sourceType"
              :class="{ valid: !v$.sourceType.$invalid, invalid: v$.sourceType.$invalid }"
              v-model="v$.sourceType.$model"
              required
            >
              <option v-for="sourceTypeOption in sourceTypeValues" :key="sourceTypeOption" :value="sourceTypeOption">
                {{ $t('recruitment.enums.' + sourceTypeOption) }}
              </option>
            </select>
            <div v-if="v$.sourceType.$anyDirty && v$.sourceType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sourceType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.sourceDetail') }}</label>
            <input
              type="text"
              class="form-control"
              name="sourceDetail"
              id="recruitment-application-sourceDetail"
              data-cy="sourceDetail"
              :class="{ valid: !v$.sourceDetail.$invalid, invalid: v$.sourceDetail.$invalid }"
              v-model="v$.sourceDetail.$model"
            />
            <div v-if="v$.sourceDetail.$anyDirty && v$.sourceDetail.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sourceDetail.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.batchName') }}</label>
            <input
              type="text"
              class="form-control"
              name="batchName"
              id="recruitment-application-batchName"
              data-cy="batchName"
              :class="{ valid: !v$.batchName.$invalid, invalid: v$.batchName.$invalid }"
              v-model="v$.batchName.$model"
            />
            <div v-if="v$.batchName.$anyDirty && v$.batchName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.batchName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.appliedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-application-appliedAt"
                data-cy="appliedAt"
                type="datetime-local"
                class="form-control"
                name="appliedAt"
                :class="{ valid: !v$.appliedAt.$invalid, invalid: v$.appliedAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.appliedAt.$model)"
                @change="updateInstantField('appliedAt', $event)"
              />
            </div>
            <div v-if="v$.appliedAt.$anyDirty && v$.appliedAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.appliedAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.closedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-application-closedAt"
                data-cy="closedAt"
                type="datetime-local"
                class="form-control"
                name="closedAt"
                :class="{ valid: !v$.closedAt.$invalid, invalid: v$.closedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.closedAt.$model)"
                @change="updateInstantField('closedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.retentionUntil') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="recruitment-application-retentionUntil"
                  v-model="v$.retentionUntil.$model"
                  name="retentionUntil"
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
                id="recruitment-application-retentionUntil"
                data-cy="retentionUntil"
                type="text"
                class="form-control"
                name="retentionUntil"
                :class="{ valid: !v$.retentionUntil.$invalid, invalid: v$.retentionUntil.$invalid }"
                v-model="v$.retentionUntil.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.convertedPersonId') }}</label>
            <input
              type="number"
              class="form-control"
              name="convertedPersonId"
              id="recruitment-application-convertedPersonId"
              data-cy="convertedPersonId"
              :class="{ valid: !v$.convertedPersonId.$invalid, invalid: v$.convertedPersonId.$invalid }"
              v-model.number="v$.convertedPersonId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.convertedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-application-convertedAt"
                data-cy="convertedAt"
                type="datetime-local"
                class="form-control"
                name="convertedAt"
                :class="{ valid: !v$.convertedAt.$invalid, invalid: v$.convertedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.convertedAt.$model)"
                @change="updateInstantField('convertedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.closeReason') }}</label>
            <textarea
              class="form-control"
              name="closeReason"
              id="recruitment-application-closeReason"
              data-cy="closeReason"
              :class="{ valid: !v$.closeReason.$invalid, invalid: v$.closeReason.$invalid }"
              v-model="v$.closeReason.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.note') }}</label>
            <textarea
              class="form-control"
              name="note"
              id="recruitment-application-note"
              data-cy="note"
              :class="{ valid: !v$.note.$invalid, invalid: v$.note.$invalid }"
              v-model="v$.note.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.candidate') }}</label>
            <select
              class="form-control"
              id="recruitment-application-candidate"
              data-cy="candidate"
              name="candidate"
              v-model="recruitmentApplication.candidate"
            >
              <option :value="null"></option>
              <option
                :value="
                  recruitmentApplication.candidate && externalCandidateOption.id === recruitmentApplication.candidate.id
                    ? recruitmentApplication.candidate
                    : externalCandidateOption
                "
                v-for="externalCandidateOption in externalCandidates"
                :key="externalCandidateOption.id"
              >
                {{ externalCandidateOption.fullName }} — {{ externalCandidateOption.email }}
              </option>
            </select>
          </div>
          <div class="mb-3" v-if="!recruitmentApplication.id && recruitmentApplication.recruitmentType === 'GRADUATE'">
            <label class="form-control-label" for="recruitment-application-requisitions">{{ $t('recruitment.fields.requisitions') }}</label>
            <select
              id="recruitment-application-requisitions"
              data-cy="requisitions"
              class="form-control"
              multiple
              size="5"
              v-model="selectedRequisitionIds"
            >
              <option
                v-for="requisitionOption in availableRecruitmentRequisitions"
                :key="requisitionOption.id"
                :value="requisitionOption.id"
              >
                {{ requisitionOption.title }} — {{ requisitionOption.department }} ({{ requisitionOption.requisitionCode }})
              </option>
            </select>
            <small class="form-text text-muted">{{ $t('recruitment.application.multiplePositionsHelp') }}</small>
          </div>
          <div class="mb-3" v-else-if="!recruitmentApplication.id">
            <label class="form-control-label" for="recruitment-application-requisition">{{ $t('recruitment.fields.requisition') }}</label>
            <select id="recruitment-application-requisition" data-cy="requisition" class="form-control" v-model="selectedRequisitionId">
              <option :value="null"></option>
              <option
                v-for="requisitionOption in availableRecruitmentRequisitions"
                :key="requisitionOption.id"
                :value="requisitionOption.id"
              >
                {{ requisitionOption.title }} — {{ requisitionOption.department }} ({{ requisitionOption.requisitionCode }})
              </option>
            </select>
          </div>
          <div class="mb-3" v-else>
            <label class="form-control-label" for="recruitment-application-requisition">{{ $t('recruitment.fields.requisition') }}</label>
            <select
              class="form-control"
              id="recruitment-application-requisition"
              data-cy="requisition"
              name="requisition"
              v-model="recruitmentApplication.requisition"
            >
              <option :value="null"></option>
              <option
                :value="
                  recruitmentApplication.requisition && item.id === recruitmentApplication.requisition.id
                    ? recruitmentApplication.requisition
                    : item
                "
                v-for="item in recruitmentRequisitions"
                :key="item.id"
              >
                {{ item.title }} — {{ item.department }} ({{ item.requisitionCode }})
              </option>
            </select>
          </div>
          <div class="mb-3" v-if="recruitmentApplication.id">
            <label class="form-control-label" for="recruitment-application">{{ $t('recruitment.fields.requisitionVersion') }}</label>
            <select
              class="form-control"
              id="recruitment-application-requisitionVersion"
              data-cy="requisitionVersion"
              name="requisitionVersion"
              v-model="recruitmentApplication.requisitionVersion"
            >
              <option :value="null"></option>
              <option
                :value="
                  recruitmentApplication.requisitionVersion &&
                  recruitmentRequisitionVersionOption.id === recruitmentApplication.requisitionVersion.id
                    ? recruitmentApplication.requisitionVersion
                    : recruitmentRequisitionVersionOption
                "
                v-for="recruitmentRequisitionVersionOption in recruitmentRequisitionVersions"
                :key="recruitmentRequisitionVersionOption.id"
              >
                {{ recruitmentRequisitionVersionOption.versionNumber }} — {{ recruitmentRequisitionVersionOption.requisition?.title }}
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
<script lang="ts" src="./recruitment-application-update.component.ts"></script>
