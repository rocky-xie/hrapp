<template>
  <div>
    <h2 id="page-heading" data-cy="ExternalCandidateHeading">
      <span id="external-candidate">{{ $t('entity.externalCandidate.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'ExternalCandidateCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-external-candidate"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.externalCandidate.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && externalCandidates?.length === 0">
      <span>{{ $t('entity.externalCandidate.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="externalCandidates?.length > 0">
      <table class="table table-striped" aria-describedby="externalCandidates">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fullName')">
              <span>{{ $t('recruitment.fields.fullName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fullName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nameReading')">
              <span>{{ $t('recruitment.fields.nameReading') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameReading'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('englishName')">
              <span>{{ $t('recruitment.fields.englishName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'englishName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('birthDate')">
              <span>{{ $t('recruitment.fields.birthDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'birthDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('gender')">
              <span>{{ $t('recruitment.fields.gender') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'gender'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('phone')">
              <span>{{ $t('recruitment.fields.phone') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'phone'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('email')">
              <span>{{ $t('recruitment.fields.email') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('address')">
              <span>{{ $t('recruitment.fields.address') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'address'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('educationSummary')">
              <span>{{ $t('recruitment.fields.educationSummary') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'educationSummary'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('workSummary')">
              <span>{{ $t('recruitment.fields.workSummary') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'workSummary'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('convertedPersonId')">
              <span>{{ $t('recruitment.fields.convertedPersonId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'convertedPersonId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('createdAt')">
              <span>{{ $t('recruitment.fields.createdAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('updatedAt')">
              <span>{{ $t('recruitment.fields.updatedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'updatedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="externalCandidate in externalCandidates" :key="externalCandidate.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ExternalCandidateView', params: { externalCandidateId: externalCandidate.id } }">{{
                externalCandidate.id
              }}</router-link>
            </td>
            <td>{{ externalCandidate.fullName }}</td>
            <td>{{ externalCandidate.nameReading }}</td>
            <td>{{ externalCandidate.englishName }}</td>
            <td>{{ externalCandidate.birthDate }}</td>
            <td>{{ externalCandidate.gender }}</td>
            <td>{{ externalCandidate.phone }}</td>
            <td>{{ externalCandidate.email }}</td>
            <td>{{ externalCandidate.address }}</td>
            <td>{{ externalCandidate.educationSummary }}</td>
            <td>{{ externalCandidate.workSummary }}</td>
            <td>{{ externalCandidate.convertedPersonId }}</td>
            <td>{{ formatDateShort(externalCandidate.createdAt) || '' }}</td>
            <td>{{ formatDateShort(externalCandidate.updatedAt) || '' }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ExternalCandidateView', params: { externalCandidateId: externalCandidate.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ExternalCandidateEdit', params: { externalCandidateId: externalCandidate.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(externalCandidate)"
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
        <span id="hrappApp.externalCandidate.delete.question" data-cy="externalCandidateDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-external-candidate-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.externalCandidate.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-externalCandidate"
            data-cy="entityConfirmDeleteButton"
            @click="removeExternalCandidate"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="externalCandidates?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./external-candidate.component.ts"></script>
