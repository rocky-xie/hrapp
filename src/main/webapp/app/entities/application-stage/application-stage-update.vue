<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.applicationStage.home.createOrEditLabel" data-cy="ApplicationStageCreateUpdateHeading">
          {{ $t('entity.applicationStage.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="applicationStage.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="applicationStage.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-stage">{{ $t('recruitment.fields.stageType') }}</label>
            <select
              class="form-control"
              name="stageType"
              :class="{ valid: !v$.stageType.$invalid, invalid: v$.stageType.$invalid }"
              v-model="v$.stageType.$model"
              id="application-stage-stageType"
              data-cy="stageType"
              required
            >
              <option v-for="applicationStageType in applicationStageTypeValues" :key="applicationStageType" :value="applicationStageType">
                {{ $t('recruitment.enums.' + applicationStageType) }}
              </option>
            </select>
            <div v-if="v$.stageType.$anyDirty && v$.stageType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.stageType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-stage">{{ $t('recruitment.fields.sequenceNumber') }}</label>
            <input
              type="number"
              class="form-control"
              name="sequenceNumber"
              id="application-stage-sequenceNumber"
              data-cy="sequenceNumber"
              :class="{ valid: !v$.sequenceNumber.$invalid, invalid: v$.sequenceNumber.$invalid }"
              v-model.number="v$.sequenceNumber.$model"
              required
            />
            <div v-if="v$.sequenceNumber.$anyDirty && v$.sequenceNumber.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sequenceNumber.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-stage">{{ $t('recruitment.fields.stageStatus') }}</label>
            <input
              type="text"
              class="form-control"
              name="stageStatus"
              id="application-stage-stageStatus"
              data-cy="stageStatus"
              :class="{ valid: !v$.stageStatus.$invalid, invalid: v$.stageStatus.$invalid }"
              v-model="v$.stageStatus.$model"
              required
            />
            <div v-if="v$.stageStatus.$anyDirty && v$.stageStatus.$invalid">
              <small class="form-text text-danger" v-for="error of v$.stageStatus.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-stage">{{ $t('recruitment.fields.startedAt') }}</label>
            <div class="d-flex">
              <input
                id="application-stage-startedAt"
                data-cy="startedAt"
                type="datetime-local"
                class="form-control"
                name="startedAt"
                :class="{ valid: !v$.startedAt.$invalid, invalid: v$.startedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.startedAt.$model)"
                @change="updateInstantField('startedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-stage">{{ $t('recruitment.fields.completedAt') }}</label>
            <div class="d-flex">
              <input
                id="application-stage-completedAt"
                data-cy="completedAt"
                type="datetime-local"
                class="form-control"
                name="completedAt"
                :class="{ valid: !v$.completedAt.$invalid, invalid: v$.completedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.completedAt.$model)"
                @change="updateInstantField('completedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-stage">{{ $t('recruitment.fields.selected') }}</label>
            <input
              type="checkbox"
              class="form-control"
              name="selected"
              id="application-stage-selected"
              data-cy="selected"
              :class="{ valid: !v$.selected.$invalid, invalid: v$.selected.$invalid }"
              v-model="v$.selected.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-stage">{{ $t('recruitment.fields.decisionReason') }}</label>
            <textarea
              class="form-control"
              name="decisionReason"
              id="application-stage-decisionReason"
              data-cy="decisionReason"
              :class="{ valid: !v$.decisionReason.$invalid, invalid: v$.decisionReason.$invalid }"
              v-model="v$.decisionReason.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-stage">{{ $t('recruitment.fields.application') }}</label>
            <select
              class="form-control"
              id="application-stage-application"
              data-cy="application"
              name="application"
              v-model="applicationStage.application"
            >
              <option :value="null"></option>
              <option
                :value="
                  applicationStage.application && recruitmentApplicationOption.id === applicationStage.application.id
                    ? applicationStage.application
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
<script lang="ts" src="./application-stage-update.component.ts"></script>
