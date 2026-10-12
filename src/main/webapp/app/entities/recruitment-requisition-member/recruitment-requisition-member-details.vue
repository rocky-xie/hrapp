<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="recruitmentRequisitionMember">
        <h2 class="jh-entity-heading" data-cy="recruitmentRequisitionMemberDetailsHeading">
          <span>{{ $t('entity.recruitmentRequisitionMember.detail.title') }}</span> {{ recruitmentRequisitionMember.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ $t('recruitment.fields.username') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisitionMember.username }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.memberType') }}</span>
          </dt>
          <dd>
            <span>{{ $t('recruitment.enums.' + recruitmentRequisitionMember.memberType) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.active') }}</span>
          </dt>
          <dd>
            <span>{{ recruitmentRequisitionMember.active }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.createdAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentRequisitionMember.createdAt">{{ formatDateLong(recruitmentRequisitionMember.createdAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.removedAt') }}</span>
          </dt>
          <dd>
            <span v-if="recruitmentRequisitionMember.removedAt">{{ formatDateLong(recruitmentRequisitionMember.removedAt) }}</span>
          </dd>
          <dt>
            <span>{{ $t('recruitment.fields.requisition') }}</span>
          </dt>
          <dd>
            <div v-if="recruitmentRequisitionMember.requisition">
              <router-link
                :to="{
                  name: 'RecruitmentRequisitionView',
                  params: { recruitmentRequisitionId: recruitmentRequisitionMember.requisition.id },
                }"
                >{{ recruitmentRequisitionMember.requisition.title }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ $t('recruitment.ui.back') }}</span>
        </button>
        <router-link
          v-if="recruitmentRequisitionMember.id"
          :to="{ name: 'RecruitmentRequisitionMemberEdit', params: { recruitmentRequisitionMemberId: recruitmentRequisitionMember.id } }"
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

<script lang="ts" src="./recruitment-requisition-member-details.component.ts"></script>
