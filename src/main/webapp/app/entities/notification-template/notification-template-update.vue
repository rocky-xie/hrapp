<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.notificationTemplate.home.createOrEditLabel" data-cy="NotificationTemplateCreateUpdateHeading">
          {{ $t('entity.notificationTemplate.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="notificationTemplate.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="notificationTemplate.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-template">{{ $t('recruitment.fields.eventType') }}</label>
            <input
              type="text"
              class="form-control"
              name="eventType"
              id="notification-template-eventType"
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
            <label class="form-control-label" for="notification-template">{{ $t('recruitment.fields.recruitmentType') }}</label>
            <select
              class="form-control"
              name="recruitmentType"
              :class="{ valid: !v$.recruitmentType.$invalid, invalid: v$.recruitmentType.$invalid }"
              v-model="v$.recruitmentType.$model"
              id="notification-template-recruitmentType"
              data-cy="recruitmentType"
            >
              <option v-for="recruitmentType in recruitmentTypeValues" :key="recruitmentType" :value="recruitmentType">
                {{ $t('recruitment.enums.' + recruitmentType) }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-template">{{ $t('recruitment.fields.language') }}</label>
            <input
              type="text"
              class="form-control"
              name="language"
              id="notification-template-language"
              data-cy="language"
              :class="{ valid: !v$.language.$invalid, invalid: v$.language.$invalid }"
              v-model="v$.language.$model"
              required
            />
            <div v-if="v$.language.$anyDirty && v$.language.$invalid">
              <small class="form-text text-danger" v-for="error of v$.language.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-template">{{ $t('recruitment.fields.versionNumber') }}</label>
            <input
              type="number"
              class="form-control"
              name="versionNumber"
              id="notification-template-versionNumber"
              data-cy="versionNumber"
              :class="{ valid: !v$.versionNumber.$invalid, invalid: v$.versionNumber.$invalid }"
              v-model.number="v$.versionNumber.$model"
              required
            />
            <div v-if="v$.versionNumber.$anyDirty && v$.versionNumber.$invalid">
              <small class="form-text text-danger" v-for="error of v$.versionNumber.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-template">{{ $t('recruitment.fields.subjectTemplate') }}</label>
            <input
              type="text"
              class="form-control"
              name="subjectTemplate"
              id="notification-template-subjectTemplate"
              data-cy="subjectTemplate"
              :class="{ valid: !v$.subjectTemplate.$invalid, invalid: v$.subjectTemplate.$invalid }"
              v-model="v$.subjectTemplate.$model"
              required
            />
            <div v-if="v$.subjectTemplate.$anyDirty && v$.subjectTemplate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.subjectTemplate.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-template">{{ $t('recruitment.fields.bodyTemplate') }}</label>
            <textarea
              class="form-control"
              name="bodyTemplate"
              id="notification-template-bodyTemplate"
              data-cy="bodyTemplate"
              :class="{ valid: !v$.bodyTemplate.$invalid, invalid: v$.bodyTemplate.$invalid }"
              v-model="v$.bodyTemplate.$model"
              required
            ></textarea>
            <div v-if="v$.bodyTemplate.$anyDirty && v$.bodyTemplate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.bodyTemplate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-template">{{ $t('recruitment.fields.externalSafe') }}</label>
            <input
              type="checkbox"
              class="form-control"
              name="externalSafe"
              id="notification-template-externalSafe"
              data-cy="externalSafe"
              :class="{ valid: !v$.externalSafe.$invalid, invalid: v$.externalSafe.$invalid }"
              v-model="v$.externalSafe.$model"
              required
            />
            <div v-if="v$.externalSafe.$anyDirty && v$.externalSafe.$invalid">
              <small class="form-text text-danger" v-for="error of v$.externalSafe.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-template">{{ $t('recruitment.fields.published') }}</label>
            <input
              type="checkbox"
              class="form-control"
              name="published"
              id="notification-template-published"
              data-cy="published"
              :class="{ valid: !v$.published.$invalid, invalid: v$.published.$invalid }"
              v-model="v$.published.$model"
              required
            />
            <div v-if="v$.published.$anyDirty && v$.published.$invalid">
              <small class="form-text text-danger" v-for="error of v$.published.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./notification-template-update.component.ts"></script>
