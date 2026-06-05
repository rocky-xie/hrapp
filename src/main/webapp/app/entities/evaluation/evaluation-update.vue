<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.evaluation.home.createOrEditLabel" data-cy="EvaluationCreateUpdateHeading">
          {{ $t('entity.evaluation.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="evaluation.id">
            <label for="id">{{ $t('global.entity.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="evaluation.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.evaluationName') }}</label>
            <input
              type="text"
              class="form-control"
              name="evaluationName"
              id="evaluation-evaluationName"
              data-cy="evaluationName"
              :class="{ valid: !v$.evaluationName.$invalid, invalid: v$.evaluationName.$invalid }"
              v-model="v$.evaluationName.$model"
              required
            />
            <div v-if="v$.evaluationName.$anyDirty && v$.evaluationName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.evaluationName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.evaluationDate') }}</label>
            <b-form-input
              id="evaluation-evaluationDate"
              data-cy="evaluationDate"
              type="date"
              class="form-control"
              name="evaluationDate"
              :class="{ 'is-valid': !v$.evaluationDate.$invalid, 'is-invalid': v$.evaluationDate.$invalid }"
              v-model="v$.evaluationDate.$model"
            />
            <div v-if="v$.evaluationDate.$anyDirty && v$.evaluationDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.evaluationDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.periodLabel') }}</label>
            <input
              type="text"
              class="form-control"
              name="periodLabel"
              id="evaluation-periodLabel"
              data-cy="periodLabel"
              :class="{ valid: !v$.periodLabel.$invalid, invalid: v$.periodLabel.$invalid }"
              v-model="v$.periodLabel.$model"
            />
            <div v-if="v$.periodLabel.$anyDirty && v$.periodLabel.$invalid">
              <small class="form-text text-danger" v-for="error of v$.periodLabel.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.progressStatus') }}</label>
            <select
              class="form-control"
              name="progressStatus"
              :class="{ valid: !v$.progressStatus.$invalid, invalid: v$.progressStatus.$invalid }"
              v-model="v$.progressStatus.$model"
              id="evaluation-progressStatus"
              data-cy="progressStatus"
            >
              <option v-for="progressStatus in progressStatusValues" :key="progressStatus" :value="progressStatus">
                {{ progressStatus }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.result') }}</label>
            <select
              class="form-control"
              name="result"
              :class="{ valid: !v$.result.$invalid, invalid: v$.result.$invalid }"
              v-model="v$.result.$model"
              id="evaluation-result"
              data-cy="result"
            >
              <option v-for="assessmentResult in assessmentResultValues" :key="assessmentResult" :value="assessmentResult">
                {{ assessmentResult }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.strengths') }}</label>
            <textarea
              class="form-control"
              name="strengths"
              id="evaluation-strengths"
              data-cy="strengths"
              :class="{ valid: !v$.strengths.$invalid, invalid: v$.strengths.$invalid }"
              v-model="v$.strengths.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.weaknesses') }}</label>
            <textarea
              class="form-control"
              name="weaknesses"
              id="evaluation-weaknesses"
              data-cy="weaknesses"
              :class="{ valid: !v$.weaknesses.$invalid, invalid: v$.weaknesses.$invalid }"
              v-model="v$.weaknesses.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.supportNeeded') }}</label>
            <textarea
              class="form-control"
              name="supportNeeded"
              id="evaluation-supportNeeded"
              data-cy="supportNeeded"
              :class="{ valid: !v$.supportNeeded.$invalid, invalid: v$.supportNeeded.$invalid }"
              v-model="v$.supportNeeded.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.nextTrainingFocus') }}</label>
            <textarea
              class="form-control"
              name="nextTrainingFocus"
              id="evaluation-nextTrainingFocus"
              data-cy="nextTrainingFocus"
              :class="{ valid: !v$.nextTrainingFocus.$invalid, invalid: v$.nextTrainingFocus.$invalid }"
              v-model="v$.nextTrainingFocus.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.evaluation.field.positionAdjustmentNeeded') }}</label>
            <b-form-checkbox
              v-model="v$.positionAdjustmentNeeded.$model"
              name="positionAdjustmentNeeded"
              id="evaluation-positionAdjustmentNeeded"
              data-cy="positionAdjustmentNeeded"
              :class="{ 'is-valid': !v$.positionAdjustmentNeeded.$invalid, 'is-invalid': v$.positionAdjustmentNeeded.$invalid }"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('entity.person.detail.title') }}</label>
            <select class="form-control" id="evaluation-person" data-cy="person" name="person" v-model="evaluation.person" required>
              <option v-if="!evaluation.person" :value="null" selected></option>
              <option
                :value="evaluation.person && personOption.id === evaluation.person.id ? evaluation.person : personOption"
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
            <label class="form-control-label" for="evaluation">{{ $t('entity.position.detail.title') }}</label>
            <select class="form-control" id="evaluation-position" data-cy="position" name="position" v-model="evaluation.position">
              <option :value="null"></option>
              <option
                :value="evaluation.position && positionOption.id === evaluation.position.id ? evaluation.position : positionOption"
                v-for="positionOption in positions"
                :key="positionOption.id"
              >
                {{ positionOption.positionName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('global.entity.field.trainingGoal') }}</label>
            <select
              class="form-control"
              id="evaluation-trainingGoal"
              data-cy="trainingGoal"
              name="trainingGoal"
              v-model="evaluation.trainingGoal"
            >
              <option :value="null"></option>
              <option
                :value="
                  evaluation.trainingGoal && trainingGoalOption.id === evaluation.trainingGoal.id
                    ? evaluation.trainingGoal
                    : trainingGoalOption
                "
                v-for="trainingGoalOption in trainingGoals"
                :key="trainingGoalOption.id"
              >
                {{ trainingGoalOption.goalName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="evaluation">{{ $t('global.entity.field.evaluator') }}</label>
            <select class="form-control" id="evaluation-evaluator" data-cy="evaluator" name="evaluator" v-model="evaluation.evaluator">
              <option :value="null"></option>
              <option
                :value="evaluation.evaluator && personOption.id === evaluation.evaluator.id ? evaluation.evaluator : personOption"
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
<script lang="ts" src="./evaluation-update.component.ts"></script>
