<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.positionRisk.home.createOrEditLabel" data-cy="PositionRiskCreateUpdateHeading">
          {{ $t('entity.positionRisk.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="positionRisk.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="positionRisk.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk">{{ $t('entity.personRisk.field.riskType') }}</label>
            <select
              class="form-control"
              name="riskType"
              :class="{ valid: !v$.riskType.$invalid, invalid: v$.riskType.$invalid }"
              v-model="v$.riskType.$model"
              id="position-risk-riskType"
              data-cy="riskType"
              required
            >
              <option v-for="riskType in riskTypeValues" :key="riskType" :value="riskType">{{ riskType }}</option>
            </select>
            <div v-if="v$.riskType.$anyDirty && v$.riskType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.riskType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk">{{ $t('entity.positionRiskEvaluation.field.riskLevel') }}</label>
            <select
              class="form-control"
              name="riskLevel"
              :class="{ valid: !v$.riskLevel.$invalid, invalid: v$.riskLevel.$invalid }"
              v-model="v$.riskLevel.$model"
              id="position-risk-riskLevel"
              data-cy="riskLevel"
              required
            >
              <option v-for="riskLevel in riskLevelValues" :key="riskLevel" :value="riskLevel">{{ riskLevel }}</option>
            </select>
            <div v-if="v$.riskLevel.$anyDirty && v$.riskLevel.$invalid">
              <small class="form-text text-danger" v-for="error of v$.riskLevel.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk">{{ $t('entity.positionRisk.field.documentStatus') }}</label>
            <select
              class="form-control"
              name="documentStatus"
              :class="{ valid: !v$.documentStatus.$invalid, invalid: v$.documentStatus.$invalid }"
              v-model="v$.documentStatus.$model"
              id="position-risk-documentStatus"
              data-cy="documentStatus"
            >
              <option v-for="documentStatus in documentStatusValues" :key="documentStatus" :value="documentStatus">
                {{ documentStatus }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk">{{ $t('entity.positionRisk.field.backupStatus') }}</label>
            <select
              class="form-control"
              name="backupStatus"
              :class="{ valid: !v$.backupStatus.$invalid, invalid: v$.backupStatus.$invalid }"
              v-model="v$.backupStatus.$model"
              id="position-risk-backupStatus"
              data-cy="backupStatus"
            >
              <option v-for="backupStatus in backupStatusValues" :key="backupStatus" :value="backupStatus">{{ backupStatus }}</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk">{{ $t('entity.positionRisk.field.customerOrSystemDependency') }}</label>
            <select
              class="form-control"
              name="customerOrSystemDependency"
              :class="{ valid: !v$.customerOrSystemDependency.$invalid, invalid: v$.customerOrSystemDependency.$invalid }"
              v-model="v$.customerOrSystemDependency.$model"
              id="position-risk-customerOrSystemDependency"
              data-cy="customerOrSystemDependency"
            >
              <option v-for="importanceLevel in importanceLevelValues" :key="importanceLevel" :value="importanceLevel">
                {{ importanceLevel }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk">{{ $t('entity.personRisk.field.riskDescription') }}</label>
            <textarea
              class="form-control"
              name="riskDescription"
              id="position-risk-riskDescription"
              data-cy="riskDescription"
              :class="{ valid: !v$.riskDescription.$invalid, invalid: v$.riskDescription.$invalid }"
              v-model="v$.riskDescription.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk">{{ $t('entity.improvementPlan.field.improvementAction') }}</label>
            <textarea
              class="form-control"
              name="improvementAction"
              id="position-risk-improvementAction"
              data-cy="improvementAction"
              :class="{ valid: !v$.improvementAction.$invalid, invalid: v$.improvementAction.$invalid }"
              v-model="v$.improvementAction.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk-identifiedDate">{{ $t('entity.personRisk.field.identifiedDate') }}</label>
            <b-form-input
              id="position-risk-identifiedDate"
              data-cy="identifiedDate"
              type="date"
              class="form-control"
              name="identifiedDate"
              :class="{ 'is-valid': !v$.identifiedDate.$invalid, 'is-invalid': v$.identifiedDate.$invalid }"
              v-model="v$.identifiedDate.$model"
            />
            <div v-if="v$.identifiedDate.$anyDirty && v$.identifiedDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.identifiedDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk-targetDate">{{ $t('entity.improvementPlan.field.targetDate') }}</label>
            <b-form-input
              id="position-risk-targetDate"
              data-cy="targetDate"
              type="date"
              class="form-control"
              name="targetDate"
              :class="{ 'is-valid': !v$.targetDate.$invalid, 'is-invalid': v$.targetDate.$invalid }"
              v-model="v$.targetDate.$model"
            />
            <div v-if="v$.targetDate.$anyDirty && v$.targetDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.targetDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk-closedDate">{{ $t('entity.personRisk.field.closedDate') }}</label>
            <b-form-input
              id="position-risk-closedDate"
              data-cy="closedDate"
              type="date"
              class="form-control"
              name="closedDate"
              :class="{ 'is-valid': !v$.closedDate.$invalid, 'is-invalid': v$.closedDate.$invalid }"
              v-model="v$.closedDate.$model"
            />
            <div v-if="v$.closedDate.$anyDirty && v$.closedDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.closedDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="position-risk">{{ $t('entity.position.detail.title') }}</label>
            <select
              class="form-control"
              id="position-risk-position"
              data-cy="position"
              name="position"
              v-model="positionRisk.position"
              required
            >
              <option v-if="!positionRisk.position" :value="null" selected></option>
              <option
                :value="positionRisk.position && positionOption.id === positionRisk.position.id ? positionRisk.position : positionOption"
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
            <label class="form-control-label" for="position-risk">{{ $t('global.entity.field.category') }}</label>
            <select class="form-control" id="position-risk-category" data-cy="category" name="category" v-model="positionRisk.category">
              <option :value="null"></option>
              <option
                :value="
                  positionRisk.category && keyResponsibilityCategoryOption.id === positionRisk.category.id
                    ? positionRisk.category
                    : keyResponsibilityCategoryOption
                "
                v-for="keyResponsibilityCategoryOption in keyResponsibilityCategories"
                :key="keyResponsibilityCategoryOption.id"
              >
                {{ keyResponsibilityCategoryOption.categoryName }}
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
<script lang="ts" src="./position-risk-update.component.ts"></script>
