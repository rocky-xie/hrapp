<template>
  <div>
    <h2 id="page-heading" data-cy="DispatchAssignmentHeading">
      <span id="dispatch-assignment">{{ $t('entity.dispatchAssignment.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'DispatchAssignmentCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-dispatch-assignment"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.dispatchAssignment.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && dispatchAssignments?.length === 0">
      <span>{{ $t('entity.dispatchAssignment.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="dispatchAssignments?.length > 0">
      <table class="table table-striped" aria-describedby="dispatchAssignments">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('candidateId')">
              <span>{{ $t('recruitment.fields.candidateId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'candidateId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('personId')">
              <span>{{ $t('recruitment.fields.personId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'personId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('partnerOrganizationId')">
              <span>{{ $t('recruitment.fields.partnerOrganizationId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'partnerOrganizationId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('department')">
              <span>{{ $t('recruitment.fields.department') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'department'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('positionId')">
              <span>{{ $t('recruitment.fields.positionId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'positionId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('startDate')">
              <span>{{ $t('recruitment.fields.startDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('expectedEndDate')">
              <span>{{ $t('recruitment.fields.expectedEndDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'expectedEndDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observationDays')">
              <span>{{ $t('recruitment.fields.observationDays') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observationDays'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('status')">
              <span>{{ $t('recruitment.fields.status') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('finalDecision')">
              <span>{{ $t('recruitment.fields.finalDecision') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'finalDecision'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('note')">
              <span>{{ $t('recruitment.fields.note') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'note'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('application.id')">
              <span>{{ $t('recruitment.fields.application') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'application.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dispatchAssignment in dispatchAssignments" :key="dispatchAssignment.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DispatchAssignmentView', params: { dispatchAssignmentId: dispatchAssignment.id } }">{{
                dispatchAssignment.id
              }}</router-link>
            </td>
            <td>{{ dispatchAssignment.candidateId }}</td>
            <td>{{ dispatchAssignment.personId }}</td>
            <td>{{ dispatchAssignment.partnerOrganizationId }}</td>
            <td>{{ dispatchAssignment.department }}</td>
            <td>{{ dispatchAssignment.positionId }}</td>
            <td>{{ dispatchAssignment.startDate }}</td>
            <td>{{ dispatchAssignment.expectedEndDate }}</td>
            <td>{{ dispatchAssignment.observationDays }}</td>
            <td>{{ $t('recruitment.enums.' + dispatchAssignment.status) }}</td>
            <td>{{ dispatchAssignment.finalDecision ? $t('recruitment.enums.' + dispatchAssignment.finalDecision) : '—' }}</td>
            <td>{{ dispatchAssignment.note }}</td>
            <td>
              <div v-if="dispatchAssignment.application">
                <router-link
                  :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: dispatchAssignment.application.id } }"
                  >{{ dispatchAssignment.application.applicationCode }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'DispatchAssignmentView', params: { dispatchAssignmentId: dispatchAssignment.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'DispatchAssignmentEdit', params: { dispatchAssignmentId: dispatchAssignment.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(dispatchAssignment)"
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
        <span id="hrappApp.dispatchAssignment.delete.question" data-cy="dispatchAssignmentDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-dispatch-assignment-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.dispatchAssignment.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-dispatchAssignment"
            data-cy="entityConfirmDeleteButton"
            @click="removeDispatchAssignment"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="dispatchAssignments?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./dispatch-assignment.component.ts"></script>
