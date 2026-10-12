<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.selectionActivity.home.createOrEditLabel" data-cy="SelectionActivityCreateUpdateHeading">
          {{ $t('entity.selectionActivity.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="selectionActivity.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="selectionActivity.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.stageType') }}</label>
            <select
              class="form-control"
              name="stageType"
              :class="{ valid: !v$.stageType.$invalid, invalid: v$.stageType.$invalid }"
              v-model="v$.stageType.$model"
              id="selection-activity-stageType"
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
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.activityType') }}</label>
            <select
              class="form-control"
              name="activityType"
              :class="{ valid: !v$.activityType.$invalid, invalid: v$.activityType.$invalid }"
              v-model="v$.activityType.$model"
              id="selection-activity-activityType"
              data-cy="activityType"
              required
            >
              <option
                v-for="selectionActivityType in selectionActivityTypeValues"
                :key="selectionActivityType"
                :value="selectionActivityType"
              >
                {{ $t('recruitment.enums.' + selectionActivityType) }}
              </option>
            </select>
            <div v-if="v$.activityType.$anyDirty && v$.activityType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.activityType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.startAt') }}</label>
            <div class="d-flex">
              <input
                id="selection-activity-startAt"
                data-cy="startAt"
                type="datetime-local"
                class="form-control"
                name="startAt"
                :class="{ valid: !v$.startAt.$invalid, invalid: v$.startAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.startAt.$model)"
                @change="updateInstantField('startAt', $event)"
              />
            </div>
            <div v-if="v$.startAt.$anyDirty && v$.startAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.startAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.endAt') }}</label>
            <div class="d-flex">
              <input
                id="selection-activity-endAt"
                data-cy="endAt"
                type="datetime-local"
                class="form-control"
                name="endAt"
                :class="{ valid: !v$.endAt.$invalid, invalid: v$.endAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.endAt.$model)"
                @change="updateInstantField('endAt', $event)"
              />
            </div>
            <div v-if="v$.endAt.$anyDirty && v$.endAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.endAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.meetingRoomId') }}</label>
            <input
              type="number"
              class="form-control"
              name="meetingRoomId"
              id="selection-activity-meetingRoomId"
              data-cy="meetingRoomId"
              :class="{ valid: !v$.meetingRoomId.$invalid, invalid: v$.meetingRoomId.$invalid }"
              v-model.number="v$.meetingRoomId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.location') }}</label>
            <input
              type="text"
              class="form-control"
              name="location"
              id="selection-activity-location"
              data-cy="location"
              :class="{ valid: !v$.location.$invalid, invalid: v$.location.$invalid }"
              v-model="v$.location.$model"
            />
            <div v-if="v$.location.$anyDirty && v$.location.$invalid">
              <small class="form-text text-danger" v-for="error of v$.location.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.status') }}</label>
            <input
              type="text"
              class="form-control"
              name="status"
              id="selection-activity-status"
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
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.scheduleVersion') }}</label>
            <input
              type="number"
              class="form-control"
              name="scheduleVersion"
              id="selection-activity-scheduleVersion"
              data-cy="scheduleVersion"
              :class="{ valid: !v$.scheduleVersion.$invalid, invalid: v$.scheduleVersion.$invalid }"
              v-model.number="v$.scheduleVersion.$model"
              required
            />
            <div v-if="v$.scheduleVersion.$anyDirty && v$.scheduleVersion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.scheduleVersion.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.changeReason') }}</label>
            <textarea
              class="form-control"
              name="changeReason"
              id="selection-activity-changeReason"
              data-cy="changeReason"
              :class="{ valid: !v$.changeReason.$invalid, invalid: v$.changeReason.$invalid }"
              v-model="v$.changeReason.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.externalCalendarProvider') }}</label>
            <input
              type="text"
              class="form-control"
              name="externalCalendarProvider"
              id="selection-activity-externalCalendarProvider"
              data-cy="externalCalendarProvider"
              :class="{ valid: !v$.externalCalendarProvider.$invalid, invalid: v$.externalCalendarProvider.$invalid }"
              v-model="v$.externalCalendarProvider.$model"
            />
            <div v-if="v$.externalCalendarProvider.$anyDirty && v$.externalCalendarProvider.$invalid">
              <small class="form-text text-danger" v-for="error of v$.externalCalendarProvider.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.externalEventId') }}</label>
            <input
              type="text"
              class="form-control"
              name="externalEventId"
              id="selection-activity-externalEventId"
              data-cy="externalEventId"
              :class="{ valid: !v$.externalEventId.$invalid, invalid: v$.externalEventId.$invalid }"
              v-model="v$.externalEventId.$model"
            />
            <div v-if="v$.externalEventId.$anyDirty && v$.externalEventId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.externalEventId.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="selection-activity">{{ $t('recruitment.fields.application') }}</label>
            <select
              class="form-control"
              id="selection-activity-application"
              data-cy="application"
              name="application"
              v-model="selectionActivity.application"
            >
              <option :value="null"></option>
              <option
                :value="
                  selectionActivity.application && recruitmentApplicationOption.id === selectionActivity.application.id
                    ? selectionActivity.application
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
<script lang="ts" src="./selection-activity-update.component.ts"></script>
