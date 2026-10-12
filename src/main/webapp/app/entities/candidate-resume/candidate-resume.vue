<template>
  <div>
    <h2 id="page-heading" data-cy="CandidateResumeHeading">
      <span id="candidate-resume">{{ $t('entity.candidateResume.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'CandidateResumeCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-candidate-resume"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.candidateResume.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && candidateResumes?.length === 0">
      <span>{{ $t('entity.candidateResume.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="candidateResumes?.length > 0">
      <table class="table table-striped" aria-describedby="candidateResumes">
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
            <th scope="col" @click="changeOrder('receivedAt')">
              <span>{{ $t('recruitment.fields.receivedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'receivedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('originalFileName')">
              <span>{{ $t('recruitment.fields.originalFileName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'originalFileName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('mediaType')">
              <span>{{ $t('recruitment.fields.mediaType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mediaType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fileSize')">
              <span>{{ $t('recruitment.fields.fileSize') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fileSize'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sha256')">
              <span>{{ $t('recruitment.fields.sha256') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sha256'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('storageKey')">
              <span>{{ $t('recruitment.fields.storageKey') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'storageKey'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('structuredSummary')">
              <span>{{ $t('recruitment.fields.structuredSummary') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'structuredSummary'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('activeVersion')">
              <span>{{ $t('recruitment.fields.activeVersion') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activeVersion'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('uploadedBy')">
              <span>{{ $t('recruitment.fields.uploadedBy') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'uploadedBy'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('candidate.id')">
              <span>{{ $t('recruitment.fields.candidate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'candidate.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="candidateResume in candidateResumes" :key="candidateResume.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CandidateResumeView', params: { candidateResumeId: candidateResume.id } }">{{
                candidateResume.id
              }}</router-link>
            </td>
            <td>{{ candidateResume.versionNumber }}</td>
            <td>{{ formatDateShort(candidateResume.receivedAt) || '' }}</td>
            <td>{{ candidateResume.originalFileName }}</td>
            <td>{{ candidateResume.mediaType }}</td>
            <td>{{ candidateResume.fileSize }}</td>
            <td>{{ candidateResume.sha256 }}</td>
            <td>{{ candidateResume.storageKey }}</td>
            <td>{{ candidateResume.structuredSummary }}</td>
            <td>{{ candidateResume.activeVersion }}</td>
            <td>{{ candidateResume.uploadedBy }}</td>
            <td>
              <div v-if="candidateResume.candidate">
                <router-link :to="{ name: 'ExternalCandidateView', params: { externalCandidateId: candidateResume.candidate.id } }">{{
                  candidateResume.candidate.fullName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CandidateResumeView', params: { candidateResumeId: candidateResume.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'CandidateResumeEdit', params: { candidateResumeId: candidateResume.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(candidateResume)"
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
        <span id="hrappApp.candidateResume.delete.question" data-cy="candidateResumeDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-candidate-resume-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.candidateResume.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-candidateResume"
            data-cy="entityConfirmDeleteButton"
            @click="removeCandidateResume"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="candidateResumes?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./candidate-resume.component.ts"></script>
