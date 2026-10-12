<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="recruitmentEvaluation">
        <h2 class="jh-entity-heading" data-cy="recruitmentEvaluationDetailsHeading">
          <span>{{ $t('entity.recruitmentEvaluation.detail.title') }}</span> {{ recruitmentEvaluation.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.stageType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + recruitmentEvaluation.stageType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.activityId') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentEvaluation.activityId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.templateVersionId') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentEvaluation.templateVersionId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.evaluatorUsername') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentEvaluation.evaluatorUsername }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.recordedByUsername') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentEvaluation.recordedByUsername }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.status') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + recruitmentEvaluation.status) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.overallRecommendation') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentEvaluation.overallRecommendation }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.totalScore') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentEvaluation.totalScore }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.summary') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentEvaluation.summary }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.answersJson') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentEvaluation.answersJson }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.evaluatedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentEvaluation.evaluatedAt">{{ formatDateLong(recruitmentEvaluation.evaluatedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.submittedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentEvaluation.submittedAt">{{ formatDateLong(recruitmentEvaluation.submittedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.publishedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentEvaluation.publishedAt">{{ formatDateLong(recruitmentEvaluation.publishedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.correctionOfId') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentEvaluation.correctionOfId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.application') }}</span>
          </dt>
          <dd>
            <div v-if="recruitmentEvaluation.application">
              <router-link
                :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: recruitmentEvaluation.application.id } }"
                >{{ recruitmentEvaluation.application.applicationCode }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="recruitmentEvaluation.id"
          :to="{ name: 'RecruitmentEvaluationEdit', params: { recruitmentEvaluationId: recruitmentEvaluation.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./recruitment-evaluation-details.component.ts"></script>
