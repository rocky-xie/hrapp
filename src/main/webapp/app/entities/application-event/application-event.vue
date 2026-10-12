<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationEventHeading">
      <span id="application-event">{{ $t('entity.applicationEvent.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'ApplicationEventCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-application-event"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.applicationEvent.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && applicationEvents?.length === 0">
      <span>{{ $t('entity.applicationEvent.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="applicationEvents?.length > 0">
      <table class="table table-striped" aria-describedby="applicationEvents">
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
            <th scope="col" @click="changeOrder('eventType')">
              <span>{{ $t('recruitment.fields.eventType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eventType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('previousValue')">
              <span>{{ $t('recruitment.fields.previousValue') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'previousValue'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('newValue')">
              <span>{{ $t('recruitment.fields.newValue') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'newValue'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('actor')">
              <span>{{ $t('recruitment.fields.actor') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'actor'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('occurredAt')">
              <span>{{ $t('recruitment.fields.occurredAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'occurredAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reason')">
              <span>{{ $t('recruitment.fields.reason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('metadataJson')">
              <span>{{ $t('recruitment.fields.metadataJson') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'metadataJson'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('application.id')">
              <span>{{ $t('recruitment.fields.application') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'application.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="applicationEvent in applicationEvents" :key="applicationEvent.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ApplicationEventView', params: { applicationEventId: applicationEvent.id } }">{{
                applicationEvent.id
              }}</router-link>
            </td>
            <td>{{ $t('recruitment.enums.' + applicationEvent.stageType) }}</td>
            <td>{{ $t('recruitment.enums.' + applicationEvent.eventType) }}</td>
            <td>{{ applicationEvent.previousValue }}</td>
            <td>{{ applicationEvent.newValue }}</td>
            <td>{{ applicationEvent.actor }}</td>
            <td>{{ formatDateShort(applicationEvent.occurredAt) || '' }}</td>
            <td>{{ applicationEvent.reason }}</td>
            <td>{{ applicationEvent.metadataJson }}</td>
            <td>
              <div v-if="applicationEvent.application">
                <router-link
                  :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: applicationEvent.application.id } }"
                  >{{ applicationEvent.application.applicationCode }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ApplicationEventView', params: { applicationEventId: applicationEvent.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ApplicationEventEdit', params: { applicationEventId: applicationEvent.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(applicationEvent)"
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
        <span id="hrappApp.applicationEvent.delete.question" data-cy="applicationEventDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-application-event-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.applicationEvent.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-applicationEvent"
            data-cy="entityConfirmDeleteButton"
            @click="removeApplicationEvent"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="applicationEvents?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./application-event.component.ts"></script>
