<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.applicationEvent.home.createOrEditLabel" data-cy="ApplicationEventCreateUpdateHeading">
          {{ $t('entity.applicationEvent.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="applicationEvent.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="applicationEvent.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-event">{{ $t('recruitment.fields.stageType') }}</label>
            <select
              class="form-control"
              name="stageType"
              :class="{ valid: !v$.stageType.$invalid, invalid: v$.stageType.$invalid }"
              v-model="v$.stageType.$model"
              id="application-event-stageType"
              data-cy="stageType"
            >
              <option v-for="applicationStageType in applicationStageTypeValues" :key="applicationStageType" :value="applicationStageType">
                {{ $t('recruitment.enums.' + applicationStageType) }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-event">{{ $t('recruitment.fields.eventType') }}</label>
            <input
              type="text"
              class="form-control"
              name="eventType"
              id="application-event-eventType"
              data-cy="eventType"
              :class="{ valid: !v$.eventType.$invalid, invalid: v$.eventType.$invalid }"
              v-model="v$.eventType.$model"
              required
            />
            <div v-if="v$.eventType.$anyDirty && v$.eventType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.eventType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-event">{{ $t('recruitment.fields.previousValue') }}</label>
            <input
              type="text"
              class="form-control"
              name="previousValue"
              id="application-event-previousValue"
              data-cy="previousValue"
              :class="{ valid: !v$.previousValue.$invalid, invalid: v$.previousValue.$invalid }"
              v-model="v$.previousValue.$model"
            />
            <div v-if="v$.previousValue.$anyDirty && v$.previousValue.$invalid">
              <small class="form-text text-danger" v-for="error of v$.previousValue.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-event">{{ $t('recruitment.fields.newValue') }}</label>
            <input
              type="text"
              class="form-control"
              name="newValue"
              id="application-event-newValue"
              data-cy="newValue"
              :class="{ valid: !v$.newValue.$invalid, invalid: v$.newValue.$invalid }"
              v-model="v$.newValue.$model"
            />
            <div v-if="v$.newValue.$anyDirty && v$.newValue.$invalid">
              <small class="form-text text-danger" v-for="error of v$.newValue.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-event">{{ $t('recruitment.fields.actor') }}</label>
            <input
              type="text"
              class="form-control"
              name="actor"
              id="application-event-actor"
              data-cy="actor"
              :class="{ valid: !v$.actor.$invalid, invalid: v$.actor.$invalid }"
              v-model="v$.actor.$model"
              required
            />
            <div v-if="v$.actor.$anyDirty && v$.actor.$invalid">
              <small class="form-text text-danger" v-for="error of v$.actor.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-event">{{ $t('recruitment.fields.occurredAt') }}</label>
            <div class="d-flex">
              <input
                id="application-event-occurredAt"
                data-cy="occurredAt"
                type="datetime-local"
                class="form-control"
                name="occurredAt"
                :class="{ valid: !v$.occurredAt.$invalid, invalid: v$.occurredAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.occurredAt.$model)"
                @change="updateInstantField('occurredAt', $event)"
              />
            </div>
            <div v-if="v$.occurredAt.$anyDirty && v$.occurredAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.occurredAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-event">{{ $t('recruitment.fields.reason') }}</label>
            <textarea
              class="form-control"
              name="reason"
              id="application-event-reason"
              data-cy="reason"
              :class="{ valid: !v$.reason.$invalid, invalid: v$.reason.$invalid }"
              v-model="v$.reason.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-event">{{ $t('recruitment.fields.metadataJson') }}</label>
            <textarea
              class="form-control"
              name="metadataJson"
              id="application-event-metadataJson"
              data-cy="metadataJson"
              :class="{ valid: !v$.metadataJson.$invalid, invalid: v$.metadataJson.$invalid }"
              v-model="v$.metadataJson.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="application-event">{{ $t('recruitment.fields.application') }}</label>
            <select
              class="form-control"
              id="application-event-application"
              data-cy="application"
              name="application"
              v-model="applicationEvent.application"
            >
              <option :value="null"></option>
              <option
                :value="
                  applicationEvent.application && recruitmentApplicationOption.id === applicationEvent.application.id
                    ? applicationEvent.application
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
<script lang="ts" src="./application-event-update.component.ts"></script>
