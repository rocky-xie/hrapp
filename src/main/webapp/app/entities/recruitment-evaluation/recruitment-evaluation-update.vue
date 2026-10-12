<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hrappApp.recruitmentEvaluation.home.createOrEditLabel" data-cy="RecruitmentEvaluationCreateUpdateHeading">
          {{ $t('entity.recruitmentEvaluation.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="recruitmentEvaluation.id">
            <label for="id">{{ $t('recruitment.ui.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="recruitmentEvaluation.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.stageType') }}</label>
            <select
              class="form-control"
              name="stageType"
              :class="{ valid: !v$.stageType.$invalid, invalid: v$.stageType.$invalid }"
              v-model="v$.stageType.$model"
              id="recruitment-evaluation-stageType"
              data-cy="stageType"
              required
            >
              <option v-for="applicationStageType in applicationStageTypeValues" :key="applicationStageType" :value="applicationStageType">
                {{ $t('recruitment.enums.' + applicationStageType) }}
              </option>
            </select>
            <div v-if="v$.stageType.$anyDirty && v$.stageType.$invalid">
              <small class="form-text text-danger" v-for="error of v$.stageType.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.activityId') }}</label>
            <input
              type="number"
              class="form-control"
              name="activityId"
              id="recruitment-evaluation-activityId"
              data-cy="activityId"
              :class="{ valid: !v$.activityId.$invalid, invalid: v$.activityId.$invalid }"
              v-model.number="v$.activityId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.templateVersionId') }}</label>
            <input
              type="number"
              class="form-control"
              name="templateVersionId"
              id="recruitment-evaluation-templateVersionId"
              data-cy="templateVersionId"
              :class="{ valid: !v$.templateVersionId.$invalid, invalid: v$.templateVersionId.$invalid }"
              v-model.number="v$.templateVersionId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.evaluatorUsername') }}</label>
            <input
              type="text"
              class="form-control"
              name="evaluatorUsername"
              id="recruitment-evaluation-evaluatorUsername"
              data-cy="evaluatorUsername"
              :class="{ valid: !v$.evaluatorUsername.$invalid, invalid: v$.evaluatorUsername.$invalid }"
              v-model="v$.evaluatorUsername.$model"
              required
            />
            <div v-if="v$.evaluatorUsername.$anyDirty && v$.evaluatorUsername.$invalid">
              <small class="form-text text-danger" v-for="error of v$.evaluatorUsername.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.recordedByUsername') }}</label>
            <input
              type="text"
              class="form-control"
              name="recordedByUsername"
              id="recruitment-evaluation-recordedByUsername"
              data-cy="recordedByUsername"
              :class="{ valid: !v$.recordedByUsername.$invalid, invalid: v$.recordedByUsername.$invalid }"
              v-model="v$.recordedByUsername.$model"
              required
            />
            <div v-if="v$.recordedByUsername.$anyDirty && v$.recordedByUsername.$invalid">
              <small class="form-text text-danger" v-for="error of v$.recordedByUsername.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.status') }}</label>
            <input
              type="text"
              class="form-control"
              name="status"
              id="recruitment-evaluation-status"
              data-cy="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              required
            />
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.overallRecommendation') }}</label>
            <input
              type="text"
              class="form-control"
              name="overallRecommendation"
              id="recruitment-evaluation-overallRecommendation"
              data-cy="overallRecommendation"
              :class="{ valid: !v$.overallRecommendation.$invalid, invalid: v$.overallRecommendation.$invalid }"
              v-model="v$.overallRecommendation.$model"
            />
            <div v-if="v$.overallRecommendation.$anyDirty && v$.overallRecommendation.$invalid">
              <small class="form-text text-danger" v-for="error of v$.overallRecommendation.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.totalScore') }}</label>
            <input
              type="number"
              class="form-control"
              name="totalScore"
              id="recruitment-evaluation-totalScore"
              data-cy="totalScore"
              :class="{ valid: !v$.totalScore.$invalid, invalid: v$.totalScore.$invalid }"
              v-model.number="v$.totalScore.$model"
            />
            <div v-if="v$.totalScore.$anyDirty && v$.totalScore.$invalid">
              <small class="form-text text-danger" v-for="error of v$.totalScore.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.summary') }}</label>
            <textarea
              class="form-control"
              name="summary"
              id="recruitment-evaluation-summary"
              data-cy="summary"
              :class="{ valid: !v$.summary.$invalid, invalid: v$.summary.$invalid }"
              v-model="v$.summary.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.answersJson') }}</label>
            <textarea
              class="form-control"
              name="answersJson"
              id="recruitment-evaluation-answersJson"
              data-cy="answersJson"
              :class="{ valid: !v$.answersJson.$invalid, invalid: v$.answersJson.$invalid }"
              v-model="v$.answersJson.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.evaluatedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-evaluation-evaluatedAt"
                data-cy="evaluatedAt"
                type="datetime-local"
                class="form-control"
                name="evaluatedAt"
                :class="{ valid: !v$.evaluatedAt.$invalid, invalid: v$.evaluatedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.evaluatedAt.$model)"
                @change="updateInstantField('evaluatedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.submittedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-evaluation-submittedAt"
                data-cy="submittedAt"
                type="datetime-local"
                class="form-control"
                name="submittedAt"
                :class="{ valid: !v$.submittedAt.$invalid, invalid: v$.submittedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.submittedAt.$model)"
                @change="updateInstantField('submittedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.publishedAt') }}</label>
            <div class="d-flex">
              <input
                id="recruitment-evaluation-publishedAt"
                data-cy="publishedAt"
                type="datetime-local"
                class="form-control"
                name="publishedAt"
                :class="{ valid: !v$.publishedAt.$invalid, invalid: v$.publishedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.publishedAt.$model)"
                @change="updateInstantField('publishedAt', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.correctionOfId') }}</label>
            <input
              type="number"
              class="form-control"
              name="correctionOfId"
              id="recruitment-evaluation-correctionOfId"
              data-cy="correctionOfId"
              :class="{ valid: !v$.correctionOfId.$invalid, invalid: v$.correctionOfId.$invalid }"
              v-model.number="v$.correctionOfId.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="recruitment-evaluation">{{ $t('recruitment.fields.application') }}</label>
            <select
              class="form-control"
              id="recruitment-evaluation-application"
              data-cy="application"
              name="application"
              v-model="recruitmentEvaluation.application"
            >
              <option :value="null"></option>
              <option
                :value="
                  recruitmentEvaluation.application && recruitmentApplicationOption.id === recruitmentEvaluation.application.id
                    ? recruitmentEvaluation.application
                    : recruitmentApplicationOption
                "
                v-for="recruitmentApplicationOption in recruitmentApplications"
                :key="recruitmentApplicationOption.id"
              >
                {{ recruitmentApplicationOption.applicationCode }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.cancel') }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.save') }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./recruitment-evaluation-update.component.ts"></script>
