<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="applicationStage">
        <h2 class="jh-entity-heading" data-cy="applicationStageDetailsHeading">
          <span>{{ $t('entity.applicationStage.detail.title') }}</span> {{ applicationStage.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.stageType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + applicationStage.stageType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.sequenceNumber') }}</span>
          </dt>
          <dd>
            <span>{{ applicationStage.sequenceNumber }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.stageStatus') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + applicationStage.stageStatus) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.startedAt') }}</span>
          </dt>
          <dd>
            <span v-if="applicationStage.startedAt">{{ formatDateLong(applicationStage.startedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.completedAt') }}</span>
          </dt>
          <dd>
            <span v-if="applicationStage.completedAt">{{ formatDateLong(applicationStage.completedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.selected') }}</span>
          </dt>
          <dd>
            <span>{{ applicationStage.selected }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.decisionReason') }}</span>
          </dt>
          <dd>
            <span>{{ applicationStage.decisionReason }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.application') }}</span>
          </dt>
          <dd>
            <div v-if="applicationStage.application">
              <router-link
                :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: applicationStage.application.id } }"
                >{{ applicationStage.application.applicationCode }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="applicationStage.id"
          :to="{ name: 'ApplicationStageEdit', params: { applicationStageId: applicationStage.id } }"
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

<script lang="ts" src="./application-stage-details.component.ts"></script>
