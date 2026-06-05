<template>
  <div>
    <h2 id="page-heading" data-cy="EvaluationHeading">
      <span id="evaluation">{{ $t('entity.evaluation.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'EvaluationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-evaluation"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.evaluation.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && evaluations?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.evaluation.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="evaluations?.length > 0">
      <table class="table table-striped" aria-describedby="evaluations">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evaluationName')">
              <span>{{ $t('entity.evaluation.field.evaluationName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evaluationName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evaluationDate')">
              <span>{{ $t('entity.evaluation.field.evaluationDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evaluationDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('periodLabel')">
              <span>{{ $t('entity.evaluation.field.periodLabel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'periodLabel'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('progressStatus')">
              <span>{{ $t('entity.evaluation.field.progressStatus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'progressStatus'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('result')">
              <span>{{ $t('entity.evaluation.field.result') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'result'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('strengths')">
              <span>{{ $t('entity.evaluation.field.strengths') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'strengths'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('weaknesses')">
              <span>{{ $t('entity.evaluation.field.weaknesses') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'weaknesses'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('supportNeeded')">
              <span>{{ $t('entity.evaluation.field.supportNeeded') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'supportNeeded'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nextTrainingFocus')">
              <span>{{ $t('entity.evaluation.field.nextTrainingFocus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nextTrainingFocus'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('positionAdjustmentNeeded')">
              <span>{{ $t('entity.evaluation.field.positionAdjustmentNeeded') }}</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'positionAdjustmentNeeded'"
              ></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('person.personName')">
              <span>{{ $t('entity.person.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'person.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('trainingGoal.goalName')">
              <span>{{ $t('global.entity.field.trainingGoal') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'trainingGoal.goalName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evaluator.personName')">
              <span>{{ $t('global.entity.field.evaluator') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evaluator.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="evaluation in evaluations" :key="evaluation.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EvaluationView', params: { evaluationId: evaluation.id } }">{{ evaluation.id }}</router-link>
            </td>
            <td>{{ evaluation.evaluationName }}</td>
            <td>{{ evaluation.evaluationDate }}</td>
            <td>{{ evaluation.periodLabel }}</td>
            <td>{{ evaluation.progressStatus }}</td>
            <td>{{ evaluation.result }}</td>
            <td>{{ evaluation.strengths }}</td>
            <td>{{ evaluation.weaknesses }}</td>
            <td>{{ evaluation.supportNeeded }}</td>
            <td>{{ evaluation.nextTrainingFocus }}</td>
            <td>{{ evaluation.positionAdjustmentNeeded }}</td>
            <td>
              <div v-if="evaluation.person">
                <router-link :to="{ name: 'PersonView', params: { personId: evaluation.person.id } }">{{
                  evaluation.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="evaluation.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: evaluation.position.id } }">{{
                  evaluation.position.positionName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="evaluation.trainingGoal">
                <router-link :to="{ name: 'TrainingGoalView', params: { trainingGoalId: evaluation.trainingGoal.id } }">{{
                  evaluation.trainingGoal.goalName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="evaluation.evaluator">
                <router-link :to="{ name: 'PersonView', params: { personId: evaluation.evaluator.id } }">{{
                  evaluation.evaluator.personName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'EvaluationView', params: { evaluationId: evaluation.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EvaluationEdit', params: { evaluationId: evaluation.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(evaluation)"
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
        <span id="hrappApp.evaluation.delete.question" data-cy="evaluationDeleteDialogHeading">{{ $t('global.entity.deleteTitle') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-evaluation-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.evaluation.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-evaluation"
            data-cy="entityConfirmDeleteButton"
            @click="removeEvaluation"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="evaluations?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./evaluation.component.ts"></script>
