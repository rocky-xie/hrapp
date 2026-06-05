<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.candidateProfile.home.createOrEditLabel" data-cy="CandidateProfileCreateUpdateHeading">
          {{ $t('entity.candidateProfile.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="candidateProfile.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="candidateProfile.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.candidateProfile.field.candidateDate') }}</label>
            <b-form-input
              id="candidate-profile-candidateDate"
              data-cy="candidateDate"
              type="date"
              class="form-control"
              name="candidateDate"
              :class="{ 'is-valid': !v$.candidateDate.$invalid, 'is-invalid': v$.candidateDate.$invalid }"
              v-model="v$.candidateDate.$model"
            />
            <div v-if="v$.candidateDate.$anyDirty && v$.candidateDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.candidateDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.candidateProfile.field.cultivateDirection') }}</label>
            <input
              type="text"
              class="form-control"
              name="cultivateDirection"
              id="candidate-profile-cultivateDirection"
              data-cy="cultivateDirection"
              :class="{ valid: !v$.cultivateDirection.$invalid, invalid: v$.cultivateDirection.$invalid }"
              v-model="v$.cultivateDirection.$model"
            />
            <div v-if="v$.cultivateDirection.$anyDirty && v$.cultivateDirection.$invalid">
              <small class="form-text text-danger" v-for="error of v$.cultivateDirection.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.candidateProfile.field.stability') }}</label>
            <select
              class="form-control"
              name="stability"
              :class="{ valid: !v$.stability.$invalid, invalid: v$.stability.$invalid }"
              v-model="v$.stability.$model"
              id="candidate-profile-stability"
              data-cy="stability"
            >
              <option v-for="importanceLevel in importanceLevelValues" :key="importanceLevel" :value="importanceLevel">
                {{ importanceLevel }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.candidateProfile.field.learningAbility') }}</label>
            <select
              class="form-control"
              name="learningAbility"
              :class="{ valid: !v$.learningAbility.$invalid, invalid: v$.learningAbility.$invalid }"
              v-model="v$.learningAbility.$model"
              id="candidate-profile-learningAbility"
              data-cy="learningAbility"
            >
              <option v-for="importanceLevel in importanceLevelValues" :key="importanceLevel" :value="importanceLevel">
                {{ importanceLevel }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">Communication Coordination</label>
            <select
              class="form-control"
              name="communicationCoordination"
              :class="{ valid: !v$.communicationCoordination.$invalid, invalid: v$.communicationCoordination.$invalid }"
              v-model="v$.communicationCoordination.$model"
              id="candidate-profile-communicationCoordination"
              data-cy="communicationCoordination"
            >
              <option v-for="importanceLevel in importanceLevelValues" :key="importanceLevel" :value="importanceLevel">
                {{ importanceLevel }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{
              $t('entity.candidateProfile.field.businessUnderstanding')
            }}</label>
            <select
              class="form-control"
              name="businessUnderstanding"
              :class="{ valid: !v$.businessUnderstanding.$invalid, invalid: v$.businessUnderstanding.$invalid }"
              v-model="v$.businessUnderstanding.$model"
              id="candidate-profile-businessUnderstanding"
              data-cy="businessUnderstanding"
            >
              <option v-for="importanceLevel in importanceLevelValues" :key="importanceLevel" :value="importanceLevel">
                {{ importanceLevel }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.candidateProfile.field.responsibility') }}</label>
            <select
              class="form-control"
              name="responsibility"
              :class="{ valid: !v$.responsibility.$invalid, invalid: v$.responsibility.$invalid }"
              v-model="v$.responsibility.$model"
              id="candidate-profile-responsibility"
              data-cy="responsibility"
            >
              <option v-for="importanceLevel in importanceLevelValues" :key="importanceLevel" :value="importanceLevel">
                {{ importanceLevel }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.candidateProfile.field.riskAwareness') }}</label>
            <select
              class="form-control"
              name="riskAwareness"
              :class="{ valid: !v$.riskAwareness.$invalid, invalid: v$.riskAwareness.$invalid }"
              v-model="v$.riskAwareness.$model"
              id="candidate-profile-riskAwareness"
              data-cy="riskAwareness"
            >
              <option v-for="importanceLevel in importanceLevelValues" :key="importanceLevel" :value="importanceLevel">
                {{ importanceLevel }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.candidateProfile.field.judgement') }}</label>
            <select
              class="form-control"
              name="judgement"
              :class="{ valid: !v$.judgement.$invalid, invalid: v$.judgement.$invalid }"
              v-model="v$.judgement.$model"
              id="candidate-profile-judgement"
              data-cy="judgement"
              required
            >
              <option v-for="candidateJudgement in candidateJudgementValues" :key="candidateJudgement" :value="candidateJudgement">
                {{ candidateJudgement }}
              </option>
            </select>
            <div v-if="v$.judgement.$anyDirty && v$.judgement.$invalid">
              <small class="form-text text-danger" v-for="error of v$.judgement.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.candidateProfile.field.evidence') }}</label>
            <textarea
              class="form-control"
              name="evidence"
              id="candidate-profile-evidence"
              data-cy="evidence"
              :class="{ valid: !v$.evidence.$invalid, invalid: v$.evidence.$invalid }"
              v-model="v$.evidence.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.person.detail.title') }}</label>
            <select
              class="form-control"
              id="candidate-profile-person"
              data-cy="person"
              name="person"
              v-model="candidateProfile.person"
              required
            >
              <option v-if="!candidateProfile.person" :value="null" selected></option>
              <option
                :value="candidateProfile.person && personOption.id === candidateProfile.person.id ? candidateProfile.person : personOption"
                v-for="personOption in people"
                :key="personOption.id"
              >
                {{ personOption.personName }}
              </option>
            </select>
          </div>
          <div v-if="v$.person.$anyDirty && v$.person.$invalid">
            <small class="form-text text-danger" v-for="error of v$.person.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('entity.position.detail.title') }}</label>
            <select
              class="form-control"
              id="candidate-profile-position"
              data-cy="position"
              name="position"
              v-model="candidateProfile.position"
            >
              <option :value="null"></option>
              <option
                :value="
                  candidateProfile.position && positionOption.id === candidateProfile.position.id
                    ? candidateProfile.position
                    : positionOption
                "
                v-for="positionOption in positions"
                :key="positionOption.id"
              >
                {{ positionOption.positionName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="candidate-profile">{{ $t('global.entity.field.observer') }}</label>
            <select
              class="form-control"
              id="candidate-profile-observer"
              data-cy="observer"
              name="observer"
              v-model="candidateProfile.observer"
            >
              <option :value="null"></option>
              <option
                :value="
                  candidateProfile.observer && personOption.id === candidateProfile.observer.id ? candidateProfile.observer : personOption
                "
                v-for="personOption in people"
                :key="personOption.id"
              >
                {{ personOption.personName }}
              </option>
            </select>
          </div>
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
<script lang="ts" src="./candidate-profile-update.component.ts"></script>
