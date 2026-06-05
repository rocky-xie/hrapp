<template>
  <div>
    <h2 id="page-heading" data-cy="PositionAssignmentHeading">
      <span id="position-assignment">{{ $t('entity.positionAssignment.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'PositionAssignmentCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-position-assignment"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.positionAssignment.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && positionAssignments?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.positionAssignment.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="positionAssignments?.length > 0">
      <table class="table table-striped" aria-describedby="positionAssignments">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('primaryOwner')">
              <span>{{ $t('entity.positionAssignment.field.primaryOwner') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'primaryOwner'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('startDate')">
              <span>{{ $t('entity.improvementPlan.field.startDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('endDate')">
              <span>{{ $t('entity.positionAssignment.field.endDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('responsibilityScope')">
              <span>{{ $t('entity.positionAssignment.field.responsibilityScope') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'responsibilityScope'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('active')">
              <span>{{ $t('entity.positionAssignment.field.active') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'active'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('person.personName')">
              <span>{{ $t('entity.person.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'person.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="positionAssignment in positionAssignments" :key="positionAssignment.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PositionAssignmentView', params: { positionAssignmentId: positionAssignment.id } }">{{
                positionAssignment.id
              }}</router-link>
            </td>
            <td>{{ positionAssignment.primaryOwner }}</td>
            <td>{{ positionAssignment.startDate }}</td>
            <td>{{ positionAssignment.endDate }}</td>
            <td>{{ positionAssignment.responsibilityScope }}</td>
            <td>{{ positionAssignment.active }}</td>
            <td>
              <div v-if="positionAssignment.person">
                <router-link :to="{ name: 'PersonView', params: { personId: positionAssignment.person.id } }">{{
                  positionAssignment.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="positionAssignment.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: positionAssignment.position.id } }">{{
                  positionAssignment.position.positionName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PositionAssignmentView', params: { positionAssignmentId: positionAssignment.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PositionAssignmentEdit', params: { positionAssignmentId: positionAssignment.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(positionAssignment)"
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
        <span id="hrappApp.positionAssignment.delete.question" data-cy="positionAssignmentDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-positionAssignment-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.positionAssignment.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-positionAssignment"
            data-cy="entityConfirmDeleteButton"
            @click="removePositionAssignment"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="positionAssignments?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./position-assignment.component.ts"></script>
