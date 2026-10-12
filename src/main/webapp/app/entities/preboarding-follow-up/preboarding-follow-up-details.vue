<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="preboardingFollowUp">
        <h2 class="jh-entity-heading" data-cy="preboardingFollowUpDetailsHeading">
          <span>{{ $t('entity.preboardingFollowUp.detail.title') }}</span> {{ preboardingFollowUp.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.candidateId') }}</span>
          </dt>
          <dd>
            <span>{{ preboardingFollowUp.candidateId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.personId') }}</span>
          </dt>
          <dd>
            <span>{{ preboardingFollowUp.personId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.plannedAt') }}</span>
          </dt>
          <dd>
            <span v-if="preboardingFollowUp.plannedAt">{{ formatDateLong(preboardingFollowUp.plannedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.contactedAt') }}</span>
          </dt>
          <dd>
            <span v-if="preboardingFollowUp.contactedAt">{{ formatDateLong(preboardingFollowUp.contactedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.ownerUsername') }}</span>
          </dt>
          <dd>
            <span>{{ preboardingFollowUp.ownerUsername }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.contactMethod') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + preboardingFollowUp.contactMethod) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.result') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + preboardingFollowUp.result) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.candidateIntent') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + preboardingFollowUp.candidateIntent) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.riskLevel') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + preboardingFollowUp.riskLevel) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.nextAction') }}</span>
          </dt>
          <dd>
            <span>{{ preboardingFollowUp.nextAction }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.nextContactAt') }}</span>
          </dt>
          <dd>
            <span v-if="preboardingFollowUp.nextContactAt">{{ formatDateLong(preboardingFollowUp.nextContactAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.application') }}</span>
          </dt>
          <dd>
            <div v-if="preboardingFollowUp.application">
              <router-link
                :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: preboardingFollowUp.application.id } }"
                >{{ preboardingFollowUp.application.applicationCode }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="preboardingFollowUp.id"
          :to="{ name: 'PreboardingFollowUpEdit', params: { preboardingFollowUpId: preboardingFollowUp.id } }"
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

<script lang="ts" src="./preboarding-follow-up-details.component.ts"></script>
