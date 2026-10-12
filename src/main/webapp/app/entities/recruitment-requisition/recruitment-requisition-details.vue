<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="recruitmentRequisition">
        <h2 class="jh-entity-heading" data-cy="recruitmentRequisitionDetailsHeading">
          <span>{{ $t('entity.recruitmentRequisition.detail.title') }}</span> {{ recruitmentRequisition.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.requisitionCode') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.requisitionCode }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.title') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.title }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.department') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.department }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.positionId') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.positionId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.recruitmentType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + recruitmentRequisition.recruitmentType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.lifecycle') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + recruitmentRequisition.lifecycle) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.editStatus') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + recruitmentRequisition.editStatus) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.plannedHeadcount') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.plannedHeadcount }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.hiredCount') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.hiredCount }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.checkedInCount') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.checkedInCount }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.priority') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.priority ? $t('recruitment.enums.' + recruitmentRequisition.priority) : '—' }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.reason') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.reason }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.targetJoinDate') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.targetJoinDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.desiredStartDate') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.desiredStartDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.desiredEndDate') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.desiredEndDate }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.currentVersion') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.currentVersion }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.lockedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentRequisition.lockedAt">{{ formatDateLong(recruitmentRequisition.lockedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.lockedBy') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.lockedBy }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.unlockedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentRequisition.unlockedAt">{{ formatDateLong(recruitmentRequisition.unlockedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.unlockReason') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.unlockReason }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.requirementsJson') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisition.requirementsJson }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="recruitmentRequisition.id"
          :to="{ name: 'RecruitmentRequisitionEdit', params: { recruitmentRequisitionId: recruitmentRequisition.id } }"
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

<script lang="ts" src="./recruitment-requisition-details.component.ts"></script>
