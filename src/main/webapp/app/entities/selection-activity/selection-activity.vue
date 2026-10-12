<template>
  <div>
    <h2 id="page-heading" data-cy="SelectionActivityHeading">
      <span id="selection-activity">{{ $t('entity.selectionActivity.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'SelectionActivityCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-selection-activity"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.selectionActivity.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && selectionActivities?.length === 0">
      <span>{{ $t('entity.selectionActivity.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="selectionActivities?.length > 0">
      <table class="table table-striped" aria-describedby="selectionActivities">
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
            <th scope="col" @click="changeOrder('activityType')">
              <span>{{ $t('recruitment.fields.activityType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activityType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('startAt')">
              <span>{{ $t('recruitment.fields.startAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('endAt')">
              <span>{{ $t('recruitment.fields.endAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('meetingRoomId')">
              <span>{{ $t('recruitment.fields.meetingRoomId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'meetingRoomId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('location')">
              <span>{{ $t('recruitment.fields.location') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'location'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('status')">
              <span>{{ $t('recruitment.fields.status') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('scheduleVersion')">
              <span>{{ $t('recruitment.fields.scheduleVersion') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'scheduleVersion'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('changeReason')">
              <span>{{ $t('recruitment.fields.changeReason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'changeReason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('externalCalendarProvider')">
              <span>{{ $t('recruitment.fields.externalCalendarProvider') }}</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'externalCalendarProvider'"
              ></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('externalEventId')">
              <span>{{ $t('recruitment.fields.externalEventId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'externalEventId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('application.id')">
              <span>{{ $t('recruitment.fields.application') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'application.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="selectionActivity in selectionActivities" :key="selectionActivity.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SelectionActivityView', params: { selectionActivityId: selectionActivity.id } }">{{
                selectionActivity.id
              }}</router-link>
            </td>
            <td>{{ $t('recruitment.enums.' + selectionActivity.stageType) }}</td>
            <td>{{ $t('recruitment.enums.' + selectionActivity.activityType) }}</td>
            <td>{{ formatDateShort(selectionActivity.startAt) || '' }}</td>
            <td>{{ formatDateShort(selectionActivity.endAt) || '' }}</td>
            <td>{{ selectionActivity.meetingRoomId }}</td>
            <td>{{ selectionActivity.location }}</td>
            <td>{{ selectionActivity.status }}</td>
            <td>{{ selectionActivity.scheduleVersion }}</td>
            <td>{{ selectionActivity.changeReason }}</td>
            <td>{{ selectionActivity.externalCalendarProvider }}</td>
            <td>{{ selectionActivity.externalEventId }}</td>
            <td>
              <div v-if="selectionActivity.application">
                <router-link
                  :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: selectionActivity.application.id } }"
                  >{{ selectionActivity.application.applicationCode }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SelectionActivityView', params: { selectionActivityId: selectionActivity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SelectionActivityEdit', params: { selectionActivityId: selectionActivity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(selectionActivity)"
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
        <span id="hrappApp.selectionActivity.delete.question" data-cy="selectionActivityDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-selection-activity-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.selectionActivity.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-selectionActivity"
            data-cy="entityConfirmDeleteButton"
            @click="removeSelectionActivity"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="selectionActivities?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./selection-activity.component.ts"></script>
