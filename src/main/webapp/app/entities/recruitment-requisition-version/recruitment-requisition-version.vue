<template>
  <div>
    <h2 id="page-heading" data-cy="RecruitmentRequisitionVersionHeading">
      <span id="recruitment-requisition-version">{{ $t('entity.recruitmentRequisitionVersion.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'RecruitmentRequisitionVersionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-recruitment-requisition-version"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.recruitmentRequisitionVersion.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && recruitmentRequisitionVersions?.length === 0">
      <span>{{ $t('entity.recruitmentRequisitionVersion.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="recruitmentRequisitionVersions?.length > 0">
      <table class="table table-striped" aria-describedby="recruitmentRequisitionVersions">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('versionNumber')">
              <span>{{ $t('recruitment.fields.versionNumber') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'versionNumber'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('snapshotJson')">
              <span>{{ $t('recruitment.fields.snapshotJson') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'snapshotJson'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('lockedAt')">
              <span>{{ $t('recruitment.fields.lockedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lockedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('lockedBy')">
              <span>{{ $t('recruitment.fields.lockedBy') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lockedBy'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('changeReason')">
              <span>{{ $t('recruitment.fields.changeReason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'changeReason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('requisition.id')">
              <span>{{ $t('recruitment.fields.requisition') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'requisition.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="recruitmentRequisitionVersion in recruitmentRequisitionVersions"
            :key="recruitmentRequisitionVersion.id"
            data-cy="entityTable"
          >
            <td>
              <router-link
                :to="{
                  name: 'RecruitmentRequisitionVersionView',
                  params: { recruitmentRequisitionVersionId: recruitmentRequisitionVersion.id },
                }"
                >{{ recruitmentRequisitionVersion.id }}</router-link
              >
            </td>
            <td>{{ recruitmentRequisitionVersion.versionNumber }}</td>
            <td>{{ recruitmentRequisitionVersion.snapshotJson }}</td>
            <td>{{ formatDateShort(recruitmentRequisitionVersion.lockedAt) || '' }}</td>
            <td>{{ recruitmentRequisitionVersion.lockedBy }}</td>
            <td>{{ recruitmentRequisitionVersion.changeReason }}</td>
            <td>
              <div v-if="recruitmentRequisitionVersion.requisition">
                <router-link
                  :to="{
                    name: 'RecruitmentRequisitionView',
                    params: { recruitmentRequisitionId: recruitmentRequisitionVersion.requisition.id },
                  }"
                  >{{ recruitmentRequisitionVersion.requisition.title }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{
                    name: 'RecruitmentRequisitionVersionView',
                    params: { recruitmentRequisitionVersionId: recruitmentRequisitionVersion.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{
                    name: 'RecruitmentRequisitionVersionEdit',
                    params: { recruitmentRequisitionVersionId: recruitmentRequisitionVersion.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(recruitmentRequisitionVersion)"
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
        <span id="hrappApp.recruitmentRequisitionVersion.delete.question" data-cy="recruitmentRequisitionVersionDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-recruitmentRequisitionVersion-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.recruitmentRequisitionVersion.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-recruitmentRequisitionVersion"
            data-cy="entityConfirmDeleteButton"
            @click="removeRecruitmentRequisitionVersion"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="recruitmentRequisitionVersions?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./recruitment-requisition-version.component.ts"></script>
