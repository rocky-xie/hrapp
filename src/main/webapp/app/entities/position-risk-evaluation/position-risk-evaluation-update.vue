<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.positionRiskEvaluation.home.createOrEditLabel" data-cy="PositionRiskEvaluationCreateUpdateHeading">
          {{ $t('entity.positionRiskEvaluation.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="positionRiskEvaluation.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="positionRiskEvaluation.id" readonly />
          </div>
          <fieldset class="border rounded p-3 mb-4">
            <legend class="h5 w-auto px-2 mb-0">{{ $t('entity.positionRiskEvaluation.section.evaluationResult') }}</legend>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{ $t('entity.evaluation.field.evaluationDate') }}</label>
              <b-form-input
                id="position-risk-evaluation-evaluationDate"
                data-cy="evaluationDate"
                type="date"
                class="form-control"
                name="evaluationDate"
                :class="{ 'is-valid': !v$.evaluationDate.$invalid, 'is-invalid': v$.evaluationDate.$invalid }"
                v-model="v$.evaluationDate.$model"
                readonly
              />
              <div v-if="v$.evaluationDate.$anyDirty && v$.evaluationDate.$invalid">
                <small class="form-text text-danger" v-for="error of v$.evaluationDate.$errors" :key="error.$uid">{{
                  error.$message
                }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{
                $t('entity.positionRiskEvaluation.field.ownerCount')
              }}</label>
              <input
                type="number"
                class="form-control"
                name="ownerCount"
                id="position-risk-evaluation-ownerCount"
                data-cy="ownerCount"
                :class="{ valid: !v$.ownerCount.$invalid, invalid: v$.ownerCount.$invalid }"
                v-model.number="v$.ownerCount.$model"
                readonly
              />
              <div v-if="v$.ownerCount.$anyDirty && v$.ownerCount.$invalid">
                <small class="form-text text-danger" v-for="error of v$.ownerCount.$errors" :key="error.$uid">{{ error.$message }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{
                $t('entity.positionRiskEvaluation.field.substitutableOwnerCount')
              }}</label>
              <input
                type="number"
                class="form-control"
                name="substitutableOwnerCount"
                id="position-risk-evaluation-substitutableOwnerCount"
                data-cy="substitutableOwnerCount"
                :class="{ valid: !v$.substitutableOwnerCount.$invalid, invalid: v$.substitutableOwnerCount.$invalid }"
                v-model.number="v$.substitutableOwnerCount.$model"
                readonly
              />
              <div v-if="v$.substitutableOwnerCount.$anyDirty && v$.substitutableOwnerCount.$invalid">
                <small class="form-text text-danger" v-for="error of v$.substitutableOwnerCount.$errors" :key="error.$uid">{{
                  error.$message
                }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{
                $t('entity.positionRiskEvaluation.field.hasSubstitute')
              }}</label>
              <b-form-checkbox
                v-model="v$.hasSubstitute.$model"
                name="hasSubstitute"
                id="position-risk-evaluation-hasSubstitute"
                data-cy="hasSubstitute"
                disabled
              />
              <div v-if="v$.hasSubstitute.$anyDirty && v$.hasSubstitute.$invalid">
                <small class="form-text text-danger" v-for="error of v$.hasSubstitute.$errors" :key="error.$uid">{{
                  error.$message
                }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{
                $t('entity.positionRiskEvaluation.field.riskLevel')
              }}</label>
              <input
                type="text"
                class="form-control"
                name="riskLevel"
                id="position-risk-evaluation-riskLevel"
                data-cy="riskLevel"
                :class="{ valid: !v$.riskLevel.$invalid, invalid: v$.riskLevel.$invalid }"
                v-model="v$.riskLevel.$model"
                readonly
              />
              <div v-if="v$.riskLevel.$anyDirty && v$.riskLevel.$invalid">
                <small class="form-text text-danger" v-for="error of v$.riskLevel.$errors" :key="error.$uid">{{ error.$message }}</small>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{
                $t('entity.positionRiskEvaluation.field.riskReason')
              }}</label>
              <pre
                class="form-control bg-light p-3"
                name="riskReason"
                id="position-risk-evaluation-riskReason"
                data-cy="riskReason"
                v-text="positionRiskEvaluation.riskReason"
              ></pre>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{
                $t('entity.positionRiskEvaluation.field.recommendedAction')
              }}</label>
              <textarea
                class="form-control"
                name="recommendedAction"
                id="position-risk-evaluation-recommendedAction"
                data-cy="recommendedAction"
                :class="{ valid: !v$.recommendedAction.$invalid, invalid: v$.recommendedAction.$invalid }"
                v-model="v$.recommendedAction.$model"
                readonly
              ></textarea>
            </div>
          </fieldset>
          <fieldset class="border rounded p-3 mb-4">
            <legend class="h5 w-auto px-2 mb-0">{{ $t('global.entity.section.inputFields') }}</legend>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{ $t('entity.position.detail.title') }}</label>
              <select
                class="form-control"
                id="position-risk-evaluation-position"
                data-cy="position"
                name="position"
                v-model="positionRiskEvaluation.position"
                @change="onPositionChange"
                required
              >
                <option v-if="!positionRiskEvaluation.position" :value="null" selected></option>
                <option
                  :value="
                    positionRiskEvaluation.position && positionOption.id === positionRiskEvaluation.position.id
                      ? positionRiskEvaluation.position
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
              <label class="form-control-label" for="position-risk-evaluation">{{ $t('entity.positionRisk.field.documentStatus') }}</label>
              <select
                class="form-control"
                name="documentStatus"
                :class="{ valid: !v$.documentStatus.$invalid, invalid: v$.documentStatus.$invalid }"
                v-model="v$.documentStatus.$model"
                @change="onEvaluationInputChange"
                id="position-risk-evaluation-documentStatus"
                data-cy="documentStatus"
              >
                <option v-for="documentStatus in documentStatusValues" :key="documentStatus" :value="documentStatus">
                  {{ documentStatus }}
                </option>
              </select>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{
                $t('entity.positionRisk.field.customerOrSystemDependency')
              }}</label>
              <select
                class="form-control"
                name="customerOrSystemDependency"
                :class="{ valid: !v$.customerOrSystemDependency.$invalid, invalid: v$.customerOrSystemDependency.$invalid }"
                v-model="v$.customerOrSystemDependency.$model"
                @change="onEvaluationInputChange"
                id="position-risk-evaluation-customerOrSystemDependency"
                data-cy="customerOrSystemDependency"
              >
                <option v-for="importanceLevel in importanceLevelValues" :key="importanceLevel" :value="importanceLevel">
                  {{ importanceLevel }}
                </option>
              </select>
            </div>
            <div class="mb-3">
              <label class="form-control-label" for="position-risk-evaluation">{{
                $t('entity.successionCandidate.field.successionReadiness')
              }}</label>
              <select
                class="form-control"
                name="successionReadiness"
                :class="{ valid: !v$.successionReadiness.$invalid, invalid: v$.successionReadiness.$invalid }"
                v-model="v$.successionReadiness.$model"
                @change="onEvaluationInputChange"
                id="position-risk-evaluation-successionReadiness"
                data-cy="successionReadiness"
              >
                <option v-for="readinessLevel in readinessLevelValues" :key="readinessLevel" :value="readinessLevel">
                  {{ readinessLevel }}
                </option>
              </select>
            </div>
          </fieldset>
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
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{
              positionRiskEvaluation.id ? 'Save' : 'Evaluate and Save'
            }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./position-risk-evaluation-update.component.ts"></script>
