<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.notificationMessage.home.createOrEditLabel" data-cy="NotificationMessageCreateUpdateHeading">
          {{ $t('entity.notificationMessage.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="notificationMessage.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="notificationMessage.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.eventType') }}</label>
            <input
              type="text"
              class="form-control"
              name="eventType"
              id="notification-message-eventType"
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
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.templateId') }}</label>
            <input
              type="number"
              class="form-control"
              name="templateId"
              id="notification-message-templateId"
              data-cy="templateId"
              :class="{ valid: !v$.templateId.$invalid, invalid: v$.templateId.$invalid }"
              v-model.number="v$.templateId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.status') }}</label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="notification-message-status"
              data-cy="status"
              required
            >
              <option
                v-for="notificationMessageStatus in notificationMessageStatusValues"
                :key="notificationMessageStatus"
                :value="notificationMessageStatus"
              >
                {{ $t('recruitment.enums.' + notificationMessageStatus) }}
              </option>
            </select>
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.sendMode') }}</label>
            <input
              type="text"
              class="form-control"
              name="sendMode"
              id="notification-message-sendMode"
              data-cy="sendMode"
              :class="{ valid: !v$.sendMode.$invalid, invalid: v$.sendMode.$invalid }"
              v-model="v$.sendMode.$model"
              required
            />
            <div v-if="v$.sendMode.$anyDirty && v$.sendMode.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sendMode.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.finalSubject') }}</label>
            <input
              type="text"
              class="form-control"
              name="finalSubject"
              id="notification-message-finalSubject"
              data-cy="finalSubject"
              :class="{ valid: !v$.finalSubject.$invalid, invalid: v$.finalSubject.$invalid }"
              v-model="v$.finalSubject.$model"
              required
            />
            <div v-if="v$.finalSubject.$anyDirty && v$.finalSubject.$invalid">
              <small class="form-text text-danger" v-for="error of v$.finalSubject.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.finalBody') }}</label>
            <textarea
              class="form-control"
              name="finalBody"
              id="notification-message-finalBody"
              data-cy="finalBody"
              :class="{ valid: !v$.finalBody.$invalid, invalid: v$.finalBody.$invalid }"
              v-model="v$.finalBody.$model"
              required
            ></textarea>
            <div v-if="v$.finalBody.$anyDirty && v$.finalBody.$invalid">
              <small class="form-text text-danger" v-for="error of v$.finalBody.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.recipientsJson') }}</label>
            <textarea
              class="form-control"
              name="recipientsJson"
              id="notification-message-recipientsJson"
              data-cy="recipientsJson"
              :class="{ valid: !v$.recipientsJson.$invalid, invalid: v$.recipientsJson.$invalid }"
              v-model="v$.recipientsJson.$model"
              required
            ></textarea>
            <div v-if="v$.recipientsJson.$anyDirty && v$.recipientsJson.$invalid">
              <small class="form-text text-danger" v-for="error of v$.recipientsJson.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.retryCount') }}</label>
            <input
              type="number"
              class="form-control"
              name="retryCount"
              id="notification-message-retryCount"
              data-cy="retryCount"
              :class="{ valid: !v$.retryCount.$invalid, invalid: v$.retryCount.$invalid }"
              v-model.number="v$.retryCount.$model"
              required
            />
            <div v-if="v$.retryCount.$anyDirty && v$.retryCount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.retryCount.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.maxRetries') }}</label>
            <input
              type="number"
              class="form-control"
              name="maxRetries"
              id="notification-message-maxRetries"
              data-cy="maxRetries"
              :class="{ valid: !v$.maxRetries.$invalid, invalid: v$.maxRetries.$invalid }"
              v-model.number="v$.maxRetries.$model"
              required
            />
            <div v-if="v$.maxRetries.$anyDirty && v$.maxRetries.$invalid">
              <small class="form-text text-danger" v-for="error of v$.maxRetries.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.lastError') }}</label>
            <textarea
              class="form-control"
              name="lastError"
              id="notification-message-lastError"
              data-cy="lastError"
              :class="{ valid: !v$.lastError.$invalid, invalid: v$.lastError.$invalid }"
              v-model="v$.lastError.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.createdBy') }}</label>
            <input
              type="text"
              class="form-control"
              name="createdBy"
              id="notification-message-createdBy"
              data-cy="createdBy"
              :class="{ valid: !v$.createdBy.$invalid, invalid: v$.createdBy.$invalid }"
              v-model="v$.createdBy.$model"
              required
            />
            <div v-if="v$.createdBy.$anyDirty && v$.createdBy.$invalid">
              <small class="form-text text-danger" v-for="error of v$.createdBy.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.confirmedBy') }}</label>
            <input
              type="text"
              class="form-control"
              name="confirmedBy"
              id="notification-message-confirmedBy"
              data-cy="confirmedBy"
              :class="{ valid: !v$.confirmedBy.$invalid, invalid: v$.confirmedBy.$invalid }"
              v-model="v$.confirmedBy.$model"
            />
            <div v-if="v$.confirmedBy.$anyDirty && v$.confirmedBy.$invalid">
              <small class="form-text text-danger" v-for="error of v$.confirmedBy.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.createdAt') }}</label>
            <div class="d-flex">
              <input
                id="notification-message-createdAt"
                data-cy="createdAt"
                type="datetime-local"
                class="form-control"
                name="createdAt"
                :class="{ valid: !v$.createdAt.$invalid, invalid: v$.createdAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.createdAt.$model)"
                @change="updateInstantField('createdAt', $event)"
              />
            </div>
            <div v-if="v$.createdAt.$anyDirty && v$.createdAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.createdAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.confirmedAt') }}</label>
            <div class="d-flex">
              <input
                id="notification-message-confirmedAt"
                data-cy="confirmedAt"
                type="datetime-local"
                class="form-control"
                name="confirmedAt"
                :class="{ valid: !v$.confirmedAt.$invalid, invalid: v$.confirmedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.confirmedAt.$model)"
                @change="updateInstantField('confirmedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.sentAt') }}</label>
            <div class="d-flex">
              <input
                id="notification-message-sentAt"
                data-cy="sentAt"
                type="datetime-local"
                class="form-control"
                name="sentAt"
                :class="{ valid: !v$.sentAt.$invalid, invalid: v$.sentAt.$invalid }"
                :value="convertDateTimeFromServer(v$.sentAt.$model)"
                @change="updateInstantField('sentAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification-message">{{ $t('recruitment.fields.application') }}</label>
            <select
              class="form-control"
              id="notification-message-application"
              data-cy="application"
              name="application"
              v-model="notificationMessage.application"
            >
              <option :value="null"></option>
              <option
                :value="
                  notificationMessage.application && recruitmentApplicationOption.id === notificationMessage.application.id
                    ? notificationMessage.application
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
<script lang="ts" src="./notification-message-update.component.ts"></script>
