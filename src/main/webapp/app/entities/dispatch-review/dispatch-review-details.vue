<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="dispatchReview">
        <h2 class="jh-entity-heading" data-cy="dispatchReviewDetailsHeading">
          <span>{{ $t('entity.dispatchReview.detail.title') }}</span> {{ dispatchReview.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.reviewedAt') }}</span>
          </dt>
          <dd>
            <span v-if="dispatchReview.reviewedAt">{{ formatDateLong(dispatchReview.reviewedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.reviewerUsername') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchReview.reviewerUsername }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.result') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + dispatchReview.result) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.recommendation') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + dispatchReview.recommendation) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.extensionEndDate') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchReview.extensionEndDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.finalDecision') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchReview.finalDecision ? $t('recruitment.enums.' + dispatchReview.finalDecision) : '—' }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.dispatchAssignment') }}</span>
          </dt>
          <dd>
            <div v-if="dispatchReview.dispatchAssignment">
              <router-link
                :to="{ name: 'DispatchAssignmentView', params: { dispatchAssignmentId: dispatchReview.dispatchAssignment.id } }"
                >{{ dispatchReview.dispatchAssignment.department }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="dispatchReview.id"
          :to="{ name: 'DispatchReviewEdit', params: { dispatchReviewId: dispatchReview.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./dispatch-review-details.component.ts"></script>
