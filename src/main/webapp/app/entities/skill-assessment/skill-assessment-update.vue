<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.skillAssessment.home.createOrEditLabel" data-cy="SkillAssessmentCreateUpdateHeading">
          {{ $t('entity.skillAssessment.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="skillAssessment.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="skillAssessment.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-assessment">{{ $t('entity.skillAssessment.field.assessmentDate') }}</label>
            <b-form-input
              id="skill-assessment-assessmentDate"
              data-cy="assessmentDate"
              type="date"
              class="form-control"
              name="assessmentDate"
              :class="{ 'is-valid': !v$.assessmentDate.$invalid, 'is-invalid': v$.assessmentDate.$invalid }"
              v-model="v$.assessmentDate.$model"
            />
            <div v-if="v$.assessmentDate.$anyDirty && v$.assessmentDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.assessmentDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-assessment">{{ $t('entity.evaluation.field.result') }}</label>
            <select
              class="form-control"
              name="result"
              :class="{ valid: !v$.result.$invalid, invalid: v$.result.$invalid }"
              v-model="v$.result.$model"
              id="skill-assessment-result"
              data-cy="result"
              required
            >
              <option v-for="assessmentResult in assessmentResultValues" :key="assessmentResult" :value="assessmentResult">
                {{ assessmentResult }}
              </option>
            </select>
            <div v-if="v$.result.$anyDirty && v$.result.$invalid">
              <small class="form-text text-danger" v-for="error of v$.result.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-assessment">{{ $t('entity.candidateProfile.field.evidence') }}</label>
            <textarea
              class="form-control"
              name="evidence"
              id="skill-assessment-evidence"
              data-cy="evidence"
              :class="{ valid: !v$.evidence.$invalid, invalid: v$.evidence.$invalid }"
              v-model="v$.evidence.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-assessment">{{ $t('entity.skillAssessment.field.comment') }}</label>
            <textarea
              class="form-control"
              name="comment"
              id="skill-assessment-comment"
              data-cy="comment"
              :class="{ valid: !v$.comment.$invalid, invalid: v$.comment.$invalid }"
              v-model="v$.comment.$model"
            ></textarea>
          </div>
          <div class="border-top pt-3 mt-3">
            <h3 class="h5">{{ $t('global.entity.section.personAndSkill') }}</h3>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-assessment-person">{{ $t('entity.person.detail.title') }}</label>
            <select
              class="form-control"
              id="skill-assessment-person"
              data-cy="person"
              name="person"
              v-model="skillAssessment.person"
              @change="onPersonChange"
              required
            >
              <option v-if="!skillAssessment.person" :value="null" selected></option>
              <option
                :value="skillAssessment.person && personOption.id === skillAssessment.person.id ? skillAssessment.person : personOption"
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
            <label class="form-control-label" for="skill-assessment-skill">{{ $t('entity.skill.detail.title') }}</label>
            <select class="form-control" id="skill-assessment-skill" data-cy="skill" name="skill" v-model="skillAssessment.skill" required>
              <option v-if="!skillAssessment.skill" :value="null" selected></option>
              <option
                :value="skillAssessment.skill && skillOption.id === skillAssessment.skill.id ? skillAssessment.skill : skillOption"
                v-for="skillOption in filteredSkills"
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
            <label class="form-control-label" for="skill-assessment">{{ $t('global.entity.field.assessor') }}</label>
            <select
              class="form-control"
              id="skill-assessment-assessor"
              data-cy="assessor"
              name="assessor"
              v-model="skillAssessment.assessor"
            >
              <option :value="null"></option>
              <option
                :value="
                  skillAssessment.assessor && personOption.id === skillAssessment.assessor.id ? skillAssessment.assessor : personOption
                "
                v-for="personOption in people"
                :key="personOption.id"
              >
                {{ personOption.personName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-assessment">{{ $t('global.entity.field.newLevel') }}</label>
            <select
              class="form-control"
              id="skill-assessment-newLevel"
              data-cy="newLevel"
              name="newLevel"
              v-model="skillAssessment.newLevel"
            >
              <option :value="null"></option>
              <option
                :value="
                  skillAssessment.newLevel && skillLevelOption.id === skillAssessment.newLevel.id
                    ? skillAssessment.newLevel
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
<script lang="ts" src="./skill-assessment-update.component.ts"></script>
