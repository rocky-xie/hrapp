<template>
  <div>
    <h2 id="page-heading" data-cy="TrainingGoalHeading">
      <span id="training-goal">{{ $t('entity.trainingGoal.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'TrainingGoalCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-training-goal"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.trainingGoal.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && trainingGoals?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.trainingGoal.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="trainingGoals?.length > 0">
      <table class="table table-striped" aria-describedby="trainingGoals">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('goalName')">
              <span>{{ $t('entity.trainingGoal.field.goalName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'goalName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('goalDescription')">
              <span>{{ $t('entity.trainingGoal.field.goalDescription') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'goalDescription'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('targetLevelDescription')">
              <span>{{ $t('entity.trainingGoal.field.targetLevelDescription') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'targetLevelDescription'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('startDate')">
              <span>{{ $t('entity.improvementPlan.field.startDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('targetDate')">
              <span>{{ $t('entity.improvementPlan.field.targetDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'targetDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('status')">
              <span>{{ $t('entity.trainingGoal.field.status') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('person.personName')">
              <span>{{ $t('entity.person.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'person.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('skill.skillName')">
              <span>{{ $t('entity.skill.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'skill.skillName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('targetLevel.code')">
              <span>{{ $t('global.entity.field.targetLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'targetLevel.code'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="trainingGoal in trainingGoals" :key="trainingGoal.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TrainingGoalView', params: { trainingGoalId: trainingGoal.id } }">{{
                trainingGoal.id
              }}</router-link>
            </td>
            <td>{{ trainingGoal.goalName }}</td>
            <td>{{ trainingGoal.goalDescription }}</td>
            <td>{{ trainingGoal.targetLevelDescription }}</td>
            <td>{{ trainingGoal.startDate }}</td>
            <td>{{ trainingGoal.targetDate }}</td>
            <td>{{ trainingGoal.status }}</td>
            <td>
              <div v-if="trainingGoal.person">
                <router-link :to="{ name: 'PersonView', params: { personId: trainingGoal.person.id } }">{{
                  trainingGoal.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trainingGoal.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: trainingGoal.position.id } }">{{
                  trainingGoal.position.positionName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trainingGoal.skill">
                <router-link :to="{ name: 'SkillView', params: { skillId: trainingGoal.skill.id } }">{{
                  trainingGoal.skill.skillName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trainingGoal.targetLevel">
                <router-link :to="{ name: 'SkillLevelView', params: { skillLevelId: trainingGoal.targetLevel.id } }">{{
                  trainingGoal.targetLevel.code
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'TrainingGoalView', params: { trainingGoalId: trainingGoal.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TrainingGoalEdit', params: { trainingGoalId: trainingGoal.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(trainingGoal)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">{{ $t('global.form.delete') }}</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #title>
        <span id="hrappApp.trainingGoal.delete.question" data-cy="trainingGoalDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-trainingGoal-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.trainingGoal.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-trainingGoal"
            data-cy="entityConfirmDeleteButton"
            @click="removeTrainingGoal"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="trainingGoals?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./training-goal.component.ts"></script>
