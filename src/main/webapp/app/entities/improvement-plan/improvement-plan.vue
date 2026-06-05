<template>
  <div>
    <h2 id="page-heading" data-cy="ImprovementPlanHeading">
      <span id="improvement-plan">{{ $t('entity.improvementPlan.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('global.form.refreshList') }}</span>
        </button>
        <router-link :to="{ name: 'ImprovementPlanCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-improvement-plan"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.improvementPlan.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && improvementPlans?.length === 0">
      <span>{{ $t('global.entity.noRecords', { entity: $t('entity.improvementPlan.home.title') }) }}</span>
    </div>
    <div class="table-responsive" v-if="improvementPlans?.length > 0">
      <table class="table table-striped" aria-describedby="improvementPlans">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('global.entity.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('planName')">
              <span>{{ $t('entity.improvementPlan.field.planName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'planName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('planStatus')">
              <span>{{ $t('entity.improvementPlan.field.planStatus') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'planStatus'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('problemSummary')">
              <span>{{ $t('entity.improvementPlan.field.problemSummary') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'problemSummary'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('improvementAction')">
              <span>{{ $t('entity.improvementPlan.field.improvementAction') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'improvementAction'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('ownerName')">
              <span>{{ $t('entity.improvementPlan.field.ownerName') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ownerName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('startDate')">
              <span>{{ $t('entity.improvementPlan.field.startDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('targetDate')">
              <span>{{ $t('entity.improvementPlan.field.targetDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'targetDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('completionDate')">
              <span>{{ $t('entity.improvementPlan.field.completionDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'completionDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reviewResult')">
              <span>{{ $t('entity.improvementPlan.field.reviewResult') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reviewResult'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('position.positionName')">
              <span>{{ $t('entity.position.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'position.positionName'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('skill.skillName')">
              <span>{{ $t('entity.skill.detail.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'skill.skillName'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="improvementPlan in improvementPlans" :key="improvementPlan.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ImprovementPlanView', params: { improvementPlanId: improvementPlan.id } }">{{
                improvementPlan.id
              }}</router-link>
            </td>
            <td>{{ improvementPlan.planName }}</td>
            <td>{{ improvementPlan.planStatus }}</td>
            <td>{{ improvementPlan.problemSummary }}</td>
            <td>{{ improvementPlan.improvementAction }}</td>
            <td>{{ improvementPlan.ownerName }}</td>
            <td>{{ improvementPlan.startDate }}</td>
            <td>{{ improvementPlan.targetDate }}</td>
            <td>{{ improvementPlan.completionDate }}</td>
            <td>{{ improvementPlan.reviewResult }}</td>
            <td>
              <div v-if="improvementPlan.position">
                <router-link :to="{ name: 'PositionView', params: { positionId: improvementPlan.position.id } }">{{
                  improvementPlan.position.positionName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="improvementPlan.skill">
                <router-link :to="{ name: 'SkillView', params: { skillId: improvementPlan.skill.id } }">{{
                  improvementPlan.skill.skillName
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ImprovementPlanView', params: { improvementPlanId: improvementPlan.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ImprovementPlanEdit', params: { improvementPlanId: improvementPlan.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('global.form.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(improvementPlan)"
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
        <span id="hrappApp.improvementPlan.delete.question" data-cy="improvementPlanDeleteDialogHeading">{{
          $t('global.entity.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-improvementPlan-heading">
          {{ $t('global.entity.deleteQuestion', { entity: $t('entity.improvementPlan.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('global.form.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-improvementPlan"
            data-cy="entityConfirmDeleteButton"
            @click="removeImprovementPlan"
          >
            {{ $t('global.form.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="improvementPlans?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./improvement-plan.component.ts"></script>
