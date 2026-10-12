<template>
  <div>
    <h2 id="page-heading" data-cy="RecruitmentRequisitionHeading">
      <span id="recruitment-requisition">{{ $t('entity.recruitmentRequisition.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'RecruitmentRequisitionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-recruitment-requisition"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.recruitmentRequisition.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && recruitmentRequisitions?.length === 0">
      <span>{{ $t('entity.recruitmentRequisition.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="recruitmentRequisitions?.length > 0">
      <table class="table table-striped" aria-describedby="recruitmentRequisitions">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('requisitionCode')">
              <span>{{ $t('recruitment.fields.requisitionCode') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'requisitionCode'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('title')">
              <span>{{ $t('recruitment.fields.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('department')">
              <span>{{ $t('recruitment.fields.department') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'department'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('positionId')">
              <span>{{ $t('recruitment.fields.positionId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'positionId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('recruitmentType')">
              <span>{{ $t('recruitment.fields.recruitmentType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'recruitmentType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('lifecycle')">
              <span>{{ $t('recruitment.fields.lifecycle') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lifecycle'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('editStatus')">
              <span>{{ $t('recruitment.fields.editStatus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'editStatus'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('plannedHeadcount')">
              <span>{{ $t('recruitment.fields.plannedHeadcount') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'plannedHeadcount'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('hiredCount')">
              <span>{{ $t('recruitment.fields.hiredCount') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'hiredCount'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('checkedInCount')">
              <span>{{ $t('recruitment.fields.checkedInCount') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'checkedInCount'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('priority')">
              <span>{{ $t('recruitment.fields.priority') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priority'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reason')">
              <span>{{ $t('recruitment.fields.reason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('targetJoinDate')">
              <span>{{ $t('recruitment.fields.targetJoinDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'targetJoinDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('desiredStartDate')">
              <span>{{ $t('recruitment.fields.desiredStartDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'desiredStartDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('desiredEndDate')">
              <span>{{ $t('recruitment.fields.desiredEndDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'desiredEndDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('currentVersion')">
              <span>{{ $t('recruitment.fields.currentVersion') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'currentVersion'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('lockedAt')">
              <span>{{ $t('recruitment.fields.lockedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lockedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('lockedBy')">
              <span>{{ $t('recruitment.fields.lockedBy') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lockedBy'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('unlockedAt')">
              <span>{{ $t('recruitment.fields.unlockedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unlockedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('unlockReason')">
              <span>{{ $t('recruitment.fields.unlockReason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unlockReason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('requirementsJson')">
              <span>{{ $t('recruitment.fields.requirementsJson') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'requirementsJson'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="recruitmentRequisition in recruitmentRequisitions" :key="recruitmentRequisition.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'RecruitmentRequisitionView', params: { recruitmentRequisitionId: recruitmentRequisition.id } }">{{
                recruitmentRequisition.id
              }}</router-link>
            </td>
            <td>{{ recruitmentRequisition.requisitionCode }}</td>
            <td>{{ recruitmentRequisition.title }}</td>
            <td>{{ recruitmentRequisition.department }}</td>
            <td>{{ recruitmentRequisition.positionId }}</td>
            <td>{{ $t('recruitment.enums.' + recruitmentRequisition.recruitmentType) }}</td>
            <td>{{ $t('recruitment.enums.' + recruitmentRequisition.lifecycle) }}</td>
            <td>{{ $t('recruitment.enums.' + recruitmentRequisition.editStatus) }}</td>
            <td>{{ recruitmentRequisition.plannedHeadcount }}</td>
            <td>{{ recruitmentRequisition.hiredCount }}</td>
            <td>{{ recruitmentRequisition.checkedInCount }}</td>
            <td>{{ recruitmentRequisition.priority ? $t('recruitment.enums.' + recruitmentRequisition.priority) : '—' }}</td>
            <td>{{ recruitmentRequisition.reason }}</td>
            <td>{{ recruitmentRequisition.targetJoinDate }}</td>
            <td>{{ recruitmentRequisition.desiredStartDate }}</td>
            <td>{{ recruitmentRequisition.desiredEndDate }}</td>
            <td>{{ recruitmentRequisition.currentVersion }}</td>
            <td>{{ formatDateShort(recruitmentRequisition.lockedAt) || '' }}</td>
            <td>{{ recruitmentRequisition.lockedBy }}</td>
            <td>{{ formatDateShort(recruitmentRequisition.unlockedAt) || '' }}</td>
            <td>{{ recruitmentRequisition.unlockReason }}</td>
            <td>{{ recruitmentRequisition.requirementsJson }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'RecruitmentRequisitionView', params: { recruitmentRequisitionId: recruitmentRequisition.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'RecruitmentRequisitionEdit', params: { recruitmentRequisitionId: recruitmentRequisition.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(recruitmentRequisition)"
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
        <span id="hrappApp.recruitmentRequisition.delete.question" data-cy="recruitmentRequisitionDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-recruitment-requisition-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.recruitmentRequisition.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-recruitmentRequisition"
            data-cy="entityConfirmDeleteButton"
            @click="removeRecruitmentRequisition"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="recruitmentRequisitions?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./recruitment-requisition.component.ts"></script>
