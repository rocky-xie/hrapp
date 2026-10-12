<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="notificationTemplate">
        <h2 class="jh-entity-heading" data-cy="notificationTemplateDetailsHeading">
          <span>{{ $t('entity.notificationTemplate.detail.title') }}</span> {{ notificationTemplate.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.eventType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + notificationTemplate.eventType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.recruitmentType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + notificationTemplate.recruitmentType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.language') }}</span>
          </dt>
          <dd>
            <span>{{ notificationTemplate.language }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.versionNumber') }}</span>
          </dt>
          <dd>
            <span>{{ notificationTemplate.versionNumber }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.subjectTemplate') }}</span>
          </dt>
          <dd>
            <span>{{ notificationTemplate.subjectTemplate }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.bodyTemplate') }}</span>
          </dt>
          <dd>
            <span>{{ notificationTemplate.bodyTemplate }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.externalSafe') }}</span>
          </dt>
          <dd>
            <span>{{ notificationTemplate.externalSafe }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.published') }}</span>
          </dt>
          <dd>
            <span>{{ notificationTemplate.published }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="notificationTemplate.id"
          :to="{ name: 'NotificationTemplateEdit', params: { notificationTemplateId: notificationTemplate.id } }"
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

<script lang="ts" src="./notification-template-details.component.ts"></script>
