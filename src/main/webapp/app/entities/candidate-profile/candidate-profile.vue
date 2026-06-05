<template>
  <div>
    <h2 id="page-heading" data-cy="CandidateProfileHeading">
      <span id="candidate-profile">{{ $t('entity.candidateProfile.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'CandidateProfileCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-candidate-profile"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.candidateProfile.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && candidateProfiles?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.candidateProfile.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="candidateProfiles?.length > 0">
      <table class="table table-striped" aria-describedby="candidateProfiles">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('candidateDate')">
              <span>{{ $t('entity.candidateProfile.field.candidateDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'candidateDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('cultivateDirection')">
              <span>{{ $t('entity.candidateProfile.field.cultivateDirection') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cultivateDirection'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('stability')">
              <span>{{ $t('entity.candidateProfile.field.stability') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stability'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('learningAbility')">
              <span>{{ $t('entity.candidateProfile.field.learningAbility') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'learningAbility'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('communicationCoordination')">
              <span>Communication Coordination</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'communicationCoordination'"
              ></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('businessUnderstanding')">
              <span>{{ $t('entity.candidateProfile.field.businessUnderstanding') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'businessUnderstanding'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('responsibility')">
              <span>{{ $t('entity.candidateProfile.field.responsibility') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'responsibility'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskAwareness')">
              <span>{{ $t('entity.candidateProfile.field.riskAwareness') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskAwareness'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('judgement')">
              <span>{{ $t('entity.candidateProfile.field.judgement') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'judgement'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evidence')">
              <span>{{ $t('entity.candidateProfile.field.evidence') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evidence'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('person.personName')">
              <span>{{ $t('entity.person.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'person.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observer.personName')">
              <span>{{ $t('global.entity.field.observer') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observer.personName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="candidateProfile in candidateProfiles" :key="candidateProfile.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CandidateProfileView', params: { candidateProfileId: candidateProfile.id } }">{{
                candidateProfile.id
              }}</router-link>
            </td>
            <td>{{ candidateProfile.candidateDate }}</td>
            <td>{{ candidateProfile.cultivateDirection }}</td>
            <td>{{ candidateProfile.stability }}</td>
            <td>{{ candidateProfile.learningAbility }}</td>
            <td>{{ candidateProfile.communicationCoordination }}</td>
            <td>{{ candidateProfile.businessUnderstanding }}</td>
            <td>{{ candidateProfile.responsibility }}</td>
            <td>{{ candidateProfile.riskAwareness }}</td>
            <td>{{ candidateProfile.judgement }}</td>
            <td>{{ candidateProfile.evidence }}</td>
            <td>
              <div v-if="candidateProfile.person">
                <router-link :to="{ name: 'PersonView', params: { personId: candidateProfile.person.id } }">{{
                  candidateProfile.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="candidateProfile.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: candidateProfile.position.id } }">{{
                  candidateProfile.position.positionName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="candidateProfile.observer">
                <router-link :to="{ name: 'PersonView', params: { personId: candidateProfile.observer.id } }">{{
                  candidateProfile.observer.personName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CandidateProfileView', params: { candidateProfileId: candidateProfile.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'CandidateProfileEdit', params: { candidateProfileId: candidateProfile.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(candidateProfile)"
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
        <span id="hrappApp.candidateProfile.delete.question" data-cy="candidateProfileDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-candidateProfile-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.candidateProfile.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-candidateProfile"
            data-cy="entityConfirmDeleteButton"
            @click="removeCandidateProfile"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="candidateProfiles?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./candidate-profile.component.ts"></script>
