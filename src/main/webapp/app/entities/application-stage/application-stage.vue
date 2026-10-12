<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationStageHeading">
      <span id="application-stage">{{ $t('entity.applicationStage.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'ApplicationStageCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-application-stage"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.applicationStage.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && applicationStages?.length === 0">
      <span>{{ $t('entity.applicationStage.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="applicationStages?.length > 0">
      <table class="table table-striped" aria-describedby="applicationStages">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('stageType')">
              <span>{{ $t('recruitment.fields.stageType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stageType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sequenceNumber')">
              <span>{{ $t('recruitment.fields.sequenceNumber') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sequenceNumber'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('stageStatus')">
              <span>{{ $t('recruitment.fields.stageStatus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stageStatus'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('startedAt')">
              <span>{{ $t('recruitment.fields.startedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('completedAt')">
              <span>{{ $t('recruitment.fields.completedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'completedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('selected')">
              <span>{{ $t('recruitment.fields.selected') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'selected'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('decisionReason')">
              <span>{{ $t('recruitment.fields.decisionReason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'decisionReason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('application.id')">
              <span>{{ $t('recruitment.fields.application') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'application.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="applicationStage in applicationStages" :key="applicationStage.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ApplicationStageView', params: { applicationStageId: applicationStage.id } }">{{
                applicationStage.id
              }}</router-link>
            </td>
            <td>{{ $t('recruitment.enums.' + applicationStage.stageType) }}</td>
            <td>{{ applicationStage.sequenceNumber }}</td>
            <td>{{ $t('recruitment.enums.' + applicationStage.stageStatus) }}</td>
            <td>{{ formatDateShort(applicationStage.startedAt) || '' }}</td>
            <td>{{ formatDateShort(applicationStage.completedAt) || '' }}</td>
            <td>{{ applicationStage.selected }}</td>
            <td>{{ applicationStage.decisionReason }}</td>
            <td>
              <div v-if="applicationStage.application">
                <router-link
                  :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: applicationStage.application.id } }"
                  >{{ applicationStage.application.applicationCode }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ApplicationStageView', params: { applicationStageId: applicationStage.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ApplicationStageEdit', params: { applicationStageId: applicationStage.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(applicationStage)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">{{ $t('recruitment.ui.delete') }}</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #title>
        <span id="hrappApp.applicationStage.delete.question" data-cy="applicationStageDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-application-stage-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.applicationStage.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-applicationStage"
            data-cy="entityConfirmDeleteButton"
            @click="removeApplicationStage"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="applicationStages?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./application-stage.component.ts"></script>
