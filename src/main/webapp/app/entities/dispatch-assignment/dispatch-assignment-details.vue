<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="dispatchAssignment">
        <h2 class="jh-entity-heading" data-cy="dispatchAssignmentDetailsHeading">
          <span>{{ $t('entity.dispatchAssignment.detail.title') }}</span> {{ dispatchAssignment.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.candidateId') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.candidateId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.personId') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.personId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.partnerOrganizationId') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.partnerOrganizationId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.department') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.department }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.positionId') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.positionId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.startDate') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.startDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.expectedEndDate') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.expectedEndDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.observationDays') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.observationDays }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.status') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + dispatchAssignment.status) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.finalDecision') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.finalDecision ? $t('recruitment.enums.' + dispatchAssignment.finalDecision) : '—' }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.note') }}</span>
          </dt>
          <dd>
            <span>{{ dispatchAssignment.note }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.application') }}</span>
          </dt>
          <dd>
            <div v-if="dispatchAssignment.application">
              <router-link
                :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: dispatchAssignment.application.id } }"
                >{{ dispatchAssignment.application.applicationCode }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="dispatchAssignment.id"
          :to="{ name: 'DispatchAssignmentEdit', params: { dispatchAssignmentId: dispatchAssignment.id } }"
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

<script lang="ts" src="./dispatch-assignment-details.component.ts"></script>
