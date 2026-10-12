<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.preboardingFollowUp.home.createOrEditLabel" data-cy="PreboardingFollowUpCreateUpdateHeading">
          {{ $t('entity.preboardingFollowUp.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="preboardingFollowUp.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="preboardingFollowUp.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.candidateId') }}</label>
            <input
              type="number"
              class="form-control"
              name="candidateId"
              id="preboarding-follow-up-candidateId"
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
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.personId') }}</label>
            <input
              type="number"
              class="form-control"
              name="personId"
              id="preboarding-follow-up-personId"
              data-cy="personId"
              :class="{ valid: !v$.personId.$invalid, invalid: v$.personId.$invalid }"
              v-model.number="v$.personId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.plannedAt') }}</label>
            <div class="d-flex">
              <input
                id="preboarding-follow-up-plannedAt"
                data-cy="plannedAt"
                type="datetime-local"
                class="form-control"
                name="plannedAt"
                :class="{ valid: !v$.plannedAt.$invalid, invalid: v$.plannedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.plannedAt.$model)"
                @change="updateInstantField('plannedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.contactedAt') }}</label>
            <div class="d-flex">
              <input
                id="preboarding-follow-up-contactedAt"
                data-cy="contactedAt"
                type="datetime-local"
                class="form-control"
                name="contactedAt"
                :class="{ valid: !v$.contactedAt.$invalid, invalid: v$.contactedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.contactedAt.$model)"
                @change="updateInstantField('contactedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.ownerUsername') }}</label>
            <input
              type="text"
              class="form-control"
              name="ownerUsername"
              id="preboarding-follow-up-ownerUsername"
              data-cy="ownerUsername"
              :class="{ valid: !v$.ownerUsername.$invalid, invalid: v$.ownerUsername.$invalid }"
              v-model="v$.ownerUsername.$model"
              required
            />
            <div v-if="v$.ownerUsername.$anyDirty && v$.ownerUsername.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ownerUsername.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.contactMethod') }}</label>
            <input
              type="text"
              class="form-control"
              name="contactMethod"
              id="preboarding-follow-up-contactMethod"
              data-cy="contactMethod"
              :class="{ valid: !v$.contactMethod.$invalid, invalid: v$.contactMethod.$invalid }"
              v-model="v$.contactMethod.$model"
            />
            <div v-if="v$.contactMethod.$anyDirty && v$.contactMethod.$invalid">
              <small class="form-text text-danger" v-for="error of v$.contactMethod.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.result') }}</label>
            <textarea
              class="form-control"
              name="result"
              id="preboarding-follow-up-result"
              data-cy="result"
              :class="{ valid: !v$.result.$invalid, invalid: v$.result.$invalid }"
              v-model="v$.result.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.candidateIntent') }}</label>
            <input
              type="text"
              class="form-control"
              name="candidateIntent"
              id="preboarding-follow-up-candidateIntent"
              data-cy="candidateIntent"
              :class="{ valid: !v$.candidateIntent.$invalid, invalid: v$.candidateIntent.$invalid }"
              v-model="v$.candidateIntent.$model"
            />
            <div v-if="v$.candidateIntent.$anyDirty && v$.candidateIntent.$invalid">
              <small class="form-text text-danger" v-for="error of v$.candidateIntent.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.riskLevel') }}</label>
            <input
              type="text"
              class="form-control"
              name="riskLevel"
              id="preboarding-follow-up-riskLevel"
              data-cy="riskLevel"
              :class="{ valid: !v$.riskLevel.$invalid, invalid: v$.riskLevel.$invalid }"
              v-model="v$.riskLevel.$model"
            />
            <div v-if="v$.riskLevel.$anyDirty && v$.riskLevel.$invalid">
              <small class="form-text text-danger" v-for="error of v$.riskLevel.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.nextAction') }}</label>
            <textarea
              class="form-control"
              name="nextAction"
              id="preboarding-follow-up-nextAction"
              data-cy="nextAction"
              :class="{ valid: !v$.nextAction.$invalid, invalid: v$.nextAction.$invalid }"
              v-model="v$.nextAction.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.nextContactAt') }}</label>
            <div class="d-flex">
              <input
                id="preboarding-follow-up-nextContactAt"
                data-cy="nextContactAt"
                type="datetime-local"
                class="form-control"
                name="nextContactAt"
                :class="{ valid: !v$.nextContactAt.$invalid, invalid: v$.nextContactAt.$invalid }"
                :value="convertDateTimeFromServer(v$.nextContactAt.$model)"
                @change="updateInstantField('nextContactAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="preboarding-follow-up">{{ $t('recruitment.fields.application') }}</label>
            <select
              class="form-control"
              id="preboarding-follow-up-application"
              data-cy="application"
              name="application"
              v-model="preboardingFollowUp.application"
            >
              <option :value="null"></option>
              <option
                :value="
                  preboardingFollowUp.application && recruitmentApplicationOption.id === preboardingFollowUp.application.id
                    ? preboardingFollowUp.application
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
<script lang="ts" src="./preboarding-follow-up-update.component.ts"></script>
