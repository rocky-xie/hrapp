<template>
  <div>
    <h2 id="page-heading" data-cy="RecruitmentEvaluationHeading">
      <span id="recruitment-evaluation">{{ $t('entity.recruitmentEvaluation.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'RecruitmentEvaluationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-recruitment-evaluation"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.recruitmentEvaluation.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && recruitmentEvaluations?.length === 0">
      <span>{{ $t('entity.recruitmentEvaluation.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="recruitmentEvaluations?.length > 0">
      <table class="table table-striped" aria-describedby="recruitmentEvaluations">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('stageType')">
              <span>{{ $t('recruitment.fields.stageType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stageType'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('activityId')">
              <span>{{ $t('recruitment.fields.activityId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activityId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('templateVersionId')">
              <span>{{ $t('recruitment.fields.templateVersionId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'templateVersionId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evaluatorUsername')">
              <span>{{ $t('recruitment.fields.evaluatorUsername') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evaluatorUsername'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('recordedByUsername')">
              <span>{{ $t('recruitment.fields.recordedByUsername') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'recordedByUsername'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('status')">
              <span>{{ $t('recruitment.fields.status') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('overallRecommendation')">
              <span>{{ $t('recruitment.fields.overallRecommendation') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'overallRecommendation'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('totalScore')">
              <span>{{ $t('recruitment.fields.totalScore') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'totalScore'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('summary')">
              <span>{{ $t('recruitment.fields.summary') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'summary'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('answersJson')">
              <span>{{ $t('recruitment.fields.answersJson') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'answersJson'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('evaluatedAt')">
              <span>{{ $t('recruitment.fields.evaluatedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'evaluatedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('submittedAt')">
              <span>{{ $t('recruitment.fields.submittedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'submittedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('publishedAt')">
              <span>{{ $t('recruitment.fields.publishedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'publishedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('correctionOfId')">
              <span>{{ $t('recruitment.fields.correctionOfId') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'correctionOfId'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('application.id')">
              <span>{{ $t('recruitment.fields.application') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'application.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="recruitmentEvaluation in recruitmentEvaluations" :key="recruitmentEvaluation.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'RecruitmentEvaluationView', params: { recruitmentEvaluationId: recruitmentEvaluation.id } }">{{
                recruitmentEvaluation.id
              }}</router-link>
            </td>
            <td>{{ $t('recruitment.enums.' + recruitmentEvaluation.stageType) }}</td>
            <td>{{ recruitmentEvaluation.activityId }}</td>
            <td>{{ recruitmentEvaluation.templateVersionId }}</td>
            <td>{{ recruitmentEvaluation.evaluatorUsername }}</td>
            <td>{{ recruitmentEvaluation.recordedByUsername }}</td>
            <td>{{ $t('recruitment.enums.' + recruitmentEvaluation.status) }}</td>
            <td>{{ recruitmentEvaluation.overallRecommendation }}</td>
            <td>{{ recruitmentEvaluation.totalScore }}</td>
            <td>{{ recruitmentEvaluation.summary }}</td>
            <td>{{ recruitmentEvaluation.answersJson }}</td>
            <td>{{ formatDateShort(recruitmentEvaluation.evaluatedAt) || '' }}</td>
            <td>{{ formatDateShort(recruitmentEvaluation.submittedAt) || '' }}</td>
            <td>{{ formatDateShort(recruitmentEvaluation.publishedAt) || '' }}</td>
            <td>{{ recruitmentEvaluation.correctionOfId }}</td>
            <td>
              <div v-if="recruitmentEvaluation.application">
                <router-link
                  :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: recruitmentEvaluation.application.id } }"
                  >{{ recruitmentEvaluation.application.applicationCode }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'RecruitmentEvaluationView', params: { recruitmentEvaluationId: recruitmentEvaluation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'RecruitmentEvaluationEdit', params: { recruitmentEvaluationId: recruitmentEvaluation.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(recruitmentEvaluation)"
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
        <span id="hrappApp.recruitmentEvaluation.delete.question" data-cy="recruitmentEvaluationDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-recruitment-evaluation-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.recruitmentEvaluation.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-recruitmentEvaluation"
            data-cy="entityConfirmDeleteButton"
            @click="removeRecruitmentEvaluation"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="recruitmentEvaluations?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./recruitment-evaluation.component.ts"></script>
