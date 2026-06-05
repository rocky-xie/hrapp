<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="positionRiskEvaluation">
        <h2 class="jh-entity-heading" data-cy="positionRiskEvaluationDetailsHeading">
          <span>{{ $t('entity.positionRiskEvaluation.detail.title') }}</span> {{ positionRiskEvaluation.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.evaluation.field.evaluationDate') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.evaluationDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRiskEvaluation.field.ownerCount') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.ownerCount }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRiskEvaluation.field.substitutableOwnerCount') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.substitutableOwnerCount }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRiskEvaluation.field.hasSubstitute') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.hasSubstitute }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRisk.field.documentStatus') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.documentStatus }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRisk.field.customerOrSystemDependency') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.customerOrSystemDependency }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.successionCandidate.field.successionReadiness') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.successionReadiness }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRiskEvaluation.field.riskLevel') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.riskLevel }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRiskEvaluation.field.riskReason') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.riskReason }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRiskEvaluation.field.recommendedAction') }}</span>
          </dt>
          <dd>
            <span>{{ positionRiskEvaluation.recommendedAction }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="positionRiskEvaluation.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: positionRiskEvaluation.position.id } }">{{
                positionRiskEvaluation.position.positionName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="positionRiskEvaluation.id"
          :to="{ name: 'PositionRiskEvaluationEdit', params: { positionRiskEvaluationId: positionRiskEvaluation.id } }"
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

<script lang="ts" src="./position-risk-evaluation-details.component.ts"></script>
