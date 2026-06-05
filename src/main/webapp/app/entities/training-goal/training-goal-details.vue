<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="trainingGoal">
        <h2 class="jh-entity-heading" data-cy="trainingGoalDetailsHeading">
          <span>{{ $t('entity.trainingGoal.detail.title') }}</span> {{ trainingGoal.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.trainingGoal.field.goalName') }}</span>
          </dt>
          <dd>
            <span>{{ trainingGoal.goalName }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trainingGoal.field.goalDescription') }}</span>
          </dt>
          <dd>
            <span>{{ trainingGoal.goalDescription }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trainingGoal.field.targetLevelDescription') }}</span>
          </dt>
          <dd>
            <span>{{ trainingGoal.targetLevelDescription }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.startDate') }}</span>
          </dt>
          <dd>
            <span>{{ trainingGoal.startDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.improvementPlan.field.targetDate') }}</span>
          </dt>
          <dd>
            <span>{{ trainingGoal.targetDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trainingGoal.field.status') }}</span>
          </dt>
          <dd>
            <span>{{ trainingGoal.status }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="trainingGoal.person">
              <router-link :to="{ name: 'PersonView', params: { personId: trainingGoal.person.id } }">{{
                trainingGoal.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="trainingGoal.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: trainingGoal.position.id } }">{{
                trainingGoal.position.positionName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.skill.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="trainingGoal.skill">
              <router-link :to="{ name: 'SkillView', params: { skillId: trainingGoal.skill.id } }">{{
                trainingGoal.skill.skillName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.targetLevel') }}</span>
          </dt>
          <dd>
            <div v-if="trainingGoal.targetLevel">
              <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: trainingGoal.targetLevel.id } }">{{
                trainingGoal.targetLevel.code
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="trainingGoal.id"
          :to="{ name: 'TrainingGoalEdit', params: { trainingGoalId: trainingGoal.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.edit') }}</span>
          </button>
        </router-link>
        <button
          v-if="
            trainingGoal.person &&
            trainingGoal.skill &&
            trainingGoal.targetLevel &&
            trainingGoal.status !== 'COMPLETED' &&
            trainingGoal.status !== 'CANCELLED'
          "
          @click="completeTrainingGoal"
          class="btn btn-success"
          data-cy="entityDetailsCompleteButton"
        >
          <font-awesome-icon icon="check"></font-awesome-icon>&nbsp;<span>{{ $t('entity.trainingGoal.action.complete') }}</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./training-goal-details.component.ts"></script>
