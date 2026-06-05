<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="improvementPlan">
        <h2 class="jh-entity-heading" data-cy="improvementPlanDetailsHeading">
          <span>{{ $t('entity.improvementPlan.detail.title') }}</span> {{ improvementPlan.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.improvementPlan.field.planName') }}</span>
          </dt>
          <dd>
            <span>{{ improvementPlan.planName }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.planStatus') }}</span>
          </dt>
          <dd>
            <span>{{ improvementPlan.planStatus }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.problemSummary') }}</span>
          </dt>
          <dd>
            <span>{{ improvementPlan.problemSummary }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.improvementAction') }}</span>
          </dt>
          <dd>
            <span>{{ improvementPlan.improvementAction }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.ownerName') }}</span>
          </dt>
          <dd>
            <span>{{ improvementPlan.ownerName }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.startDate') }}</span>
          </dt>
          <dd>
            <span>{{ improvementPlan.startDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.targetDate') }}</span>
          </dt>
          <dd>
            <span>{{ improvementPlan.targetDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.completionDate') }}</span>
          </dt>
          <dd>
            <span>{{ improvementPlan.completionDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.reviewResult') }}</span>
          </dt>
          <dd>
            <span>{{ improvementPlan.reviewResult }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="improvementPlan.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: improvementPlan.position.id } }">{{
                improvementPlan.position.positionName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="improvementPlan.skill">
              <router-link :to="{ name: 'SkillView', params: { skillId: improvementPlan.skill.id } }">{{
                improvementPlan.skill.skillName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="improvementPlan.id"
          :to="{ name: 'ImprovementPlanEdit', params: { improvementPlanId: improvementPlan.id } }"
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

<script lang="ts" src="./improvement-plan-details.component.ts"></script>
