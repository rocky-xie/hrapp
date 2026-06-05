<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.staffSubstitution.home.createOrEditLabel" data-cy="StaffSubstitutionCreateUpdateHeading">
          {{ $t('entity.staffSubstitution.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="staffSubstitution.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="staffSubstitution.id" readonly />
          </div>
          <fieldset class="border rounded p-3 mb-4">
            <legend class="h5 w-auto px-2 mb-0">{{ $t('entity.staffSubstitution.section.evaluationResult') }}</legend>
            <div class="mb-3">
              <label class="form-control-label" for="staff-substitution">{{ $t('entity.staffSubstitution.field.coverageRate') }}</label>
              <input
                type="number"
                class="form-control"
                name="coverageRate"
                id="staff-substitution-coverageRate"
                data-cy="coverageRate"
                :class="{ valid: !v$.coverageRate.$invalid, invalid: v$.coverageRate.$invalid }"
                v-model.number="v$.coverageRate.$model"
                readonly
              />
              <div v-if="v$.coverageRate.$anyDirty && v$.coverageRate.$invalid">
                <small class="form-text text-danger" v-for="error of v$.coverageRate.$errors" :key="error.$uid">{{ error.$message }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="staff-substitution">{{ $t('entity.staffSubstitution.field.totalSkillCount') }}</label>
              <input
                type="number"
                class="form-control"
                name="totalSkillCount"
                id="staff-substitution-totalSkillCount"
                data-cy="totalSkillCount"
                :class="{ valid: !v$.totalSkillCount.$invalid, invalid: v$.totalSkillCount.$invalid }"
                v-model.number="v$.totalSkillCount.$model"
                readonly
              />
              <div v-if="v$.totalSkillCount.$anyDirty && v$.totalSkillCount.$invalid">
                <small class="form-text text-danger" v-for="error of v$.totalSkillCount.$errors" :key="error.$uid">{{
                  error.$message
                }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="staff-substitution">{{
                $t('entity.staffSubstitution.field.coveredSkillCount')
              }}</label>
              <input
                type="number"
                class="form-control"
                name="coveredSkillCount"
                id="staff-substitution-coveredSkillCount"
                data-cy="coveredSkillCount"
                :class="{ valid: !v$.coveredSkillCount.$invalid, invalid: v$.coveredSkillCount.$invalid }"
                v-model.number="v$.coveredSkillCount.$model"
                readonly
              />
              <div v-if="v$.coveredSkillCount.$anyDirty && v$.coveredSkillCount.$invalid">
                <small class="form-text text-danger" v-for="error of v$.coveredSkillCount.$errors" :key="error.$uid">{{
                  error.$message
                }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="staff-substitution">{{ $t('entity.staffSubstitution.field.missingSkills') }}</label>
              <textarea
                class="form-control"
                name="missingSkills"
                id="staff-substitution-missingSkills"
                data-cy="missingSkills"
                :class="{ valid: !v$.missingSkills.$invalid, invalid: v$.missingSkills.$invalid }"
                v-model="v$.missingSkills.$model"
                readonly
              ></textarea>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="staff-substitution">{{ $t('entity.staffSubstitution.field.substitutable') }}</label>
              <b-form-checkbox
                v-model="v$.substitutable.$model"
                name="substitutable"
                id="staff-substitution-substitutable"
                data-cy="substitutable"
                disabled
              />
              <div v-if="v$.substitutable.$anyDirty && v$.substitutable.$invalid">
                <small class="form-text text-danger" v-for="error of v$.substitutable.$errors" :key="error.$uid">{{
                  error.$message
                }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="staff-substitution">{{ $t('entity.evaluation.field.evaluationDate') }}</label>
              <b-form-input
                id="staff-substitution-evaluationDate"
                data-cy="evaluationDate"
                type="date"
                class="form-control"
                name="evaluationDate"
                :class="{ 'is-valid': !v$.evaluationDate.$invalid, 'is-invalid': v$.evaluationDate.$invalid }"
                v-model="v$.evaluationDate.$model"
                readonly
              />
              <div v-if="v$.evaluationDate.$anyDirty && v$.evaluationDate.$invalid">
                <small class="form-text text-danger" v-for="error of v$.evaluationDate.$errors" :key="error.$uid">{{
                  error.$message
                }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="staff-substitution">{{ $t('entity.staffSubstitution.field.reason') }}</label>
              <textarea
                class="form-control"
                name="reason"
                id="staff-substitution-reason"
                data-cy="reason"
                :class="{ valid: !v$.reason.$invalid, invalid: v$.reason.$invalid }"
                v-model="v$.reason.$model"
                readonly
              ></textarea>
            </div>
          </fieldset>
          <fieldset class="border rounded p-3 mb-4">
            <legend class="h5 w-auto px-2 mb-0">{{ $t('global.entity.section.inputFields') }}</legend>
            <div class="mb-3">
              <label class="form-control-label" for="staff-substitution">{{ $t('entity.staffSubstitution.field.thresholdRate') }}</label>
              <input
                type="number"
                class="form-control"
                name="thresholdRate"
                id="staff-substitution-thresholdRate"
                data-cy="thresholdRate"
                :class="{ valid: !v$.thresholdRate.$invalid, invalid: v$.thresholdRate.$invalid }"
                v-model.number="v$.thresholdRate.$model"
                @change="onThresholdRateChange"
                required
              />
              <div v-if="v$.thresholdRate.$anyDirty && v$.thresholdRate.$invalid">
                <small class="form-text text-danger" v-for="error of v$.thresholdRate.$errors" :key="error.$uid">{{
                  error.$message
                }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="staff-substitution">{{ $t('entity.position.detail.title') }}</label>
              <select
                class="form-control"
                id="staff-substitution-position"
                data-cy="position"
                name="position"
                v-model="staffSubstitution.position"
                @change="onPositionChange"
                required
              >
                <option v-if="!staffSubstitution.position" :value="null" selected></option>
                <option
                  :value="
                    staffSubstitution.position && positionOption.id === staffSubstitution.position.id
                      ? staffSubstitution.position
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
              <label class="form-control-label" for="staff-substitution">{{ $t('global.entity.field.candidatePerson') }}</label>
              <select
                class="form-control"
                id="staff-substitution-candidatePerson"
                data-cy="candidatePerson"
                name="candidatePerson"
                v-model="staffSubstitution.candidatePerson"
                @change="onCandidatePersonChange"
                required
              >
                <option v-if="!staffSubstitution.candidatePerson" :value="null" selected></option>
                <option
                  :value="
                    staffSubstitution.candidatePerson && personOption.id === staffSubstitution.candidatePerson.id
                      ? staffSubstitution.candidatePerson
                      : personOption
                  "
                  v-for="personOption in candidatePeople"
                  :key="personOption.id"
                >
                  {{ personOption.personName }}
                </option>
              </select>
            </div>
            <div v-if="v$.candidatePerson.$anyDirty && v$.candidatePerson.$invalid">
              <small class="form-text text-danger" v-for="error of v$.candidatePerson.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </fieldset>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.cancel') }}</span>
          </button>
          <button
            type="button"
            id="recalculate-entity"
            data-cy="entityCreateRecalculateButton"
            class="btn btn-info me-2"
            :disabled="!staffSubstitution.position || !staffSubstitution.candidatePerson || isRefreshing"
            @click="recalculate()"
          >
            <font-awesome-icon icon="sync" :spin="isRefreshing"></font-awesome-icon>&nbsp;<span>{{
              $t('entity.staffSubstitution.action.recalculate')
            }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ staffSubstitution.id ? 'Save' : 'Calculate and Save' }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./staff-substitution-update.component.ts"></script>
