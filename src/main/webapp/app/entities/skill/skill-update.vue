<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.skill.home.createOrEditLabel" data-cy="SkillCreateUpdateHeading">
          {{ $t('entity.skill.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="skill.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="skill.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill">{{ $t('entity.skill.field.skillCode') }}</label>
            <input
              type="text"
              class="form-control"
              name="skillCode"
              id="skill-skillCode"
              data-cy="skillCode"
              :class="{ valid: !v$.skillCode.$invalid, invalid: v$.skillCode.$invalid }"
              v-model="v$.skillCode.$model"
              required
            />
            <div v-if="v$.skillCode.$anyDirty && v$.skillCode.$invalid">
              <small class="form-text text-danger" v-for="error of v$.skillCode.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill">{{ $t('entity.skill.field.skillName') }}</label>
            <input
              type="text"
              class="form-control"
              name="skillName"
              id="skill-skillName"
              data-cy="skillName"
              :class="{ valid: !v$.skillName.$invalid, invalid: v$.skillName.$invalid }"
              v-model="v$.skillName.$model"
              required
            />
            <div v-if="v$.skillName.$anyDirty && v$.skillName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.skillName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill">{{ $t('entity.skill.field.skillType') }}</label>
            <select
              class="form-control"
              name="skillType"
              :class="{ valid: !v$.skillType.$invalid, invalid: v$.skillType.$invalid }"
              v-model="v$.skillType.$model"
              id="skill-skillType"
              data-cy="skillType"
              required
            >
              <option v-for="skillType in skillTypeValues" :key="skillType" :value="skillType">{{ skillType }}</option>
            </select>
            <div v-if="v$.skillType.$anyDirty && v$.skillType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.skillType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill">{{ $t('entity.skill.field.measurableFlag') }}</label>
            <b-form-checkbox
              v-model="v$.measurableFlag.$model"
              name="measurableFlag"
              id="skill-measurableFlag"
              data-cy="measurableFlag"
              :class="{ 'is-valid': !v$.measurableFlag.$invalid, 'is-invalid': v$.measurableFlag.$invalid }"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill">{{ $t('entity.skill.field.description') }}</label>
            <textarea
              class="form-control"
              name="description"
              id="skill-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill">{{ $t('entity.skill.field.evidenceType') }}</label>
            <select
              class="form-control"
              name="evidenceType"
              :class="{ valid: !v$.evidenceType.$invalid, invalid: v$.evidenceType.$invalid }"
              v-model="v$.evidenceType.$model"
              id="skill-evidenceType"
              data-cy="evidenceType"
            >
              <option v-for="evidenceType in evidenceTypeValues" :key="evidenceType" :value="evidenceType">{{ evidenceType }}</option>
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
<script lang="ts" src="./skill-update.component.ts"></script>
