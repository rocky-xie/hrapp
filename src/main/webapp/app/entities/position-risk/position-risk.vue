<template>
  <div>
    <h2 id="page-heading" data-cy="PositionRiskHeading">
      <span id="position-risk">{{ $t('entity.positionRisk.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'PositionRiskCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-position-risk"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.positionRisk.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && positionRisks?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.positionRisk.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="positionRisks?.length > 0">
      <table class="table table-striped" aria-describedby="positionRisks">
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
            <th scope="col" @click="changeOrder('documentStatus')">
              <span>{{ $t('entity.positionRisk.field.documentStatus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'documentStatus'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('backupStatus')">
              <span>{{ $t('entity.positionRisk.field.backupStatus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'backupStatus'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('customerOrSystemDependency')">
              <span>{{ $t('entity.positionRisk.field.customerOrSystemDependency') }}</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'customerOrSystemDependency'"
              ></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('category.categoryName')">
              <span>{{ $t('global.entity.field.category') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'category.categoryName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="positionRisk in positionRisks" :key="positionRisk.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PositionRiskView', params: { positionRiskId: positionRisk.id } }">{{
                positionRisk.id
              }}</router-link>
            </td>
            <td>{{ positionRisk.riskType }}</td>
            <td>{{ positionRisk.riskLevel }}</td>
            <td>{{ positionRisk.documentStatus }}</td>
            <td>{{ positionRisk.backupStatus }}</td>
            <td>{{ positionRisk.customerOrSystemDependency }}</td>
            <td>{{ positionRisk.riskDescription }}</td>
            <td>{{ positionRisk.improvementAction }}</td>
            <td>{{ positionRisk.identifiedDate }}</td>
            <td>{{ positionRisk.targetDate }}</td>
            <td>{{ positionRisk.closedDate }}</td>
            <td>
              <div v-if="positionRisk.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: positionRisk.position.id } }">{{
                  positionRisk.position.positionName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="positionRisk.category">
                <router-link
                  :to="{ name: 'KeyResponsibilityCategoryView', params: { keyResponsibilityCategoryId: positionRisk.category.id } }"
                  >{{ positionRisk.category.categoryName }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'PositionRiskView', params: { positionRiskId: positionRisk.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PositionRiskEdit', params: { positionRiskId: positionRisk.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(positionRisk)"
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
        <span id="hrappApp.positionRisk.delete.question" data-cy="positionRiskDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-positionRisk-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.positionRisk.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-positionRisk"
            data-cy="entityConfirmDeleteButton"
            @click="removePositionRisk"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="positionRisks?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./position-risk.component.ts"></script>
