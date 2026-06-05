<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.positionMatch.home.createOrEditLabel" data-cy="PositionMatchCreateUpdateHeading">
          {{ $t('entity.positionMatch.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="positionMatch.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="positionMatch.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-match">{{ $t('entity.positionMatch.field.matchScore') }}</label>
            <input
              type="number"
              class="form-control"
              name="matchScore"
              id="position-match-matchScore"
              data-cy="matchScore"
              :class="{ valid: !v$.matchScore.$invalid, invalid: v$.matchScore.$invalid }"
              v-model.number="v$.matchScore.$model"
            />
            <div v-if="v$.matchScore.$anyDirty && v$.matchScore.$invalid">
              <small class="form-text text-danger" v-for="error of v$.matchScore.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-match">{{ $t('entity.positionMatch.field.matchedSkills') }}</label>
            <textarea
              class="form-control"
              name="matchedSkills"
              id="position-match-matchedSkills"
              data-cy="matchedSkills"
              :class="{ valid: !v$.matchedSkills.$invalid, invalid: v$.matchedSkills.$invalid }"
              v-model="v$.matchedSkills.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-match">{{ $t('entity.positionMatch.field.gapSkills') }}</label>
            <textarea
              class="form-control"
              name="gapSkills"
              id="position-match-gapSkills"
              data-cy="gapSkills"
              :class="{ valid: !v$.gapSkills.$invalid, invalid: v$.gapSkills.$invalid }"
              v-model="v$.gapSkills.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-match">{{ $t('entity.positionMatch.field.readiness') }}</label>
            <select
              class="form-control"
              name="readiness"
              :class="{ valid: !v$.readiness.$invalid, invalid: v$.readiness.$invalid }"
              v-model="v$.readiness.$model"
              id="position-match-readiness"
              data-cy="readiness"
              required
            >
              <option v-for="readinessLevel in readinessLevelValues" :key="readinessLevel" :value="readinessLevel">
                {{ readinessLevel }}
              </option>
            </select>
            <div v-if="v$.readiness.$anyDirty && v$.readiness.$invalid">
              <small class="form-text text-danger" v-for="error of v$.readiness.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-match">{{ $t('entity.positionMatch.field.recommendation') }}</label>
            <select
              class="form-control"
              name="recommendation"
              :class="{ valid: !v$.recommendation.$invalid, invalid: v$.recommendation.$invalid }"
              v-model="v$.recommendation.$model"
              id="position-match-recommendation"
              data-cy="recommendation"
              required
            >
              <option v-for="recommendation in recommendationValues" :key="recommendation" :value="recommendation">
                {{ recommendation }}
              </option>
            </select>
            <div v-if="v$.recommendation.$anyDirty && v$.recommendation.$invalid">
              <small class="form-text text-danger" v-for="error of v$.recommendation.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-match">{{ $t('entity.positionMatch.field.analysisDate') }}</label>
            <b-form-input
              id="position-match-analysisDate"
              data-cy="analysisDate"
              type="date"
              class="form-control"
              name="analysisDate"
              :class="{ 'is-valid': !v$.analysisDate.$invalid, 'is-invalid': v$.analysisDate.$invalid }"
              v-model="v$.analysisDate.$model"
            />
            <div v-if="v$.analysisDate.$anyDirty && v$.analysisDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.analysisDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-match">{{ $t('entity.positionMatch.field.remark') }}</label>
            <textarea
              class="form-control"
              name="remark"
              id="position-match-remark"
              data-cy="remark"
              :class="{ valid: !v$.remark.$invalid, invalid: v$.remark.$invalid }"
              v-model="v$.remark.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-match">{{ $t('entity.person.detail.title') }}</label>
            <select class="form-control" id="position-match-person" data-cy="person" name="person" v-model="positionMatch.person" required>
              <option v-if="!positionMatch.person" :value="null" selected></option>
              <option
                :value="positionMatch.person && personOption.id === positionMatch.person.id ? positionMatch.person : personOption"
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
            <label class="form-control-label" for="position-match">{{ $t('entity.position.detail.title') }}</label>
            <select
              class="form-control"
              id="position-match-position"
              data-cy="position"
              name="position"
              v-model="positionMatch.position"
              required
            >
              <option v-if="!positionMatch.position" :value="null" selected></option>
              <option
                :value="positionMatch.position && positionOption.id === positionMatch.position.id ? positionMatch.position : positionOption"
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
<script lang="ts" src="./position-match-update.component.ts"></script>
