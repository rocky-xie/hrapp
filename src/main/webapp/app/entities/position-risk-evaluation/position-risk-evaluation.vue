<template>
  <div>
    <h2 id="page-heading" data-cy="PositionRiskEvaluationHeading">
      <span id="position-risk-evaluation">{{ $t('entity.positionRiskEvaluation.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'PositionRiskEvaluationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-position-risk-evaluation"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.positionRiskEvaluation.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && positionRiskEvaluations?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.positionRiskEvaluation.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="positionRiskEvaluations?.length > 0">
      <table class="table table-striped" aria-describedby="positionRiskEvaluations">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evaluationDate')">
              <span>{{ $t('entity.evaluation.field.evaluationDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evaluationDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('ownerCount')">
              <span>{{ $t('entity.positionRiskEvaluation.field.ownerCount') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ownerCount'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('substitutableOwnerCount')">
              <span>{{ $t('entity.positionRiskEvaluation.field.substitutableOwnerCount') }}</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'substitutableOwnerCount'"
              ></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('hasSubstitute')">
              <span>{{ $t('entity.positionRiskEvaluation.field.hasSubstitute') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'hasSubstitute'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('documentStatus')">
              <span>{{ $t('entity.positionRisk.field.documentStatus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'documentStatus'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('customerOrSystemDependency')">
              <span>{{ $t('entity.positionRisk.field.customerOrSystemDependency') }}</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'customerOrSystemDependency'"
              ></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('successionReadiness')">
              <span>{{ $t('entity.successionCandidate.field.successionReadiness') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'successionReadiness'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskLevel')">
              <span>{{ $t('entity.positionRiskEvaluation.field.riskLevel') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskLevel'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('riskReason')">
              <span>{{ $t('entity.positionRiskEvaluation.field.riskReason') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'riskReason'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('recommendedAction')">
              <span>{{ $t('entity.positionRiskEvaluation.field.recommendedAction') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'recommendedAction'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="positionRiskEvaluation in positionRiskEvaluations" :key="positionRiskEvaluation.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PositionRiskEvaluationView', params: { positionRiskEvaluationId: positionRiskEvaluation.id } }">{{
                positionRiskEvaluation.id
              }}</router-link>
            </td>
            <td>{{ positionRiskEvaluation.evaluationDate }}</td>
            <td>{{ positionRiskEvaluation.ownerCount }}</td>
            <td>{{ positionRiskEvaluation.substitutableOwnerCount }}</td>
            <td>{{ positionRiskEvaluation.hasSubstitute }}</td>
            <td>{{ positionRiskEvaluation.documentStatus }}</td>
            <td>{{ positionRiskEvaluation.customerOrSystemDependency }}</td>
            <td>{{ positionRiskEvaluation.successionReadiness }}</td>
            <td>{{ positionRiskEvaluation.riskLevel }}</td>
            <td>{{ positionRiskEvaluation.riskReason }}</td>
            <td>{{ positionRiskEvaluation.recommendedAction }}</td>
            <td>
              <div v-if="positionRiskEvaluation.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: positionRiskEvaluation.position.id } }">{{
                  positionRiskEvaluation.position.positionName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PositionRiskEvaluationView', params: { positionRiskEvaluationId: positionRiskEvaluation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PositionRiskEvaluationEdit', params: { positionRiskEvaluationId: positionRiskEvaluation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(positionRiskEvaluation)"
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
        <span id="hrappApp.positionRiskEvaluation.delete.question" data-cy="positionRiskEvaluationDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-positionRiskEvaluation-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.positionRiskEvaluation.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-positionRiskEvaluation"
            data-cy="entityConfirmDeleteButton"
            @click="removePositionRiskEvaluation"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="positionRiskEvaluations?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./position-risk-evaluation.component.ts"></script>
