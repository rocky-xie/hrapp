<template>
  <div>
    <h2 id="page-heading" data-cy="PartnerOrganizationHeading">
      <span id="partner-organization">{{ $t('entity.partnerOrganization.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'PartnerOrganizationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-partner-organization"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.partnerOrganization.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && partnerOrganizations?.length === 0">
      <span>{{ $t('entity.partnerOrganization.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="partnerOrganizations?.length > 0">
      <table class="table table-striped" aria-describedby="partnerOrganizations">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('partnerCode')">
              <span>{{ $t('recruitment.fields.partnerCode') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'partnerCode'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('name')">
              <span>{{ $t('recruitment.fields.name') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('rolesJson')">
              <span>{{ $t('recruitment.fields.rolesJson') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'rolesJson'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('dispatchEnabled')">
              <span>{{ $t('recruitment.fields.dispatchEnabled') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dispatchEnabled'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('defaultObservationDays')">
              <span>{{ $t('recruitment.fields.defaultObservationDays') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'defaultObservationDays'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('active')">
              <span>{{ $t('recruitment.fields.active') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'active'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="partnerOrganization in partnerOrganizations" :key="partnerOrganization.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PartnerOrganizationView', params: { partnerOrganizationId: partnerOrganization.id } }">{{
                partnerOrganization.id
              }}</router-link>
            </td>
            <td>{{ partnerOrganization.partnerCode }}</td>
            <td>{{ partnerOrganization.name }}</td>
            <td>{{ partnerOrganization.rolesJson }}</td>
            <td>{{ partnerOrganization.dispatchEnabled }}</td>
            <td>{{ partnerOrganization.defaultObservationDays }}</td>
            <td>{{ partnerOrganization.active }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PartnerOrganizationView', params: { partnerOrganizationId: partnerOrganization.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PartnerOrganizationEdit', params: { partnerOrganizationId: partnerOrganization.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(partnerOrganization)"
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
        <span id="hrappApp.partnerOrganization.delete.question" data-cy="partnerOrganizationDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-partner-organization-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.partnerOrganization.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-partnerOrganization"
            data-cy="entityConfirmDeleteButton"
            @click="removePartnerOrganization"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="partnerOrganizations?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./partner-organization.component.ts"></script>
