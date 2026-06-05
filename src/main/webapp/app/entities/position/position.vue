<template>
  <div>
    <h2 id="page-heading" data-cy="PositionHeading">
      <span id="position">{{ $t('entity.position.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'PositionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-position"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.position.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && positions?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.position.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="positions?.length > 0">
      <table class="table table-striped" aria-describedby="positions">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('positionCode')">
              <span>{{ $t('entity.position.field.positionCode') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'positionCode'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('positionName')">
              <span>{{ $t('entity.position.field.positionName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('positionType')">
              <span>{{ $t('entity.position.field.positionType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'positionType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('businessImportance')">
              <span>{{ $t('entity.position.field.businessImportance') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'businessImportance'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('keyPosition')">
              <span>{{ $t('entity.position.field.keyPosition') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'keyPosition'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('description')">
              <span>{{ $t('entity.skill.field.description') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('plannedHeadcount')">
              <span>{{ $t('entity.position.field.plannedHeadcount') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'plannedHeadcount'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('minimumOwnerCount')">
              <span>{{ $t('entity.position.field.minimumOwnerCount') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'minimumOwnerCount'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reviewCycle')">
              <span>{{ $t('entity.position.field.reviewCycle') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reviewCycle'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('active')">
              <span>{{ $t('entity.positionAssignment.field.active') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'active'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="position in positions" :key="position.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PositionView', params: { positionId: position.id } }">{{ position.id }}</router-link>
            </td>
            <td>{{ position.positionCode }}</td>
            <td>{{ position.positionName }}</td>
            <td>{{ position.positionType }}</td>
            <td>{{ position.businessImportance }}</td>
            <td>{{ position.keyPosition }}</td>
            <td>{{ position.description }}</td>
            <td>{{ position.plannedHeadcount }}</td>
            <td>{{ position.minimumOwnerCount }}</td>
            <td>{{ position.reviewCycle }}</td>
            <td>{{ position.active }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'PositionView', params: { positionId: position.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PositionEdit', params: { positionId: position.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(position)"
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
        <span id="hrappApp.position.delete.question" data-cy="positionDeleteDialogHeading">{{ $t('global.entity.deleteTitle') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-position-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.position.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-position"
            data-cy="entityConfirmDeleteButton"
            @click="removePosition"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="positions?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./position.component.ts"></script>
