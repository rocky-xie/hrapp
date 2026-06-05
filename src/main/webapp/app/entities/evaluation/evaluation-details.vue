<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="evaluation">
        <h2 class="jh-entity-heading" data-cy="evaluationDetailsHeading">
          <span>{{ $t('entity.evaluation.detail.title') }}</span> {{ evaluation.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.evaluation.field.evaluationName') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.evaluationName }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.evaluationDate') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.evaluationDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.periodLabel') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.periodLabel }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.progressStatus') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.progressStatus }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.result') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.result }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.strengths') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.strengths }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.weaknesses') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.weaknesses }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.supportNeeded') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.supportNeeded }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.nextTrainingFocus') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.nextTrainingFocus }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.evaluation.field.positionAdjustmentNeeded') }}</span>
          </dt>
          <dd>
            <span>{{ evaluation.positionAdjustmentNeeded }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="evaluation.person">
              <router-link :to="{ name: 'PersonView', params: { personId: evaluation.person.id } }">{{
                evaluation.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="evaluation.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: evaluation.position.id } }">{{
                evaluation.position.positionName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.trainingGoal') }}</span>
          </dt>
          <dd>
            <div v-if="evaluation.trainingGoal">
              <router-link :to="{ name: 'TrainingGoalView', params: { trainingGoalId: evaluation.trainingGoal.id } }">{{
                evaluation.trainingGoal.goalName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.evaluator') }}</span>
          </dt>
          <dd>
            <div v-if="evaluation.evaluator">
              <router-link :to="{ name: 'PersonView', params: { personId: evaluation.evaluator.id } }">{{
                evaluation.evaluator.personName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="evaluation.id"
          :to="{ name: 'EvaluationEdit', params: { evaluationId: evaluation.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./evaluation-details.component.ts"></script>
