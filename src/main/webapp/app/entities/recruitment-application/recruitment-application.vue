<template>
  <div>
    <h2 id="page-heading" data-cy="RecruitmentApplicationHeading">
      <span id="recruitment-application">{{ $t('entity.recruitmentApplication.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'RecruitmentApplicationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-recruitment-application"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.recruitmentApplication.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && recruitmentApplications?.length === 0">
      <span>{{ $t('entity.recruitmentApplication.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="recruitmentApplications?.length > 0">
      <table class="table table-striped" aria-describedby="recruitmentApplications">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('applicationCode')">
              <span>{{ $t('recruitment.fields.applicationCode') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'applicationCode'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('recruitmentType')">
              <span>{{ $t('recruitment.fields.recruitmentType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'recruitmentType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('currentStage')">
              <span>{{ $t('recruitment.fields.currentStage') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'currentStage'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('status')">
              <span>{{ $t('recruitment.fields.status') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('decision')">
              <span>{{ $t('recruitment.fields.decision') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'decision'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sourceType')">
              <span>{{ $t('recruitment.fields.sourceType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sourceType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sourceDetail')">
              <span>{{ $t('recruitment.fields.sourceDetail') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sourceDetail'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('batchName')">
              <span>{{ $t('recruitment.fields.batchName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'batchName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('appliedAt')">
              <span>{{ $t('recruitment.fields.appliedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'appliedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('closedAt')">
              <span>{{ $t('recruitment.fields.closedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'closedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('retentionUntil')">
              <span>{{ $t('recruitment.fields.retentionUntil') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'retentionUntil'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('convertedPersonId')">
              <span>{{ $t('recruitment.fields.convertedPersonId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'convertedPersonId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('convertedAt')">
              <span>{{ $t('recruitment.fields.convertedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'convertedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('closeReason')">
              <span>{{ $t('recruitment.fields.closeReason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'closeReason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('note')">
              <span>{{ $t('recruitment.fields.note') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'note'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('candidate.id')">
              <span>{{ $t('recruitment.fields.candidate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'candidate.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('requisition.id')">
              <span>{{ $t('recruitment.fields.requisition') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'requisition.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('requisitionVersion.id')">
              <span>{{ $t('recruitment.fields.requisitionVersion') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'requisitionVersion.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="recruitmentApplication in recruitmentApplications" :key="recruitmentApplication.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: recruitmentApplication.id } }">{{
                recruitmentApplication.id
              }}</router-link>
            </td>
            <td>{{ recruitmentApplication.applicationCode }}</td>
            <td>{{ $t('recruitment.enums.' + recruitmentApplication.recruitmentType) }}</td>
            <td>{{ recruitmentApplication.currentStage ? $t('recruitment.enums.' + recruitmentApplication.currentStage) : '—' }}</td>
            <td>{{ $t('recruitment.enums.' + recruitmentApplication.status) }}</td>
            <td>{{ recruitmentApplication.decision ? $t('recruitment.enums.' + recruitmentApplication.decision) : '—' }}</td>
            <td>{{ recruitmentApplication.sourceType ? $t('recruitment.enums.' + recruitmentApplication.sourceType) : '—' }}</td>
            <td>{{ recruitmentApplication.sourceDetail }}</td>
            <td>{{ recruitmentApplication.batchName }}</td>
            <td>{{ formatDateShort(recruitmentApplication.appliedAt) || '' }}</td>
            <td>{{ formatDateShort(recruitmentApplication.closedAt) || '' }}</td>
            <td>{{ recruitmentApplication.retentionUntil }}</td>
            <td>{{ recruitmentApplication.convertedPersonId }}</td>
            <td>{{ formatDateShort(recruitmentApplication.convertedAt) || '' }}</td>
            <td>{{ recruitmentApplication.closeReason }}</td>
            <td>{{ recruitmentApplication.note }}</td>
            <td>
              <div v-if="recruitmentApplication.candidate">
                <router-link
                  :to="{ name: 'ExternalCandidateView', params: { externalCandidateId: recruitmentApplication.candidate.id } }"
                  >{{ recruitmentApplication.candidate.fullName }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="recruitmentApplication.requisition">
                <router-link
                  :to="{ name: 'RecruitmentRequisitionView', params: { recruitmentRequisitionId: recruitmentApplication.requisition.id } }"
                  >{{ recruitmentApplication.requisition.title }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="recruitmentApplication.requisitionVersion">
                <router-link
                  :to="{
                    name: 'RecruitmentRequisitionVersionView',
                    params: { recruitmentRequisitionVersionId: recruitmentApplication.requisitionVersion.id },
                  }"
                  >{{ recruitmentApplication.requisitionVersion.versionNumber }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: recruitmentApplication.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'RecruitmentApplicationEdit', params: { recruitmentApplicationId: recruitmentApplication.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(recruitmentApplication)"
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
        <span id="hrappApp.recruitmentApplication.delete.question" data-cy="recruitmentApplicationDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-recruitment-application-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.recruitmentApplication.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-recruitmentApplication"
            data-cy="entityConfirmDeleteButton"
            @click="removeRecruitmentApplication"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="recruitmentApplications?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./recruitment-application.component.ts"></script>
