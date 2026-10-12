<template>
  <div v-if="canAccessRecruitment">
    <b-dropdown-item to="/recruitment-workflow">
      <font-awesome-icon icon="tasks" />
      <span>{{ $t('recruitment.workflow.title') }}</span>
    </b-dropdown-item>
    <b-dropdown-divider />
    <b-dropdown-item to="/recruitment-application"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.recruitmentApplication.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/recruitment-evaluation"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.recruitmentEvaluation.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/recruitment-requisition"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.recruitmentRequisition.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/recruitment-requisition-member"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.recruitmentRequisitionMember.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/recruitment-requisition-version"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.recruitmentRequisitionVersion.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/candidate-resume"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.candidateResume.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/external-candidate"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.externalCandidate.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/selection-activity"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.selectionActivity.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/application-event"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.applicationEvent.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/application-stage"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.applicationStage.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/hiring-decision"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.hiringDecision.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/dispatch-assignment"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.dispatchAssignment.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/dispatch-review"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.dispatchReview.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/preboarding-follow-up"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.preboardingFollowUp.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/notification-message"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.notificationMessage.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/notification-template"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.notificationTemplate.name') }}</span></b-dropdown-item
    >
    <b-dropdown-item to="/partner-organization"
      ><font-awesome-icon icon="asterisk" /><span>{{ $t('entity.partnerOrganization.name') }}</span></b-dropdown-item
    >
  </div>
</template>
