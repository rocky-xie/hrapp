<template>
  <div>
    <h2 id="page-heading" data-cy="PreboardingFollowUpHeading">
      <span id="preboarding-follow-up">{{ $t('entity.preboardingFollowUp.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'PreboardingFollowUpCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-preboarding-follow-up"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.preboardingFollowUp.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && preboardingFollowUps?.length === 0">
      <span>{{ $t('entity.preboardingFollowUp.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="preboardingFollowUps?.length > 0">
      <table class="table table-striped" aria-describedby="preboardingFollowUps">
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
            <th scope="col" @click="changeOrder('plannedAt')">
              <span>{{ $t('recruitment.fields.plannedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'plannedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('contactedAt')">
              <span>{{ $t('recruitment.fields.contactedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'contactedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('ownerUsername')">
              <span>{{ $t('recruitment.fields.ownerUsername') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ownerUsername'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('contactMethod')">
              <span>{{ $t('recruitment.fields.contactMethod') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'contactMethod'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('result')">
              <span>{{ $t('recruitment.fields.result') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'result'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('candidateIntent')">
              <span>{{ $t('recruitment.fields.candidateIntent') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'candidateIntent'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskLevel')">
              <span>{{ $t('recruitment.fields.riskLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskLevel'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nextAction')">
              <span>{{ $t('recruitment.fields.nextAction') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nextAction'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nextContactAt')">
              <span>{{ $t('recruitment.fields.nextContactAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nextContactAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('application.id')">
              <span>{{ $t('recruitment.fields.application') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'application.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="preboardingFollowUp in preboardingFollowUps" :key="preboardingFollowUp.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PreboardingFollowUpView', params: { preboardingFollowUpId: preboardingFollowUp.id } }">{{
                preboardingFollowUp.id
              }}</router-link>
            </td>
            <td>{{ preboardingFollowUp.candidateId }}</td>
            <td>{{ preboardingFollowUp.personId }}</td>
            <td>{{ formatDateShort(preboardingFollowUp.plannedAt) || '' }}</td>
            <td>{{ formatDateShort(preboardingFollowUp.contactedAt) || '' }}</td>
            <td>{{ preboardingFollowUp.ownerUsername }}</td>
            <td>{{ $t('recruitment.enums.' + preboardingFollowUp.contactMethod) }}</td>
            <td>{{ $t('recruitment.enums.' + preboardingFollowUp.result) }}</td>
            <td>{{ $t('recruitment.enums.' + preboardingFollowUp.candidateIntent) }}</td>
            <td>{{ $t('recruitment.enums.' + preboardingFollowUp.riskLevel) }}</td>
            <td>{{ preboardingFollowUp.nextAction }}</td>
            <td>{{ formatDateShort(preboardingFollowUp.nextContactAt) || '' }}</td>
            <td>
              <div v-if="preboardingFollowUp.application">
                <router-link
                  :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: preboardingFollowUp.application.id } }"
                  >{{ preboardingFollowUp.application.applicationCode }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PreboardingFollowUpView', params: { preboardingFollowUpId: preboardingFollowUp.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PreboardingFollowUpEdit', params: { preboardingFollowUpId: preboardingFollowUp.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(preboardingFollowUp)"
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
        <span id="hrappApp.preboardingFollowUp.delete.question" data-cy="preboardingFollowUpDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-preboarding-follow-up-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.preboardingFollowUp.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-preboardingFollowUp"
            data-cy="entityConfirmDeleteButton"
            @click="removePreboardingFollowUp"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="preboardingFollowUps?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./preboarding-follow-up.component.ts"></script>
