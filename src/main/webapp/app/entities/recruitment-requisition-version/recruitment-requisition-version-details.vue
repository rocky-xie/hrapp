<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="recruitmentRequisitionVersion">
        <h2 class="jh-entity-heading" data-cy="recruitmentRequisitionVersionDetailsHeading">
          <span>{{ $t('entity.recruitmentRequisitionVersion.detail.title') }}</span> {{ recruitmentRequisitionVersion.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.versionNumber') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisitionVersion.versionNumber }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.snapshotJson') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisitionVersion.snapshotJson }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.lockedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentRequisitionVersion.lockedAt">{{ formatDateLong(recruitmentRequisitionVersion.lockedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.lockedBy') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisitionVersion.lockedBy }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.changeReason') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisitionVersion.changeReason }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.requisition') }}</span>
          </dt>
          <dd>
            <div v-if="recruitmentRequisitionVersion.requisition">
              <router-link
                :to="{
                  name: 'RecruitmentRequisitionView',
                  params: { recruitmentRequisitionId: recruitmentRequisitionVersion.requisition.id },
                }"
                >{{ recruitmentRequisitionVersion.requisition.title }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="recruitmentRequisitionVersion.id"
          :to="{ name: 'RecruitmentRequisitionVersionEdit', params: { recruitmentRequisitionVersionId: recruitmentRequisitionVersion.id } }"
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

<script lang="ts" src="./recruitment-requisition-version-details.component.ts"></script>
