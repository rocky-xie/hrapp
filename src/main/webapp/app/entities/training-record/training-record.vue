<template>
  <div>
    <h2 id="page-heading" data-cy="TrainingRecordHeading">
      <span id="training-record">{{ $t('entity.trainingRecord.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'TrainingRecordCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-training-record"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.trainingRecord.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && trainingRecords?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.trainingRecord.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="trainingRecords?.length > 0">
      <table class="table table-striped" aria-describedby="trainingRecords">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('trainingDate')">
              <span>{{ $t('entity.trainingRecord.field.trainingDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'trainingDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('trainingType')">
              <span>{{ $t('entity.trainingRecord.field.trainingType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'trainingType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('topic')">
              <span>{{ $t('entity.trainingRecord.field.topic') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'topic'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('taskDescription')">
              <span>{{ $t('entity.trainingRecord.field.taskDescription') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'taskDescription'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('resultDescription')">
              <span>{{ $t('entity.trainingRecord.field.resultDescription') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'resultDescription'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evidence')">
              <span>{{ $t('entity.candidateProfile.field.evidence') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evidence'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nextAction')">
              <span>{{ $t('entity.trainingRecord.field.nextAction') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nextAction'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('person.personName')">
              <span>{{ $t('entity.person.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'person.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('trainingGoal.goalName')">
              <span>{{ $t('global.entity.field.trainingGoal') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'trainingGoal.goalName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('mentor.personName')">
              <span>{{ $t('global.entity.field.mentor') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mentor.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="trainingRecord in trainingRecords" :key="trainingRecord.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TrainingRecordView', params: { trainingRecordId: trainingRecord.id } }">{{
                trainingRecord.id
              }}</router-link>
            </td>
            <td>{{ trainingRecord.trainingDate }}</td>
            <td>{{ trainingRecord.trainingType }}</td>
            <td>{{ trainingRecord.topic }}</td>
            <td>{{ trainingRecord.taskDescription }}</td>
            <td>{{ trainingRecord.resultDescription }}</td>
            <td>{{ trainingRecord.evidence }}</td>
            <td>{{ trainingRecord.nextAction }}</td>
            <td>
              <div v-if="trainingRecord.person">
                <router-link :to="{ name: 'PersonView', params: { personId: trainingRecord.person.id } }">{{
                  trainingRecord.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trainingRecord.trainingGoal">
                <router-link :to="{ name: 'TrainingGoalView', params: { trainingGoalId: trainingRecord.trainingGoal.id } }">{{
                  trainingRecord.trainingGoal.goalName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trainingRecord.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: trainingRecord.position.id } }">{{
                  trainingRecord.position.positionName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trainingRecord.mentor">
                <router-link :to="{ name: 'PersonView', params: { personId: trainingRecord.mentor.id } }">{{
                  trainingRecord.mentor.personName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'TrainingRecordView', params: { trainingRecordId: trainingRecord.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'TrainingRecordEdit', params: { trainingRecordId: trainingRecord.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(trainingRecord)"
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
        <span id="hrappApp.trainingRecord.delete.question" data-cy="trainingRecordDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-trainingRecord-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.trainingRecord.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-trainingRecord"
            data-cy="entityConfirmDeleteButton"
            @click="removeTrainingRecord"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="trainingRecords?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./training-record.component.ts"></script>
