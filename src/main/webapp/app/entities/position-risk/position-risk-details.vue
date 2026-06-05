<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="positionRisk">
        <h2 class="jh-entity-heading" data-cy="positionRiskDetailsHeading">
          <span>{{ $t('entity.positionRisk.detail.title') }}</span> {{ positionRisk.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.personRisk.field.riskType') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.riskType }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRiskEvaluation.field.riskLevel') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.riskLevel }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRisk.field.documentStatus') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.documentStatus }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRisk.field.backupStatus') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.backupStatus }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRisk.field.customerOrSystemDependency') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.customerOrSystemDependency }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.personRisk.field.riskDescription') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.riskDescription }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.improvementAction') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.improvementAction }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.personRisk.field.identifiedDate') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.identifiedDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.targetDate') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.targetDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.personRisk.field.closedDate') }}</span>
          </dt>
          <dd>
            <span>{{ positionRisk.closedDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="positionRisk.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: positionRisk.position.id } }">{{
                positionRisk.position.positionName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.category') }}</span>
          </dt>
          <dd>
            <div v-if="positionRisk.category">
              <router-link
                :to="{ name: 'KeyResponsibilityCategoryView', params: { keyResponsibilityCategoryId: positionRisk.category.id } }"
                >{{ positionRisk.category.categoryName }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="positionRisk.id"
          :to="{ name: 'PositionRiskEdit', params: { positionRiskId: positionRisk.id } }"
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

<script lang="ts" src="./position-risk-details.component.ts"></script>
