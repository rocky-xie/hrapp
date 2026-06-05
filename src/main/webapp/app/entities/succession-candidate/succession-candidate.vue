<template>
  <div>
    <h2 id="page-heading" data-cy="SuccessionCandidateHeading">
      <span id="succession-candidate">{{ $t('entity.successionCandidate.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'SuccessionCandidateCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-succession-candidate"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.successionCandidate.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && successionCandidates?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.successionCandidate.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="successionCandidates?.length > 0">
      <table class="table table-striped" aria-describedby="successionCandidates">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('successionReadiness')">
              <span>{{ $t('entity.successionCandidate.field.successionReadiness') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'successionReadiness'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('requiredTraining')">
              <span>{{ $t('entity.successionCandidate.field.requiredTraining') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'requiredTraining'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('estimatedTimeToReady')">
              <span>{{ $t('entity.successionCandidate.field.estimatedTimeToReady') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estimatedTimeToReady'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskAfterTraining')">
              <span>{{ $t('entity.successionCandidate.field.riskAfterTraining') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskAfterTraining'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reviewDate')">
              <span>{{ $t('entity.successionCandidate.field.reviewDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reviewDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('priority')">
              <span>{{ $t('entity.successionCandidate.field.priority') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priority'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('currentOwner.personName')">
              <span>{{ $t('global.entity.field.currentOwner') }}</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'currentOwner.personName'"
              ></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('candidate.personName')">
              <span>{{ $t('global.entity.field.candidate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'candidate.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="successionCandidate in successionCandidates" :key="successionCandidate.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SuccessionCandidateView', params: { successionCandidateId: successionCandidate.id } }">{{
                successionCandidate.id
              }}</router-link>
            </td>
            <td>{{ successionCandidate.successionReadiness }}</td>
            <td>{{ successionCandidate.requiredTraining }}</td>
            <td>{{ successionCandidate.estimatedTimeToReady }}</td>
            <td>{{ successionCandidate.riskAfterTraining }}</td>
            <td>{{ successionCandidate.reviewDate }}</td>
            <td>{{ successionCandidate.priority }}</td>
            <td>
              <div v-if="successionCandidate.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: successionCandidate.position.id } }">{{
                  successionCandidate.position.positionName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="successionCandidate.currentOwner">
                <router-link :to="{ name: 'PersonView', params: { personId: successionCandidate.currentOwner.id } }">{{
                  successionCandidate.currentOwner.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="successionCandidate.candidate">
                <router-link :to="{ name: 'PersonView', params: { personId: successionCandidate.candidate.id } }">{{
                  successionCandidate.candidate.personName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SuccessionCandidateView', params: { successionCandidateId: successionCandidate.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SuccessionCandidateEdit', params: { successionCandidateId: successionCandidate.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(successionCandidate)"
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
        <span id="hrappApp.successionCandidate.delete.question" data-cy="successionCandidateDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-successionCandidate-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.successionCandidate.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-successionCandidate"
            data-cy="entityConfirmDeleteButton"
            @click="removeSuccessionCandidate"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="successionCandidates?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./succession-candidate.component.ts"></script>
