<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.personSkill.home.createOrEditLabel" data-cy="PersonSkillCreateUpdateHeading">
          {{ $t('entity.personSkill.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="personSkill.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="personSkill.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person-skill-assessmentDate">{{
              $t('entity.skillAssessment.field.assessmentDate')
            }}</label>
            <b-form-input
              id="person-skill-assessmentDate"
              data-cy="assessmentDate"
              type="date"
              class="form-control"
              name="assessmentDate"
              :class="{ valid: !v$.assessmentDate.$invalid, invalid: v$.assessmentDate.$invalid }"
              v-model="v$.assessmentDate.$model"
              required
            />
            <div v-if="v$.assessmentDate.$anyDirty && v$.assessmentDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.assessmentDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person-skill-nextReviewDate">{{ $t('entity.personSkill.field.nextReviewDate') }}</label>
            <b-form-input
              id="person-skill-nextReviewDate"
              data-cy="nextReviewDate"
              type="date"
              class="form-control"
              name="nextReviewDate"
              :class="{ valid: !v$.nextReviewDate.$invalid, invalid: v$.nextReviewDate.$invalid }"
              v-model="v$.nextReviewDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person-skill">{{ $t('entity.candidateProfile.field.evidence') }}</label>
            <textarea
              class="form-control"
              name="evidence"
              id="person-skill-evidence"
              data-cy="evidence"
              :class="{ valid: !v$.evidence.$invalid, invalid: v$.evidence.$invalid }"
              v-model="v$.evidence.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person-skill">{{ $t('entity.personSkill.field.confidence') }}</label>
            <select
              class="form-control"
              name="confidence"
              :class="{ valid: !v$.confidence.$invalid, invalid: v$.confidence.$invalid }"
              v-model="v$.confidence.$model"
              id="person-skill-confidence"
              data-cy="confidence"
            >
              <option v-for="confidenceLevel in confidenceLevelValues" :key="confidenceLevel" :value="confidenceLevel">
                {{ confidenceLevel }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person-skill">{{ $t('entity.personSkill.field.growthDirection') }}</label>
            <textarea
              class="form-control"
              name="growthDirection"
              id="person-skill-growthDirection"
              data-cy="growthDirection"
              :class="{ valid: !v$.growthDirection.$invalid, invalid: v$.growthDirection.$invalid }"
              v-model="v$.growthDirection.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person-skill">{{ $t('entity.person.detail.title') }}</label>
            <select class="form-control" id="person-skill-person" data-cy="person" name="person" v-model="personSkill.person" required>
              <option v-if="!personSkill.person" :value="null" selected></option>
              <option
                :value="personSkill.person && personOption.id === personSkill.person.id ? personSkill.person : personOption"
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
            <label class="form-control-label" for="person-skill">{{ $t('entity.skill.detail.title') }}</label>
            <select class="form-control" id="person-skill-skill" data-cy="skill" name="skill" v-model="personSkill.skill" required>
              <option v-if="!personSkill.skill" :value="null" selected></option>
              <option
                :value="personSkill.skill && skillOption.id === personSkill.skill.id ? personSkill.skill : skillOption"
                v-for="skillOption in skills"
                :key="skillOption.id"
              >
                {{ skillOption.skillName }}
              </option>
            </select>
          </div>
          <div v-if="v$.skill.$anyDirty && v$.skill.$invalid">
            <small class="form-text text-danger" v-for="error of v$.skill.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person-skill">{{ $t('global.entity.field.currentLevel') }}</label>
            <select
              class="form-control"
              id="person-skill-currentLevel"
              data-cy="currentLevel"
              name="currentLevel"
              v-model="personSkill.currentLevel"
              required
            >
              <option v-if="!personSkill.currentLevel" :value="null" selected></option>
              <option
                :value="
                  personSkill.currentLevel && skillLevelOption.id === personSkill.currentLevel.id
                    ? personSkill.currentLevel
                    : skillLevelOption
                "
                v-for="skillLevelOption in skillLevels"
                :key="skillLevelOption.id"
              >
                {{ skillLevelOption.code }}
              </option>
            </select>
          </div>
          <div v-if="v$.currentLevel.$anyDirty && v$.currentLevel.$invalid">
            <small class="form-text text-danger" v-for="error of v$.currentLevel.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person-skill">{{ $t('global.entity.field.previousLevel') }}</label>
            <select
              class="form-control"
              id="person-skill-previousLevel"
              data-cy="previousLevel"
              name="previousLevel"
              v-model="personSkill.previousLevel"
            >
              <option :value="null"></option>
              <option
                :value="
                  personSkill.previousLevel && skillLevelOption.id === personSkill.previousLevel.id
                    ? personSkill.previousLevel
                    : skillLevelOption
                "
                v-for="skillLevelOption in skillLevels"
                :key="skillLevelOption.id"
              >
                {{ skillLevelOption.code }}
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
<script lang="ts" src="./person-skill-update.component.ts"></script>
