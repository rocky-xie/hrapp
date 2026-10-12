<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="selectionActivity">
        <h2 class="jh-entity-heading" data-cy="selectionActivityDetailsHeading">
          <span>{{ $t('entity.selectionActivity.detail.title') }}</span> {{ selectionActivity.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.stageType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + selectionActivity.stageType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.activityType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + selectionActivity.activityType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.startAt') }}</span>
          </dt>
          <dd>
            <span v-if="selectionActivity.startAt">{{ formatDateLong(selectionActivity.startAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.endAt') }}</span>
          </dt>
          <dd>
            <span v-if="selectionActivity.endAt">{{ formatDateLong(selectionActivity.endAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.meetingRoomId') }}</span>
          </dt>
          <dd>
            <span>{{ selectionActivity.meetingRoomId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.location') }}</span>
          </dt>
          <dd>
            <span>{{ selectionActivity.location }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.status') }}</span>
          </dt>
          <dd>
            <span>{{ selectionActivity.status }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.scheduleVersion') }}</span>
          </dt>
          <dd>
            <span>{{ selectionActivity.scheduleVersion }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.changeReason') }}</span>
          </dt>
          <dd>
            <span>{{ selectionActivity.changeReason }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.externalCalendarProvider') }}</span>
          </dt>
          <dd>
            <span>{{ selectionActivity.externalCalendarProvider }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.externalEventId') }}</span>
          </dt>
          <dd>
            <span>{{ selectionActivity.externalEventId }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.application') }}</span>
          </dt>
          <dd>
            <div v-if="selectionActivity.application">
              <router-link
                :to="{ name: 'RecruitmentApplicationView', params: { recruitmentApplicationId: selectionActivity.application.id } }"
                >{{ selectionActivity.application.applicationCode }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="selectionActivity.id"
          :to="{ name: 'SelectionActivityEdit', params: { selectionActivityId: selectionActivity.id } }"
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

<script lang="ts" src="./selection-activity-details.component.ts"></script>
