<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="notificationMessage">
        <h2 class="jh-entity-heading" data-cy="notificationMessageDetailsHeading">
          <span>{{ $t('entity.notificationMessage.detail.title') }}</span> {{ notificationMessage.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.eventType') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.eventType }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.templateId') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.templateId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.status') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + notificationMessage.status) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.sendMode') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + notificationMessage.sendMode) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.finalSubject') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.finalSubject }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.finalBody') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.finalBody }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.recipientsJson') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.recipientsJson }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.retryCount') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.retryCount }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.maxRetries') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.maxRetries }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.lastError') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.lastError }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.createdBy') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.createdBy }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.confirmedBy') }}</span>
          </dt>
          <dd>
            <span>{{ notificationMessage.confirmedBy }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.createdAt') }}</span>
          </dt>
          <dd>
            <span v-if="notificationMessage.createdAt">{{ formatDateLong(notificationMessage.createdAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.confirmedAt') }}</span>
          </dt>
          <dd>
            <span v-if="notificationMessage.confirmedAt">{{ formatDateLong(notificationMessage.confirmedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.sentAt') }}</span>
          </dt>
          <dd>
            <span v-if="notificationMessage.sentAt">{{ formatDateLong(notificationMessage.sentAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.application') }}</span>
          </dt>
          <dd>
            <div v-if="notificationMessage.application">
              <router-link
                :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: notificationMessage.application.id } }"
                >{{ notificationMessage.application.applicationCode }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="notificationMessage.id"
          :to="{ name: 'NotificationMessageEdit', params: { notificationMessageId: notificationMessage.id } }"
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

<script lang="ts" src="./notification-message-details.component.ts"></script>
