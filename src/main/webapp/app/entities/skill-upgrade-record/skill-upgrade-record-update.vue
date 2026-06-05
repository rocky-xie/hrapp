<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.skillUpgradeRecord.home.createOrEditLabel" data-cy="SkillUpgradeRecordCreateUpdateHeading">
          {{ $t('entity.skillUpgradeRecord.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="skillUpgradeRecord.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="skillUpgradeRecord.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{ $t('entity.skillUpgradeRecord.field.changeType') }}</label>
            <select
              class="form-control"
              name="changeType"
              :class="{ valid: !v$.changeType.$invalid, invalid: v$.changeType.$invalid }"
              v-model="v$.changeType.$model"
              id="skill-upgrade-record-changeType"
              data-cy="changeType"
              required
            >
              <option v-for="skillChangeType in skillChangeTypeValues" :key="skillChangeType" :value="skillChangeType">
                {{ skillChangeType }}
              </option>
            </select>
            <div v-if="v$.changeType.$anyDirty && v$.changeType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.changeType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{ $t('entity.skillUpgradeRecord.field.changeDate') }}</label>
            <b-form-input
              id="skill-upgrade-record-changeDate"
              data-cy="changeDate"
              type="date"
              class="form-control"
              name="changeDate"
              :class="{ 'is-valid': !v$.changeDate.$invalid, 'is-invalid': v$.changeDate.$invalid }"
              v-model="v$.changeDate.$model"
            />
            <div v-if="v$.changeDate.$anyDirty && v$.changeDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.changeDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{ $t('entity.staffSubstitution.field.reason') }}</label>
            <input
              type="text"
              class="form-control"
              name="reason"
              id="skill-upgrade-record-reason"
              data-cy="reason"
              :class="{ valid: !v$.reason.$invalid, invalid: v$.reason.$invalid }"
              v-model="v$.reason.$model"
              required
            />
            <div v-if="v$.reason.$anyDirty && v$.reason.$invalid">
              <small class="form-text text-danger" v-for="error of v$.reason.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{
              $t('entity.skillUpgradeRecord.field.beforeLevelLabel')
            }}</label>
            <input
              type="text"
              class="form-control"
              name="beforeLevelLabel"
              id="skill-upgrade-record-beforeLevelLabel"
              data-cy="beforeLevelLabel"
              :class="{ valid: !v$.beforeLevelLabel.$invalid, invalid: v$.beforeLevelLabel.$invalid }"
              v-model="v$.beforeLevelLabel.$model"
            />
            <div v-if="v$.beforeLevelLabel.$anyDirty && v$.beforeLevelLabel.$invalid">
              <small class="form-text text-danger" v-for="error of v$.beforeLevelLabel.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{ $t('entity.skillUpgradeRecord.field.afterLevelLabel') }}</label>
            <input
              type="text"
              class="form-control"
              name="afterLevelLabel"
              id="skill-upgrade-record-afterLevelLabel"
              data-cy="afterLevelLabel"
              :class="{ valid: !v$.afterLevelLabel.$invalid, invalid: v$.afterLevelLabel.$invalid }"
              v-model="v$.afterLevelLabel.$model"
            />
            <div v-if="v$.afterLevelLabel.$anyDirty && v$.afterLevelLabel.$invalid">
              <small class="form-text text-danger" v-for="error of v$.afterLevelLabel.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{ $t('entity.candidateProfile.field.evidence') }}</label>
            <textarea
              class="form-control"
              name="evidence"
              id="skill-upgrade-record-evidence"
              data-cy="evidence"
              :class="{ valid: !v$.evidence.$invalid, invalid: v$.evidence.$invalid }"
              v-model="v$.evidence.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{ $t('entity.skillAssessment.field.comment') }}</label>
            <textarea
              class="form-control"
              name="comment"
              id="skill-upgrade-record-comment"
              data-cy="comment"
              :class="{ valid: !v$.comment.$invalid, invalid: v$.comment.$invalid }"
              v-model="v$.comment.$model"
            ></textarea>
          </div>
          <div class="border-top pt-3 mt-3">
            <h3 class="h5">{{ $t('global.entity.section.personAndSkill') }}</h3>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record-person">{{ $t('entity.person.detail.title') }}</label>
            <select
              class="form-control"
              id="skill-upgrade-record-person"
              data-cy="person"
              name="person"
              v-model="skillUpgradeRecord.person"
              @change="onPersonChange"
              required
            >
              <option v-if="!skillUpgradeRecord.person" :value="null" selected></option>
              <option
                :value="
                  skillUpgradeRecord.person && personOption.id === skillUpgradeRecord.person.id ? skillUpgradeRecord.person : personOption
                "
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
            <label class="form-control-label" for="skill-upgrade-record-skill">{{ $t('entity.skill.detail.title') }}</label>
            <select
              class="form-control"
              id="skill-upgrade-record-skill"
              data-cy="skill"
              name="skill"
              v-model="skillUpgradeRecord.skill"
              @change="onSkillChange"
              required
            >
              <option v-if="!skillUpgradeRecord.skill" :value="null" selected></option>
              <option
                :value="skillUpgradeRecord.skill && skillOption.id === skillUpgradeRecord.skill.id ? skillUpgradeRecord.skill : skillOption"
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
          <div class="border-top pt-3 mt-3">
            <h3 class="h5">{{ $t('global.entity.section.upgradeDetails') }}</h3>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{ $t('global.entity.field.oldLevel') }}</label>
            <select
              class="form-control"
              id="skill-upgrade-record-oldLevel"
              data-cy="oldLevel"
              name="oldLevel"
              v-model="skillUpgradeRecord.oldLevel"
            >
              <option :value="null"></option>
              <option
                :value="
                  skillUpgradeRecord.oldLevel && skillLevelOption.id === skillUpgradeRecord.oldLevel.id
                    ? skillUpgradeRecord.oldLevel
                    : skillLevelOption
                "
                v-for="skillLevelOption in skillLevels"
                :key="skillLevelOption.id"
              >
                {{ skillLevelOption.code }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{ $t('global.entity.field.newLevel') }}</label>
            <select
              class="form-control"
              id="skill-upgrade-record-newLevel"
              data-cy="newLevel"
              name="newLevel"
              v-model="skillUpgradeRecord.newLevel"
              required
            >
              <option v-if="!skillUpgradeRecord.newLevel" :value="null" selected></option>
              <option
                :value="
                  skillUpgradeRecord.newLevel && skillLevelOption.id === skillUpgradeRecord.newLevel.id
                    ? skillUpgradeRecord.newLevel
                    : skillLevelOption
                "
                v-for="skillLevelOption in skillLevels"
                :key="skillLevelOption.id"
              >
                {{ skillLevelOption.code }}
              </option>
            </select>
          </div>
          <div v-if="v$.newLevel.$anyDirty && v$.newLevel.$invalid">
            <small class="form-text text-danger" v-for="error of v$.newLevel.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-upgrade-record">{{ $t('global.entity.field.assessor') }}</label>
            <select
              class="form-control"
              id="skill-upgrade-record-assessor"
              data-cy="assessor"
              name="assessor"
              v-model="skillUpgradeRecord.assessor"
            >
              <option :value="null"></option>
              <option
                :value="
                  skillUpgradeRecord.assessor && personOption.id === skillUpgradeRecord.assessor.id
                    ? skillUpgradeRecord.assessor
                    : personOption
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
<script lang="ts" src="./skill-upgrade-record-update.component.ts"></script>
