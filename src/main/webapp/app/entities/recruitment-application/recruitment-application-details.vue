<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="recruitmentApplication">
        <h2 class="jh-entity-heading" data-cy="recruitmentApplicationDetailsHeading">
          <span>{{ $t('entity.recruitmentApplication.detail.title') }}</span> {{ recruitmentApplication.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.applicationCode') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.applicationCode }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.recruitmentType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + recruitmentApplication.recruitmentType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.currentStage') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.currentStage ? $t('recruitment.enums.' + recruitmentApplication.currentStage) : '—' }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.status') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + recruitmentApplication.status) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.decision') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.decision ? $t('recruitment.enums.' + recruitmentApplication.decision) : '—' }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.sourceType') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.sourceType ? $t('recruitment.enums.' + recruitmentApplication.sourceType) : '—' }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.sourceDetail') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.sourceDetail }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.batchName') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.batchName }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.appliedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentApplication.appliedAt">{{ formatDateLong(recruitmentApplication.appliedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.closedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentApplication.closedAt">{{ formatDateLong(recruitmentApplication.closedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.retentionUntil') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.retentionUntil }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.convertedPersonId') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.convertedPersonId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.convertedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentApplication.convertedAt">{{ formatDateLong(recruitmentApplication.convertedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.closeReason') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.closeReason }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.note') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentApplication.note }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.candidate') }}</span>
          </dt>
          <dd>
            <div v-if="recruitmentApplication.candidate">
              <router-link :to="{ name: 'ExternalCandidateView', params: { externalCandidateId: recruitmentApplication.candidate.id } }">{{
                recruitmentApplication.candidate.fullName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.requisition') }}</span>
          </dt>
          <dd>
            <div v-if="recruitmentApplication.requisition">
              <router-link
                :to="{ name: 'RecruitmentRequisitionView', params: { recruitmentRequisitionId: recruitmentApplication.requisition.id } }"
                >{{ recruitmentApplication.requisition.title }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.requisitionVersion') }}</span>
          </dt>
          <dd>
            <div v-if="recruitmentApplication.requisitionVersion">
              <router-link
                :to="{
                  name: 'RecruitmentRequisitionVersionView',
                  params: { recruitmentRequisitionVersionId: recruitmentApplication.requisitionVersion.id },
                }"
                >{{ recruitmentApplication.requisitionVersion.versionNumber }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="recruitmentApplication.id"
          :to="{ name: 'RecruitmentApplicationEdit', params: { recruitmentApplicationId: recruitmentApplication.id } }"
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

<script lang="ts" src="./recruitment-application-details.component.ts"></script>
