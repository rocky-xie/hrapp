<template>
  <div>
    <h2 id="page-heading" data-cy="DispatchReviewHeading">
      <span id="dispatch-review">{{ $t('entity.dispatchReview.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>{{ $t('recruitment.ui.refresh') }}</span>
        </button>
        <router-link :to="{ name: 'DispatchReviewCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-dispatch-review"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ $t('entity.dispatchReview.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && dispatchReviews?.length === 0">
      <span>{{ $t('entity.dispatchReview.home.empty') }}</span>
    </div>
    <div class="table-responsive" v-if="dispatchReviews?.length > 0">
      <table class="table table-striped" aria-describedby="dispatchReviews">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ $t('recruitment.ui.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reviewedAt')">
              <span>{{ $t('recruitment.fields.reviewedAt') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reviewedAt'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('reviewerUsername')">
              <span>{{ $t('recruitment.fields.reviewerUsername') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reviewerUsername'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('result')">
              <span>{{ $t('recruitment.fields.result') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'result'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('recommendation')">
              <span>{{ $t('recruitment.fields.recommendation') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'recommendation'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('extensionEndDate')">
              <span>{{ $t('recruitment.fields.extensionEndDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'extensionEndDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('finalDecision')">
              <span>{{ $t('recruitment.fields.finalDecision') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'finalDecision'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('dispatchAssignment.id')">
              <span>{{ $t('recruitment.fields.dispatchAssignment') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dispatchAssignment.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dispatchReview in dispatchReviews" :key="dispatchReview.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DispatchReviewView', params: { dispatchReviewId: dispatchReview.id } }">{{
                dispatchReview.id
              }}</router-link>
            </td>
            <td>{{ formatDateShort(dispatchReview.reviewedAt) || '' }}</td>
            <td>{{ dispatchReview.reviewerUsername }}</td>
            <td>{{ $t('recruitment.enums.' + dispatchReview.result) }}</td>
            <td>{{ $t('recruitment.enums.' + dispatchReview.recommendation) }}</td>
            <td>{{ dispatchReview.extensionEndDate }}</td>
            <td>{{ dispatchReview.finalDecision ? $t('recruitment.enums.' + dispatchReview.finalDecision) : '—' }}</td>
            <td>
              <div v-if="dispatchReview.dispatchAssignment">
                <router-link
                  :to="{ name: 'DispatchAssignmentView', params: { dispatchAssignmentId: dispatchReview.dispatchAssignment.id } }"
                  >{{ dispatchReview.dispatchAssignment.department }}</router-link
                >
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'DispatchReviewView', params: { dispatchReviewId: dispatchReview.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'DispatchReviewEdit', params: { dispatchReviewId: dispatchReview.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ $t('recruitment.ui.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(dispatchReview)"
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
        <span id="hrappApp.dispatchReview.delete.question" data-cy="dispatchReviewDeleteDialogHeading">{{
          $t('recruitment.ui.deleteTitle')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-dispatch-review-heading">
          {{ $t('recruitment.ui.deleteQuestion', { entity: $t('entity.dispatchReview.name'), id: removeId }) }}
        </p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ $t('recruitment.ui.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-dispatchReview"
            data-cy="entityConfirmDeleteButton"
            @click="removeDispatchReview"
          >
            {{ $t('recruitment.ui.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="dispatchReviews?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./dispatch-review.component.ts"></script>
