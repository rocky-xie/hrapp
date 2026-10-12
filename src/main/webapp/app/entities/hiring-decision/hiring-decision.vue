<template>
  <div>
    <h2 id="page-heading" data-cy="HiringDecisionHeading">
      <span id="hiring-decision">{{ $t('entity.hiringDecision.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'HiringDecisionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-hiring-decision"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.hiringDecision.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && hiringDecisions?.length === 0">
      <span>{{ $t('entity.hiringDecision.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="hiringDecisions?.length > 0">
      <table class="table table-striped" aria-describedby="hiringDecisions">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('decision')">
              <span>{{ $t('recruitment.fields.decision') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'decision'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('decisionSource')">
              <span>{{ $t('recruitment.fields.decisionSource') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'decisionSource'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('decidedBy')">
              <span>{{ $t('recruitment.fields.decidedBy') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'decidedBy'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('decidedAt')">
              <span>{{ $t('recruitment.fields.decidedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'decidedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reason')">
              <span>{{ $t('recruitment.fields.reason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('correctionOfId')">
              <span>{{ $t('recruitment.fields.correctionOfId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'correctionOfId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('active')">
              <span>{{ $t('recruitment.fields.active') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'active'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('application.id')">
              <span>{{ $t('recruitment.fields.application') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'application.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="hiringDecision in hiringDecisions" :key="hiringDecision.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'HiringDecisionView', params: { hiringDecisionId: hiringDecision.id } }">{{
                hiringDecision.id
              }}</router-link>
            </td>
            <td>{{ $t('recruitment.enums.' + hiringDecision.decision) }}</td>
            <td>{{ hiringDecision.decisionSource }}</td>
            <td>{{ hiringDecision.decidedBy }}</td>
            <td>{{ formatDateShort(hiringDecision.decidedAt) || '' }}</td>
            <td>{{ hiringDecision.reason }}</td>
            <td>{{ hiringDecision.correctionOfId }}</td>
            <td>{{ hiringDecision.active }}</td>
            <td>
              <div v-if="hiringDecision.application">
                <router-link
                  :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: hiringDecision.application.id } }"
                  >{{ hiringDecision.application.applicationCode }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'HiringDecisionView', params: { hiringDecisionId: hiringDecision.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'HiringDecisionEdit', params: { hiringDecisionId: hiringDecision.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(hiringDecision)"
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
        <span id="hrappApp.hiringDecision.delete.question" data-cy="hiringDecisionDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-hiring-decision-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.hiringDecision.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-hiringDecision"
            data-cy="entityConfirmDeleteButton"
            @click="removeHiringDecision"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="hiringDecisions?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./hiring-decision.component.ts"></script>
