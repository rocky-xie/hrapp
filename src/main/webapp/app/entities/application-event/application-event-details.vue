<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="applicationEvent">
        <h2 class="jh-entity-heading" data-cy="applicationEventDetailsHeading">
          <span>{{ $t('entity.applicationEvent.detail.title') }}</span> {{ applicationEvent.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.stageType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + applicationEvent.stageType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.eventType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + applicationEvent.eventType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.previousValue') }}</span>
          </dt>
          <dd>
            <span>{{ applicationEvent.previousValue }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.newValue') }}</span>
          </dt>
          <dd>
            <span>{{ applicationEvent.newValue }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.actor') }}</span>
          </dt>
          <dd>
            <span>{{ applicationEvent.actor }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.occurredAt') }}</span>
          </dt>
          <dd>
            <span v-if="applicationEvent.occurredAt">{{ formatDateLong(applicationEvent.occurredAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.reason') }}</span>
          </dt>
          <dd>
            <span>{{ applicationEvent.reason }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.metadataJson') }}</span>
          </dt>
          <dd>
            <span>{{ applicationEvent.metadataJson }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.application') }}</span>
          </dt>
          <dd>
            <div v-if="applicationEvent.application">
              <router-link
                :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: applicationEvent.application.id } }"
                >{{ applicationEvent.application.applicationCode }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="applicationEvent.id"
          :to="{ name: 'ApplicationEventEdit', params: { applicationEventId: applicationEvent.id } }"
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

<script lang="ts" src="./application-event-details.component.ts"></script>
