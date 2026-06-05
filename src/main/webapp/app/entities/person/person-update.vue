<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.person.home.createOrEditLabel" data-cy="PersonCreateUpdateHeading">
          {{ $t('entity.person.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="person.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="person.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.employeeCode') }}</label>
            <input
              type="text"
              class="form-control"
              name="employeeCode"
              id="person-employeeCode"
              data-cy="employeeCode"
              :class="{ valid: !v$.employeeCode.$invalid, invalid: v$.employeeCode.$invalid }"
              v-model="v$.employeeCode.$model"
            />
            <div v-if="v$.employeeCode.$anyDirty && v$.employeeCode.$invalid">
              <small class="form-text text-danger" v-for="error of v$.employeeCode.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.personName') }}</label>
            <input
              type="text"
              class="form-control"
              name="personName"
              id="person-personName"
              data-cy="personName"
              :class="{ valid: !v$.personName.$invalid, invalid: v$.personName.$invalid }"
              v-model="v$.personName.$model"
              required
            />
            <div v-if="v$.personName.$anyDirty && v$.personName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.personName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.age') }}</label>
            <input
              type="number"
              class="form-control"
              name="age"
              id="person-age"
              data-cy="age"
              :class="{ valid: !v$.age.$invalid, invalid: v$.age.$invalid }"
              v-model.number="v$.age.$model"
            />
            <div v-if="v$.age.$anyDirty && v$.age.$invalid">
              <small class="form-text text-danger" v-for="error of v$.age.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.gender') }}</label>
            <select
              class="form-control"
              name="gender"
              :class="{ valid: !v$.gender.$invalid, invalid: v$.gender.$invalid }"
              v-model="v$.gender.$model"
              id="person-gender"
              data-cy="gender"
            >
              <option v-for="gender in genderValues" :key="gender" :value="gender">{{ gender }}</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.department') }}</label>
            <input
              type="text"
              class="form-control"
              name="department"
              id="person-department"
              data-cy="department"
              :class="{ valid: !v$.department.$invalid, invalid: v$.department.$invalid }"
              v-model="v$.department.$model"
            />
            <div v-if="v$.department.$anyDirty && v$.department.$invalid">
              <small class="form-text text-danger" v-for="error of v$.department.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.currentRole') }}</label>
            <input
              type="text"
              class="form-control"
              name="currentRole"
              id="person-currentRole"
              data-cy="currentRole"
              :class="{ valid: !v$.currentRole.$invalid, invalid: v$.currentRole.$invalid }"
              v-model="v$.currentRole.$model"
            />
            <div v-if="v$.currentRole.$anyDirty && v$.currentRole.$invalid">
              <small class="form-text text-danger" v-for="error of v$.currentRole.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.employmentStatus') }}</label>
            <select
              class="form-control"
              name="employmentStatus"
              :class="{ valid: !v$.employmentStatus.$invalid, invalid: v$.employmentStatus.$invalid }"
              v-model="v$.employmentStatus.$model"
              id="person-employmentStatus"
              data-cy="employmentStatus"
              required
            >
              <option v-for="employmentStatus in employmentStatusValues" :key="employmentStatus" :value="employmentStatus">
                {{ employmentStatus }}
              </option>
            </select>
            <div v-if="v$.employmentStatus.$anyDirty && v$.employmentStatus.$invalid">
              <small class="form-text text-danger" v-for="error of v$.employmentStatus.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.joinDate') }}</label>
            <b-form-input
              id="person-joinDate"
              data-cy="joinDate"
              type="date"
              class="form-control"
              name="joinDate"
              :class="{ 'is-valid': !v$.joinDate.$invalid, 'is-invalid': v$.joinDate.$invalid }"
              v-model="v$.joinDate.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.mentorFlag') }}</label>
            <b-form-checkbox
              v-model="v$.mentorFlag.$model"
              name="mentorFlag"
              id="person-mentorFlag"
              data-cy="mentorFlag"
              :class="{ 'is-valid': !v$.mentorFlag.$invalid, 'is-invalid': v$.mentorFlag.$invalid }"
              required
            />
            <div v-if="v$.mentorFlag.$anyDirty && v$.mentorFlag.$invalid">
              <small class="form-text text-danger" v-for="error of v$.mentorFlag.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.coreCandidateFlag') }}</label>
            <b-form-checkbox
              v-model="v$.coreCandidateFlag.$model"
              name="coreCandidateFlag"
              id="person-coreCandidateFlag"
              data-cy="coreCandidateFlag"
              :class="{ 'is-valid': !v$.coreCandidateFlag.$invalid, 'is-invalid': v$.coreCandidateFlag.$invalid }"
              required
            />
            <div v-if="v$.coreCandidateFlag.$anyDirty && v$.coreCandidateFlag.$invalid">
              <small class="form-text text-danger" v-for="error of v$.coreCandidateFlag.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="person">{{ $t('entity.person.field.note') }}</label>
            <textarea
              class="form-control"
              name="note"
              id="person-note"
              data-cy="note"
              :class="{ valid: !v$.note.$invalid, invalid: v$.note.$invalid }"
              v-model="v$.note.$model"
            ></textarea>
          </div>
          <div class="mb-4 pt-3 border-top">
            <div class="d-flex align-items-center justify-content-between mb-2">
              <h3 class="h5 mb-0">{{ $t('entity.skill.home.title') }}</h3>
              <button type="button" class="btn btn-outline-primary btn-sm" data-cy="addPersonSkill" @click="addPersonSkill()">
                <font-awesome-icon icon="plus"></font-awesome-icon>&nbsp;<span>{{ $t('entity.person.related.addSkill') }}</span>
              </button>
            </div>
            <div class="table-responsive">
              <table class="table table-sm align-middle">
                <thead>
                  <tr>
                    <th>{{ $t('entity.skill.detail.title') }}</th>
                    <th>{{ $t('global.entity.field.currentLevel') }}</th>
                    <th>{{ $t('entity.skillAssessment.field.assessmentDate') }}</th>
                    <th>{{ $t('global.entity.field.nextReview') }}</th>
                    <th>{{ $t('entity.candidateProfile.field.evidence') }}</th>
                    <th>{{ $t('entity.personSkill.field.confidence') }}</th>
                    <th>{{ $t('entity.personSkill.field.growthDirection') }}</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(personSkill, index) in personSkills" :key="personSkill.id ?? index">
                    <td style="min-width: 12rem">
                      <select class="form-control form-control-sm" v-model="personSkill.skill" data-cy="personSkillSkill">
                        <option :value="null"></option>
                        <option
                          v-for="skillOption in skills"
                          :key="skillOption.id"
                          :value="personSkill.skill && skillOption.id === personSkill.skill.id ? personSkill.skill : skillOption"
                        >
                          {{ skillOption.skillName }}
                        </option>
                      </select>
                    </td>
                    <td style="min-width: 10rem">
                      <select class="form-control form-control-sm" v-model="personSkill.currentLevel" data-cy="personSkillCurrentLevel">
                        <option :value="null"></option>
                        <option
                          v-for="skillLevelOption in skillLevels"
                          :key="skillLevelOption.id"
                          :value="
                            personSkill.currentLevel && skillLevelOption.id === personSkill.currentLevel.id
                              ? personSkill.currentLevel
                              : skillLevelOption
                          "
                        >
                          {{ skillLevelOption.code }}
                        </option>
                      </select>
                    </td>
                    <td style="min-width: 10rem">
                      <input
                        type="date"
                        class="form-control form-control-sm"
                        v-model="personSkill.assessmentDate"
                        data-cy="personSkillAssessmentDate"
                      />
                    </td>
                    <td style="min-width: 10rem">
                      <input
                        type="date"
                        class="form-control form-control-sm"
                        v-model="personSkill.nextReviewDate"
                        data-cy="personSkillNextReviewDate"
                      />
                    </td>
                    <td style="min-width: 14rem">
                      <input
                        type="text"
                        class="form-control form-control-sm"
                        v-model="personSkill.evidence"
                        data-cy="personSkillEvidence"
                      />
                    </td>
                    <td style="min-width: 9rem">
                      <select class="form-control form-control-sm" v-model="personSkill.confidence" data-cy="personSkillConfidence">
                        <option :value="null"></option>
                        <option v-for="confidenceLevel in confidenceLevelValues" :key="confidenceLevel" :value="confidenceLevel">
                          {{ confidenceLevel }}
                        </option>
                      </select>
                    </td>
                    <td style="min-width: 14rem">
                      <input
                        type="text"
                        class="form-control form-control-sm"
                        v-model="personSkill.growthDirection"
                        data-cy="personSkillGrowthDirection"
                      />
                    </td>
                    <td class="text-end">
                      <button
                        type="button"
                        class="btn btn-outline-danger btn-sm"
                        data-cy="removePersonSkill"
                        @click="removePersonSkill(index)"
                      >
                        <font-awesome-icon icon="trash"></font-awesome-icon>
                      </button>
                    </td>
                  </tr>
                  <tr v-if="personSkills.length === 0">
                    <td colspan="8" class="text-muted">{{ $t('global.entity.message.noSkills') }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
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
<script lang="ts" src="./person-update.component.ts"></script>
