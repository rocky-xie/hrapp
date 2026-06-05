<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.positionSkillRequirement.home.createOrEditLabel" data-cy="PositionSkillRequirementCreateUpdateHeading">
          {{ $t('entity.positionSkillRequirement.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="positionSkillRequirement.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="positionSkillRequirement.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-skill-requirement">{{
              $t('entity.positionSkillRequirement.field.importance')
            }}</label>
            <select
              class="form-control"
              name="importance"
              :class="{ valid: !v$.importance.$invalid, invalid: v$.importance.$invalid }"
              v-model="v$.importance.$model"
              id="position-skill-requirement-importance"
              data-cy="importance"
              required
            >
              <option
                v-for="requirementImportance in requirementImportanceValues"
                :key="requirementImportance"
                :value="requirementImportance"
              >
                {{ requirementImportance }}
              </option>
            </select>
            <div v-if="v$.importance.$anyDirty && v$.importance.$invalid">
              <small class="form-text text-danger" v-for="error of v$.importance.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-skill-requirement">{{ $t('entity.positionMatch.field.remark') }}</label>
            <textarea
              class="form-control"
              name="remark"
              id="position-skill-requirement-remark"
              data-cy="remark"
              :class="{ valid: !v$.remark.$invalid, invalid: v$.remark.$invalid }"
              v-model="v$.remark.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-skill-requirement">{{ $t('entity.position.detail.title') }}</label>
            <select
              class="form-control"
              id="position-skill-requirement-position"
              data-cy="position"
              name="position"
              v-model="positionSkillRequirement.position"
              required
            >
              <option v-if="!positionSkillRequirement.position" :value="null" selected></option>
              <option
                :value="
                  positionSkillRequirement.position && positionOption.id === positionSkillRequirement.position.id
                    ? positionSkillRequirement.position
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
            <label class="form-control-label" for="position-skill-requirement">{{ $t('entity.skill.detail.title') }}</label>
            <select
              class="form-control"
              id="position-skill-requirement-skill"
              data-cy="skill"
              name="skill"
              v-model="positionSkillRequirement.skill"
              required
            >
              <option v-if="!positionSkillRequirement.skill" :value="null" selected></option>
              <option
                :value="
                  positionSkillRequirement.skill && skillOption.id === positionSkillRequirement.skill.id
                    ? positionSkillRequirement.skill
                    : skillOption
                "
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
            <label class="form-control-label" for="position-skill-requirement">{{ $t('global.entity.field.requiredLevel') }}</label>
            <select
              class="form-control"
              id="position-skill-requirement-requiredLevel"
              data-cy="requiredLevel"
              name="requiredLevel"
              v-model="positionSkillRequirement.requiredLevel"
              required
            >
              <option v-if="!positionSkillRequirement.requiredLevel" :value="null" selected></option>
              <option
                :value="
                  positionSkillRequirement.requiredLevel && skillLevelOption.id === positionSkillRequirement.requiredLevel.id
                    ? positionSkillRequirement.requiredLevel
                    : skillLevelOption
                "
                v-for="skillLevelOption in skillLevels"
                :key="skillLevelOption.id"
              >
                {{ skillLevelOption.code }}
              </option>
            </select>
          </div>
          <div v-if="v$.requiredLevel.$anyDirty && v$.requiredLevel.$invalid">
            <small class="form-text text-danger" v-for="error of v$.requiredLevel.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-skill-requirement">{{ $t('global.entity.field.preferredLevel') }}</label>
            <select
              class="form-control"
              id="position-skill-requirement-preferredLevel"
              data-cy="preferredLevel"
              name="preferredLevel"
              v-model="positionSkillRequirement.preferredLevel"
            >
              <option :value="null"></option>
              <option
                :value="
                  positionSkillRequirement.preferredLevel && skillLevelOption.id === positionSkillRequirement.preferredLevel.id
                    ? positionSkillRequirement.preferredLevel
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
<script lang="ts" src="./position-skill-requirement-update.component.ts"></script>
