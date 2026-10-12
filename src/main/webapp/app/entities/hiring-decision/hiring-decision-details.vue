<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="hiringDecision">
        <h2 class="jh-entity-heading" data-cy="hiringDecisionDetailsHeading">
          <span>{{ $t('entity.hiringDecision.detail.title') }}</span> {{ hiringDecision.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.decision') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + hiringDecision.decision) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.decisionSource') }}</span>
          </dt>
          <dd>
            <span>{{ hiringDecision.decisionSource }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.decidedBy') }}</span>
          </dt>
          <dd>
            <span>{{ hiringDecision.decidedBy }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.decidedAt') }}</span>
          </dt>
          <dd>
            <span v-if="hiringDecision.decidedAt">{{ formatDateLong(hiringDecision.decidedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.reason') }}</span>
          </dt>
          <dd>
            <span>{{ hiringDecision.reason }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.correctionOfId') }}</span>
          </dt>
          <dd>
            <span>{{ hiringDecision.correctionOfId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.active') }}</span>
          </dt>
          <dd>
            <span>{{ hiringDecision.active }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.application') }}</span>
          </dt>
          <dd>
            <div v-if="hiringDecision.application">
              <router-link
                :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: hiringDecision.application.id } }"
                >{{ hiringDecision.application.applicationCode }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="hiringDecision.id"
          :to="{ name: 'HiringDecisionEdit', params: { hiringDecisionId: hiringDecision.id } }"
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

<script lang="ts" src="./hiring-decision-details.component.ts"></script>
