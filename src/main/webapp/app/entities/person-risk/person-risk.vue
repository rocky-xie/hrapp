<template>
  <div>
    <h2 id="page-heading" data-cy="PersonRiskHeading">
      <span id="person-risk">{{ $t('entity.personRisk.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'PersonRiskCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-person-risk"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.personRisk.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && personRisks?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.personRisk.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="personRisks?.length > 0">
      <table class="table table-striped" aria-describedby="personRisks">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskType')">
              <span>{{ $t('entity.personRisk.field.riskType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskLevel')">
              <span>{{ $t('entity.positionRiskEvaluation.field.riskLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskLevel'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskDescription')">
              <span>{{ $t('entity.personRisk.field.riskDescription') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskDescription'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('improvementAction')">
              <span>{{ $t('entity.improvementPlan.field.improvementAction') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'improvementAction'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('identifiedDate')">
              <span>{{ $t('entity.personRisk.field.identifiedDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'identifiedDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('targetDate')">
              <span>{{ $t('entity.improvementPlan.field.targetDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'targetDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('closedDate')">
              <span>{{ $t('entity.personRisk.field.closedDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'closedDate'"></jhi-sort-indicator>
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
          <tr v-for="personRisk in personRisks" :key="personRisk.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PersonRiskView', params: { personRiskId: personRisk.id } }">{{ personRisk.id }}</router-link>
            </td>
            <td>{{ personRisk.riskType }}</td>
            <td>{{ personRisk.riskLevel }}</td>
            <td>{{ personRisk.riskDescription }}</td>
            <td>{{ personRisk.improvementAction }}</td>
            <td>{{ personRisk.identifiedDate }}</td>
            <td>{{ personRisk.targetDate }}</td>
            <td>{{ personRisk.closedDate }}</td>
            <td>
              <div v-if="personRisk.person">
                <router-link :to="{ name: 'PersonView', params: { personId: personRisk.person.id } }">{{
                  personRisk.person.personName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="personRisk.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: personRisk.position.id } }">{{
                  personRisk.position.positionName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'PersonRiskView', params: { personRiskId: personRisk.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PersonRiskEdit', params: { personRiskId: personRisk.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(personRisk)"
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
        <span id="hrappApp.personRisk.delete.question" data-cy="personRiskDeleteDialogHeading">{{ $t('global.entity.deleteTitle') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-personRisk-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.personRisk.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-personRisk"
            data-cy="entityConfirmDeleteButton"
            @click="removePersonRisk"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="personRisks?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./person-risk.component.ts"></script>
