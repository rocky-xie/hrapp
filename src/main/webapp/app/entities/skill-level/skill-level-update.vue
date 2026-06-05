<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.skillLevel.home.createOrEditLabel" data-cy="SkillLevelCreateUpdateHeading">
          {{ $t('entity.skillLevel.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="skillLevel.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="skillLevel.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-level">{{ $t('entity.skillLevel.field.code') }}</label>
            <select
              class="form-control"
              name="code"
              :class="{ valid: !v$.code.$invalid, invalid: v$.code.$invalid }"
              v-model="v$.code.$model"
              id="skill-level-code"
              data-cy="code"
              required
            >
              <option v-for="levelCode in levelCodeValues" :key="levelCode" :value="levelCode">{{ levelCode }}</option>
            </select>
            <div v-if="v$.code.$anyDirty && v$.code.$invalid">
              <small class="form-text text-danger" v-for="error of v$.code.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-level">{{ $t('entity.skillLevel.field.levelName') }}</label>
            <input
              type="text"
              class="form-control"
              name="levelName"
              id="skill-level-levelName"
              data-cy="levelName"
              :class="{ valid: !v$.levelName.$invalid, invalid: v$.levelName.$invalid }"
              v-model="v$.levelName.$model"
              required
            />
            <div v-if="v$.levelName.$anyDirty && v$.levelName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.levelName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-level">{{ $t('entity.skillLevel.field.definition') }}</label>
            <textarea
              class="form-control"
              name="definition"
              id="skill-level-definition"
              data-cy="definition"
              :class="{ valid: !v$.definition.$invalid, invalid: v$.definition.$invalid }"
              v-model="v$.definition.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-level">{{ $t('entity.skillLevel.field.observableEvidence') }}</label>
            <textarea
              class="form-control"
              name="observableEvidence"
              id="skill-level-observableEvidence"
              data-cy="observableEvidence"
              :class="{ valid: !v$.observableEvidence.$invalid, invalid: v$.observableEvidence.$invalid }"
              v-model="v$.observableEvidence.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="skill-level">{{ $t('entity.skillLevel.field.sortOrder') }}</label>
            <input
              type="number"
              class="form-control"
              name="sortOrder"
              id="skill-level-sortOrder"
              data-cy="sortOrder"
              :class="{ valid: !v$.sortOrder.$invalid, invalid: v$.sortOrder.$invalid }"
              v-model.number="v$.sortOrder.$model"
              required
            />
            <div v-if="v$.sortOrder.$anyDirty && v$.sortOrder.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sortOrder.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./skill-level-update.component.ts"></script>
