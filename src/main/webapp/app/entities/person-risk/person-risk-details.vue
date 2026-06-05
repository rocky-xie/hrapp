<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="personRisk">
        <h2 class="jh-entity-heading" data-cy="personRiskDetailsHeading">
          <span>{{ $t('entity.personRisk.detail.title') }}</span> {{ personRisk.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.personRisk.field.riskType') }}</span>
          </dt>
          <dd>
            <span>{{ personRisk.riskType }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.positionRiskEvaluation.field.riskLevel') }}</span>
          </dt>
          <dd>
            <span>{{ personRisk.riskLevel }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.personRisk.field.riskDescription') }}</span>
          </dt>
          <dd>
            <span>{{ personRisk.riskDescription }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.improvementAction') }}</span>
          </dt>
          <dd>
            <span>{{ personRisk.improvementAction }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.personRisk.field.identifiedDate') }}</span>
          </dt>
          <dd>
            <span>{{ personRisk.identifiedDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.targetDate') }}</span>
          </dt>
          <dd>
            <span>{{ personRisk.targetDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.personRisk.field.closedDate') }}</span>
          </dt>
          <dd>
            <span>{{ personRisk.closedDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="personRisk.person">
              <router-link :to="{ name: 'PersonView', params: { personId: personRisk.person.id } }">{{
                personRisk.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="personRisk.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: personRisk.position.id } }">{{
                personRisk.position.positionName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="personRisk.id"
          :to="{ name: 'PersonRiskEdit', params: { personRiskId: personRisk.id } }"
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

<script lang="ts" src="./person-risk-details.component.ts"></script>
