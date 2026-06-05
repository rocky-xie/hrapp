<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.successionCandidate.home.createOrEditLabel" data-cy="SuccessionCandidateCreateUpdateHeading">
          {{ $t('entity.successionCandidate.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="successionCandidate.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="successionCandidate.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="succession-candidate">{{
              $t('entity.successionCandidate.field.successionReadiness')
            }}</label>
            <select
              class="form-control"
              name="successionReadiness"
              :class="{ valid: !v$.successionReadiness.$invalid, invalid: v$.successionReadiness.$invalid }"
              v-model="v$.successionReadiness.$model"
              id="succession-candidate-successionReadiness"
              data-cy="successionReadiness"
              required
            >
              <option v-for="readinessLevel in readinessLevelValues" :key="readinessLevel" :value="readinessLevel">
                {{ readinessLevel }}
              </option>
            </select>
            <div v-if="v$.successionReadiness.$anyDirty && v$.successionReadiness.$invalid">
              <small class="form-text text-danger" v-for="error of v$.successionReadiness.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="succession-candidate">{{
              $t('entity.successionCandidate.field.requiredTraining')
            }}</label>
            <textarea
              class="form-control"
              name="requiredTraining"
              id="succession-candidate-requiredTraining"
              data-cy="requiredTraining"
              :class="{ valid: !v$.requiredTraining.$invalid, invalid: v$.requiredTraining.$invalid }"
              v-model="v$.requiredTraining.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="succession-candidate">{{
              $t('entity.successionCandidate.field.estimatedTimeToReady')
            }}</label>
            <input
              type="text"
              class="form-control"
              name="estimatedTimeToReady"
              id="succession-candidate-estimatedTimeToReady"
              data-cy="estimatedTimeToReady"
              :class="{ valid: !v$.estimatedTimeToReady.$invalid, invalid: v$.estimatedTimeToReady.$invalid }"
              v-model="v$.estimatedTimeToReady.$model"
            />
            <div v-if="v$.estimatedTimeToReady.$anyDirty && v$.estimatedTimeToReady.$invalid">
              <small class="form-text text-danger" v-for="error of v$.estimatedTimeToReady.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="succession-candidate">{{
              $t('entity.successionCandidate.field.riskAfterTraining')
            }}</label>
            <select
              class="form-control"
              name="riskAfterTraining"
              :class="{ valid: !v$.riskAfterTraining.$invalid, invalid: v$.riskAfterTraining.$invalid }"
              v-model="v$.riskAfterTraining.$model"
              id="succession-candidate-riskAfterTraining"
              data-cy="riskAfterTraining"
            >
              <option v-for="riskLevel in riskLevelValues" :key="riskLevel" :value="riskLevel">{{ riskLevel }}</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="succession-candidate">{{ $t('entity.successionCandidate.field.reviewDate') }}</label>
            <b-form-input
              id="succession-candidate-reviewDate"
              data-cy="reviewDate"
              type="date"
              class="form-control"
              name="reviewDate"
              :class="{ 'is-valid': !v$.reviewDate.$invalid, 'is-invalid': v$.reviewDate.$invalid }"
              v-model="v$.reviewDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="succession-candidate">{{ $t('entity.successionCandidate.field.priority') }}</label>
            <input
              type="number"
              class="form-control"
              name="priority"
              id="succession-candidate-priority"
              data-cy="priority"
              :class="{ valid: !v$.priority.$invalid, invalid: v$.priority.$invalid }"
              v-model.number="v$.priority.$model"
            />
            <div v-if="v$.priority.$anyDirty && v$.priority.$invalid">
              <small class="form-text text-danger" v-for="error of v$.priority.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <fieldset class="border rounded p-3 mb-4">
            <legend class="h5 w-auto px-2 mb-0">{{ $t('entity.successionCandidate.section.positionAndOwner') }}</legend>
            <div class="mb-3">
              <label class="form-control-label" for="succession-candidate">{{ $t('entity.position.detail.title') }}</label>
              <select
                class="form-control"
                id="succession-candidate-position"
                data-cy="position"
                name="position"
                v-model="successionCandidate.position"
                required
              >
                <option v-if="!successionCandidate.position" :value="null" selected></option>
                <option
                  :value="
                    successionCandidate.position && positionOption.id === successionCandidate.position.id
                      ? successionCandidate.position
                      : positionOption
                  "
                  v-for="positionOption in positions"
                  :key="positionOption.id"
                >
                  {{ positionOption.positionName }}
                </option>
              </select>
            </div>
            <div v-if="v$.position.$anyDirty && v$.position.$invalid">
              <small class="form-text text-danger" v-for="error of v$.position.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="succession-candidate">{{ $t('global.entity.field.currentOwner') }}</label>
              <select
                class="form-control"
                id="succession-candidate-currentOwner"
                data-cy="currentOwner"
                name="currentOwner"
                v-model="successionCandidate.currentOwner"
              >
                <option :value="null"></option>
                <option
                  :value="
                    successionCandidate.currentOwner && personOption.id === successionCandidate.currentOwner.id
                      ? successionCandidate.currentOwner
                      : personOption
                  "
                  v-for="personOption in availableCurrentOwners"
                  :key="personOption.id"
                >
                  {{ personOption.personName }}
                </option>
              </select>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="succession-candidate">{{ $t('global.entity.field.candidate') }}</label>
              <select
                class="form-control"
                id="succession-candidate-candidate"
                data-cy="candidate"
                name="candidate"
                v-model="successionCandidate.candidate"
                required
              >
                <option v-if="!successionCandidate.candidate" :value="null" selected></option>
                <option
                  :value="
                    successionCandidate.candidate && personOption.id === successionCandidate.candidate.id
                      ? successionCandidate.candidate
                      : personOption
                  "
                  v-for="personOption in availableCandidates"
                  :key="personOption.id"
                >
                  {{ personOption.personName }}
                </option>
              </select>
            </div>
            <div v-if="v$.candidate.$anyDirty && v$.candidate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.candidate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </fieldset>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.cancel') }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.save') }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./succession-candidate-update.component.ts"></script>
