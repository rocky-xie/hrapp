<template>
  <div>
    <h2 id="page-heading" data-cy="TrustObservationHeading">
      <span id="trust-observation">{{ $t('entity.trustObservation.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'TrustObservationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-trust-observation"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.trustObservation.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && trustObservations?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.trustObservation.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="trustObservations?.length > 0">
      <table class="table table-striped" aria-describedby="trustObservations">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observationDate')">
              <span>{{ $t('entity.trustObservation.field.observationDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observationDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('trustStage')">
              <span>{{ $t('entity.trustObservation.field.trustStage') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'trustStage'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observedBehavior')">
              <span>{{ $t('entity.trustObservation.field.observedBehavior') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observedBehavior'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('positiveSignal')">
              <span>{{ $t('entity.trustObservation.field.positiveSignal') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'positiveSignal'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskSignal')">
              <span>{{ $t('entity.trustObservation.field.riskSignal') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskSignal'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nextObservationPoint')">
              <span>{{ $t('entity.trustObservation.field.nextObservationPoint') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nextObservationPoint'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('person.personName')">
              <span>{{ $t('entity.person.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'person.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observer.personName')">
              <span>{{ $t('global.entity.field.observer') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observer.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="trustObservation in trustObservations" :key="trustObservation.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TrustObservationView', params: { trustObservationId: trustObservation.id } }">{{
                trustObservation.id
              }}</router-link>
            </td>
            <td>{{ trustObservation.observationDate }}</td>
            <td>{{ trustObservation.trustStage }}</td>
            <td>{{ trustObservation.observedBehavior }}</td>
            <td>{{ trustObservation.positiveSignal }}</td>
            <td>{{ trustObservation.riskSignal }}</td>
            <td>{{ trustObservation.nextObservationPoint }}</td>
            <td>
              <div v-if="trustObservation.person">
                <router-link :to="{ name: 'PersonView', params: { personId: trustObservation.person.id } }">{{
                  trustObservation.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trustObservation.observer">
                <router-link :to="{ name: 'PersonView', params: { personId: trustObservation.observer.id } }">{{
                  trustObservation.observer.personName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'TrustObservationView', params: { trustObservationId: trustObservation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'TrustObservationEdit', params: { trustObservationId: trustObservation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(trustObservation)"
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
        <span id="hrappApp.trustObservation.delete.question" data-cy="trustObservationDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-trustObservation-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.trustObservation.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-trustObservation"
            data-cy="entityConfirmDeleteButton"
            @click="removeTrustObservation"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="trustObservations?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./trust-observation.component.ts"></script>
