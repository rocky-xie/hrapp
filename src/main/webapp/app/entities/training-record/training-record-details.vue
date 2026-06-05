<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="trainingRecord">
        <h2 class="jh-entity-heading" data-cy="trainingRecordDetailsHeading">
          <span>{{ $t('entity.trainingRecord.detail.title') }}</span> {{ trainingRecord.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('entity.trainingRecord.field.trainingDate') }}</span>
          </dt>
          <dd>
            <span>{{ trainingRecord.trainingDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trainingRecord.field.trainingType') }}</span>
          </dt>
          <dd>
            <span>{{ trainingRecord.trainingType }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trainingRecord.field.topic') }}</span>
          </dt>
          <dd>
            <span>{{ trainingRecord.topic }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trainingRecord.field.taskDescription') }}</span>
          </dt>
          <dd>
            <span>{{ trainingRecord.taskDescription }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trainingRecord.field.resultDescription') }}</span>
          </dt>
          <dd>
            <span>{{ trainingRecord.resultDescription }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.candidateProfile.field.evidence') }}</span>
          </dt>
          <dd>
            <span>{{ trainingRecord.evidence }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.trainingRecord.field.nextAction') }}</span>
          </dt>
          <dd>
            <span>{{ trainingRecord.nextAction }}</span>
          </dd>
          <dt>
            <span>{{ $t('entity.person.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="trainingRecord.person">
              <router-link :to="{ name: 'PersonView', params: { personId: trainingRecord.person.id } }">{{
                trainingRecord.person.personName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.trainingGoal') }}</span>
          </dt>
          <dd>
            <div v-if="trainingRecord.trainingGoal">
              <router-link :to="{ name: 'TrainingGoalView', params: { trainingGoalId: trainingRecord.trainingGoal.id } }">{{
                trainingRecord.trainingGoal.goalName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('entity.position.detail.title') }}</span>
          </dt>
          <dd>
            <div v-if="trainingRecord.position">
              <router-link :to="{ name: 'PositionView', params: { positionId: trainingRecord.position.id } }">{{
                trainingRecord.position.positionName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('global.entity.field.mentor') }}</span>
          </dt>
          <dd>
            <div v-if="trainingRecord.mentor">
              <router-link :to="{ name: 'PersonView', params: { personId: trainingRecord.mentor.id } }">{{
                trainingRecord.mentor.personName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('global.form.back') }}</span>
        </button>
        <router-link
          v-if="trainingRecord.id"
          :to="{ name: 'TrainingRecordEdit', params: { trainingRecordId: trainingRecord.id } }"
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

<script lang="ts" src="./training-record-details.component.ts"></script>
