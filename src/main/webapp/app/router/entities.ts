import { Authority } from '@/shared/jhipster/constants';
const Entities = () => import('@/entities/entities.vue');

const Position = () => import('@/entities/position/position.vue');
const PositionUpdate = () => import('@/entities/position/position-update.vue');
const PositionDetails = () => import('@/entities/position/position-details.vue');

const Person = () => import('@/entities/person/person.vue');
const PersonUpdate = () => import('@/entities/person/person-update.vue');
const PersonDetails = () => import('@/entities/person/person-details.vue');

const Skill = () => import('@/entities/skill/skill.vue');
const SkillUpdate = () => import('@/entities/skill/skill-update.vue');
const SkillDetails = () => import('@/entities/skill/skill-details.vue');

const SkillLevel = () => import('@/entities/skill-level/skill-level.vue');
const SkillLevelUpdate = () => import('@/entities/skill-level/skill-level-update.vue');
const SkillLevelDetails = () => import('@/entities/skill-level/skill-level-details.vue');

const PositionMatch = () => import('@/entities/position-match/position-match.vue');
const PositionMatchUpdate = () => import('@/entities/position-match/position-match-update.vue');
const PositionMatchDetails = () => import('@/entities/position-match/position-match-details.vue');

const SuccessionCandidate = () => import('@/entities/succession-candidate/succession-candidate.vue');
const SuccessionCandidateUpdate = () => import('@/entities/succession-candidate/succession-candidate-update.vue');
const SuccessionCandidateDetails = () => import('@/entities/succession-candidate/succession-candidate-details.vue');

const PositionRisk = () => import('@/entities/position-risk/position-risk.vue');
const PositionRiskUpdate = () => import('@/entities/position-risk/position-risk-update.vue');
const PositionRiskDetails = () => import('@/entities/position-risk/position-risk-details.vue');

const PersonRisk = () => import('@/entities/person-risk/person-risk.vue');
const PersonRiskUpdate = () => import('@/entities/person-risk/person-risk-update.vue');
const PersonRiskDetails = () => import('@/entities/person-risk/person-risk-details.vue');

const SkillUpgradeRecord = () => import('@/entities/skill-upgrade-record/skill-upgrade-record.vue');
const SkillUpgradeRecordUpdate = () => import('@/entities/skill-upgrade-record/skill-upgrade-record-update.vue');
const SkillUpgradeRecordDetails = () => import('@/entities/skill-upgrade-record/skill-upgrade-record-details.vue');

const StaffSubstitution = () => import('@/entities/staff-substitution/staff-substitution.vue');
const StaffSubstitutionUpdate = () => import('@/entities/staff-substitution/staff-substitution-update.vue');
const StaffSubstitutionDetails = () => import('@/entities/staff-substitution/staff-substitution-details.vue');

const PositionRiskEvaluation = () => import('@/entities/position-risk-evaluation/position-risk-evaluation.vue');
const PositionRiskEvaluationUpdate = () => import('@/entities/position-risk-evaluation/position-risk-evaluation-update.vue');
const PositionRiskEvaluationDetails = () => import('@/entities/position-risk-evaluation/position-risk-evaluation-details.vue');

const TrainingGoal = () => import('@/entities/training-goal/training-goal.vue');
const TrainingGoalUpdate = () => import('@/entities/training-goal/training-goal-update.vue');
const TrainingGoalDetails = () => import('@/entities/training-goal/training-goal-details.vue');

const TrainingRecord = () => import('@/entities/training-record/training-record.vue');
const TrainingRecordUpdate = () => import('@/entities/training-record/training-record-update.vue');
const TrainingRecordDetails = () => import('@/entities/training-record/training-record-details.vue');

const SkillAssessment = () => import('@/entities/skill-assessment/skill-assessment.vue');
const SkillAssessmentUpdate = () => import('@/entities/skill-assessment/skill-assessment-update.vue');
const SkillAssessmentDetails = () => import('@/entities/skill-assessment/skill-assessment-details.vue');

const TrustObservation = () => import('@/entities/trust-observation/trust-observation.vue');
const TrustObservationUpdate = () => import('@/entities/trust-observation/trust-observation-update.vue');
const TrustObservationDetails = () => import('@/entities/trust-observation/trust-observation-details.vue');

const Evaluation = () => import('@/entities/evaluation/evaluation.vue');
const EvaluationUpdate = () => import('@/entities/evaluation/evaluation-update.vue');
const EvaluationDetails = () => import('@/entities/evaluation/evaluation-details.vue');

const ImprovementPlan = () => import('@/entities/improvement-plan/improvement-plan.vue');
const ImprovementPlanUpdate = () => import('@/entities/improvement-plan/improvement-plan-update.vue');
const ImprovementPlanDetails = () => import('@/entities/improvement-plan/improvement-plan-details.vue');

const KeyResponsibilityCategory = () => import('@/entities/key-responsibility-category/key-responsibility-category.vue');
const KeyResponsibilityCategoryUpdate = () => import('@/entities/key-responsibility-category/key-responsibility-category-update.vue');
const KeyResponsibilityCategoryDetails = () => import('@/entities/key-responsibility-category/key-responsibility-category-details.vue');

const CandidateProfile = () => import('@/entities/candidate-profile/candidate-profile.vue');
const CandidateProfileUpdate = () => import('@/entities/candidate-profile/candidate-profile-update.vue');
const CandidateProfileDetails = () => import('@/entities/candidate-profile/candidate-profile-details.vue');

const ApplicationEvent = () => import('@/entities/application-event/application-event.vue');
const ApplicationEventUpdate = () => import('@/entities/application-event/application-event-update.vue');
const ApplicationEventDetails = () => import('@/entities/application-event/application-event-details.vue');
const ApplicationStage = () => import('@/entities/application-stage/application-stage.vue');
const ApplicationStageUpdate = () => import('@/entities/application-stage/application-stage-update.vue');
const ApplicationStageDetails = () => import('@/entities/application-stage/application-stage-details.vue');
const CandidateResume = () => import('@/entities/candidate-resume/candidate-resume.vue');
const CandidateResumeUpdate = () => import('@/entities/candidate-resume/candidate-resume-update.vue');
const CandidateResumeDetails = () => import('@/entities/candidate-resume/candidate-resume-details.vue');
const DispatchAssignment = () => import('@/entities/dispatch-assignment/dispatch-assignment.vue');
const DispatchAssignmentUpdate = () => import('@/entities/dispatch-assignment/dispatch-assignment-update.vue');
const DispatchAssignmentDetails = () => import('@/entities/dispatch-assignment/dispatch-assignment-details.vue');
const DispatchReview = () => import('@/entities/dispatch-review/dispatch-review.vue');
const DispatchReviewUpdate = () => import('@/entities/dispatch-review/dispatch-review-update.vue');
const DispatchReviewDetails = () => import('@/entities/dispatch-review/dispatch-review-details.vue');
const ExternalCandidate = () => import('@/entities/external-candidate/external-candidate.vue');
const ExternalCandidateUpdate = () => import('@/entities/external-candidate/external-candidate-update.vue');
const ExternalCandidateDetails = () => import('@/entities/external-candidate/external-candidate-details.vue');
const HiringDecision = () => import('@/entities/hiring-decision/hiring-decision.vue');
const HiringDecisionUpdate = () => import('@/entities/hiring-decision/hiring-decision-update.vue');
const HiringDecisionDetails = () => import('@/entities/hiring-decision/hiring-decision-details.vue');
const NotificationMessage = () => import('@/entities/notification-message/notification-message.vue');
const NotificationMessageUpdate = () => import('@/entities/notification-message/notification-message-update.vue');
const NotificationMessageDetails = () => import('@/entities/notification-message/notification-message-details.vue');
const NotificationTemplate = () => import('@/entities/notification-template/notification-template.vue');
const NotificationTemplateUpdate = () => import('@/entities/notification-template/notification-template-update.vue');
const NotificationTemplateDetails = () => import('@/entities/notification-template/notification-template-details.vue');
const PartnerOrganization = () => import('@/entities/partner-organization/partner-organization.vue');
const PartnerOrganizationUpdate = () => import('@/entities/partner-organization/partner-organization-update.vue');
const PartnerOrganizationDetails = () => import('@/entities/partner-organization/partner-organization-details.vue');
const PreboardingFollowUp = () => import('@/entities/preboarding-follow-up/preboarding-follow-up.vue');
const PreboardingFollowUpUpdate = () => import('@/entities/preboarding-follow-up/preboarding-follow-up-update.vue');
const PreboardingFollowUpDetails = () => import('@/entities/preboarding-follow-up/preboarding-follow-up-details.vue');
const RecruitmentApplication = () => import('@/entities/recruitment-application/recruitment-application.vue');
const RecruitmentWorkflow = () => import('@/entities/recruitment-workflow/recruitment-workflow.vue');
const RecruitmentApplicationUpdate = () => import('@/entities/recruitment-application/recruitment-application-update.vue');
const RecruitmentApplicationDetails = () => import('@/entities/recruitment-application/recruitment-application-details.vue');
const RecruitmentEvaluation = () => import('@/entities/recruitment-evaluation/recruitment-evaluation.vue');
const RecruitmentEvaluationUpdate = () => import('@/entities/recruitment-evaluation/recruitment-evaluation-update.vue');
const RecruitmentEvaluationDetails = () => import('@/entities/recruitment-evaluation/recruitment-evaluation-details.vue');
const RecruitmentRequisition = () => import('@/entities/recruitment-requisition/recruitment-requisition.vue');
const RecruitmentRequisitionUpdate = () => import('@/entities/recruitment-requisition/recruitment-requisition-update.vue');
const RecruitmentRequisitionDetails = () => import('@/entities/recruitment-requisition/recruitment-requisition-details.vue');
const RecruitmentRequisitionMember = () => import('@/entities/recruitment-requisition-member/recruitment-requisition-member.vue');
const RecruitmentRequisitionMemberUpdate = () =>
  import('@/entities/recruitment-requisition-member/recruitment-requisition-member-update.vue');
const RecruitmentRequisitionMemberDetails = () =>
  import('@/entities/recruitment-requisition-member/recruitment-requisition-member-details.vue');
const RecruitmentRequisitionVersion = () => import('@/entities/recruitment-requisition-version/recruitment-requisition-version.vue');
const RecruitmentRequisitionVersionUpdate = () =>
  import('@/entities/recruitment-requisition-version/recruitment-requisition-version-update.vue');
const RecruitmentRequisitionVersionDetails = () =>
  import('@/entities/recruitment-requisition-version/recruitment-requisition-version-details.vue');
const SelectionActivity = () => import('@/entities/selection-activity/selection-activity.vue');
const SelectionActivityUpdate = () => import('@/entities/selection-activity/selection-activity-update.vue');
const SelectionActivityDetails = () => import('@/entities/selection-activity/selection-activity-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'position',
      name: 'Position',
      component: Position,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position/new',
      name: 'PositionCreate',
      component: PositionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position/:positionId/edit',
      name: 'PositionEdit',
      component: PositionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position/:positionId/view',
      name: 'PositionView',
      component: PositionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'person',
      name: 'Person',
      component: Person,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'person/new',
      name: 'PersonCreate',
      component: PersonUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'person/:personId/edit',
      name: 'PersonEdit',
      component: PersonUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'person/:personId/view',
      name: 'PersonView',
      component: PersonDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill',
      name: 'Skill',
      component: Skill,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill/new',
      name: 'SkillCreate',
      component: SkillUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill/:skillId/edit',
      name: 'SkillEdit',
      component: SkillUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill/:skillId/view',
      name: 'SkillView',
      component: SkillDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-level',
      name: 'SkillLevel',
      component: SkillLevel,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-level/new',
      name: 'SkillLevelCreate',
      component: SkillLevelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-level/:skillLevelId/edit',
      name: 'SkillLevelEdit',
      component: SkillLevelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-level/:skillLevelId/view',
      name: 'SkillLevelView',
      component: SkillLevelDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-match',
      name: 'PositionMatch',
      component: PositionMatch,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-match/new',
      name: 'PositionMatchCreate',
      component: PositionMatchUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-match/:positionMatchId/edit',
      name: 'PositionMatchEdit',
      component: PositionMatchUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-match/:positionMatchId/view',
      name: 'PositionMatchView',
      component: PositionMatchDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'succession-candidate',
      name: 'SuccessionCandidate',
      component: SuccessionCandidate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'succession-candidate/new',
      name: 'SuccessionCandidateCreate',
      component: SuccessionCandidateUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'succession-candidate/:successionCandidateId/edit',
      name: 'SuccessionCandidateEdit',
      component: SuccessionCandidateUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'succession-candidate/:successionCandidateId/view',
      name: 'SuccessionCandidateView',
      component: SuccessionCandidateDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-risk',
      name: 'PositionRisk',
      component: PositionRisk,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-risk/new',
      name: 'PositionRiskCreate',
      component: PositionRiskUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-risk/:positionRiskId/edit',
      name: 'PositionRiskEdit',
      component: PositionRiskUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-risk/:positionRiskId/view',
      name: 'PositionRiskView',
      component: PositionRiskDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'person-risk',
      name: 'PersonRisk',
      component: PersonRisk,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'person-risk/new',
      name: 'PersonRiskCreate',
      component: PersonRiskUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'person-risk/:personRiskId/edit',
      name: 'PersonRiskEdit',
      component: PersonRiskUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'person-risk/:personRiskId/view',
      name: 'PersonRiskView',
      component: PersonRiskDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-upgrade-record',
      name: 'SkillUpgradeRecord',
      component: SkillUpgradeRecord,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-upgrade-record/new',
      name: 'SkillUpgradeRecordCreate',
      component: SkillUpgradeRecordUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-upgrade-record/:skillUpgradeRecordId/edit',
      name: 'SkillUpgradeRecordEdit',
      component: SkillUpgradeRecordUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-upgrade-record/:skillUpgradeRecordId/view',
      name: 'SkillUpgradeRecordView',
      component: SkillUpgradeRecordDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'staff-substitution',
      name: 'StaffSubstitution',
      component: StaffSubstitution,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'staff-substitution/new',
      name: 'StaffSubstitutionCreate',
      component: StaffSubstitutionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'staff-substitution/:staffSubstitutionId/edit',
      name: 'StaffSubstitutionEdit',
      component: StaffSubstitutionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'staff-substitution/:staffSubstitutionId/view',
      name: 'StaffSubstitutionView',
      component: StaffSubstitutionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-risk-evaluation',
      name: 'PositionRiskEvaluation',
      component: PositionRiskEvaluation,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-risk-evaluation/new',
      name: 'PositionRiskEvaluationCreate',
      component: PositionRiskEvaluationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-risk-evaluation/:positionRiskEvaluationId/edit',
      name: 'PositionRiskEvaluationEdit',
      component: PositionRiskEvaluationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'position-risk-evaluation/:positionRiskEvaluationId/view',
      name: 'PositionRiskEvaluationView',
      component: PositionRiskEvaluationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'training-goal',
      name: 'TrainingGoal',
      component: TrainingGoal,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'training-goal/new',
      name: 'TrainingGoalCreate',
      component: TrainingGoalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'training-goal/:trainingGoalId/edit',
      name: 'TrainingGoalEdit',
      component: TrainingGoalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'training-goal/:trainingGoalId/view',
      name: 'TrainingGoalView',
      component: TrainingGoalDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'training-record',
      name: 'TrainingRecord',
      component: TrainingRecord,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'training-record/new',
      name: 'TrainingRecordCreate',
      component: TrainingRecordUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'training-record/:trainingRecordId/edit',
      name: 'TrainingRecordEdit',
      component: TrainingRecordUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'training-record/:trainingRecordId/view',
      name: 'TrainingRecordView',
      component: TrainingRecordDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-assessment',
      name: 'SkillAssessment',
      component: SkillAssessment,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-assessment/new',
      name: 'SkillAssessmentCreate',
      component: SkillAssessmentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-assessment/:skillAssessmentId/edit',
      name: 'SkillAssessmentEdit',
      component: SkillAssessmentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'skill-assessment/:skillAssessmentId/view',
      name: 'SkillAssessmentView',
      component: SkillAssessmentDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trust-observation',
      name: 'TrustObservation',
      component: TrustObservation,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trust-observation/new',
      name: 'TrustObservationCreate',
      component: TrustObservationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trust-observation/:trustObservationId/edit',
      name: 'TrustObservationEdit',
      component: TrustObservationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trust-observation/:trustObservationId/view',
      name: 'TrustObservationView',
      component: TrustObservationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'evaluation',
      name: 'Evaluation',
      component: Evaluation,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'evaluation/new',
      name: 'EvaluationCreate',
      component: EvaluationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'evaluation/:evaluationId/edit',
      name: 'EvaluationEdit',
      component: EvaluationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'evaluation/:evaluationId/view',
      name: 'EvaluationView',
      component: EvaluationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'improvement-plan',
      name: 'ImprovementPlan',
      component: ImprovementPlan,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'improvement-plan/new',
      name: 'ImprovementPlanCreate',
      component: ImprovementPlanUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'improvement-plan/:improvementPlanId/edit',
      name: 'ImprovementPlanEdit',
      component: ImprovementPlanUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'improvement-plan/:improvementPlanId/view',
      name: 'ImprovementPlanView',
      component: ImprovementPlanDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'key-responsibility-category',
      name: 'KeyResponsibilityCategory',
      component: KeyResponsibilityCategory,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'key-responsibility-category/new',
      name: 'KeyResponsibilityCategoryCreate',
      component: KeyResponsibilityCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'key-responsibility-category/:keyResponsibilityCategoryId/edit',
      name: 'KeyResponsibilityCategoryEdit',
      component: KeyResponsibilityCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'key-responsibility-category/:keyResponsibilityCategoryId/view',
      name: 'KeyResponsibilityCategoryView',
      component: KeyResponsibilityCategoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'candidate-profile',
      name: 'CandidateProfile',
      component: CandidateProfile,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'candidate-profile/new',
      name: 'CandidateProfileCreate',
      component: CandidateProfileUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'candidate-profile/:candidateProfileId/edit',
      name: 'CandidateProfileEdit',
      component: CandidateProfileUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'candidate-profile/:candidateProfileId/view',
      name: 'CandidateProfileView',
      component: CandidateProfileDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-event',
      name: 'ApplicationEvent',
      component: ApplicationEvent,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'application-event/new',
      name: 'ApplicationEventCreate',
      component: ApplicationEventUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'application-event/:applicationEventId/edit',
      name: 'ApplicationEventEdit',
      component: ApplicationEventUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'application-event/:applicationEventId/view',
      name: 'ApplicationEventView',
      component: ApplicationEventDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'application-stage',
      name: 'ApplicationStage',
      component: ApplicationStage,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'application-stage/new',
      name: 'ApplicationStageCreate',
      component: ApplicationStageUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'application-stage/:applicationStageId/edit',
      name: 'ApplicationStageEdit',
      component: ApplicationStageUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'application-stage/:applicationStageId/view',
      name: 'ApplicationStageView',
      component: ApplicationStageDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'candidate-resume',
      name: 'CandidateResume',
      component: CandidateResume,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'candidate-resume/new',
      name: 'CandidateResumeCreate',
      component: CandidateResumeUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'candidate-resume/:candidateResumeId/edit',
      name: 'CandidateResumeEdit',
      component: CandidateResumeUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'candidate-resume/:candidateResumeId/view',
      name: 'CandidateResumeView',
      component: CandidateResumeDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'dispatch-assignment',
      name: 'DispatchAssignment',
      component: DispatchAssignment,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'dispatch-assignment/new',
      name: 'DispatchAssignmentCreate',
      component: DispatchAssignmentUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'dispatch-assignment/:dispatchAssignmentId/edit',
      name: 'DispatchAssignmentEdit',
      component: DispatchAssignmentUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'dispatch-assignment/:dispatchAssignmentId/view',
      name: 'DispatchAssignmentView',
      component: DispatchAssignmentDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'dispatch-review',
      name: 'DispatchReview',
      component: DispatchReview,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'dispatch-review/new',
      name: 'DispatchReviewCreate',
      component: DispatchReviewUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'dispatch-review/:dispatchReviewId/edit',
      name: 'DispatchReviewEdit',
      component: DispatchReviewUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'dispatch-review/:dispatchReviewId/view',
      name: 'DispatchReviewView',
      component: DispatchReviewDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'external-candidate',
      name: 'ExternalCandidate',
      component: ExternalCandidate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'external-candidate/new',
      name: 'ExternalCandidateCreate',
      component: ExternalCandidateUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'external-candidate/:externalCandidateId/edit',
      name: 'ExternalCandidateEdit',
      component: ExternalCandidateUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'external-candidate/:externalCandidateId/view',
      name: 'ExternalCandidateView',
      component: ExternalCandidateDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'hiring-decision',
      name: 'HiringDecision',
      component: HiringDecision,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'hiring-decision/new',
      name: 'HiringDecisionCreate',
      component: HiringDecisionUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'hiring-decision/:hiringDecisionId/edit',
      name: 'HiringDecisionEdit',
      component: HiringDecisionUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'hiring-decision/:hiringDecisionId/view',
      name: 'HiringDecisionView',
      component: HiringDecisionDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'notification-message',
      name: 'NotificationMessage',
      component: NotificationMessage,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'notification-message/new',
      name: 'NotificationMessageCreate',
      component: NotificationMessageUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'notification-message/:notificationMessageId/edit',
      name: 'NotificationMessageEdit',
      component: NotificationMessageUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'notification-message/:notificationMessageId/view',
      name: 'NotificationMessageView',
      component: NotificationMessageDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'notification-template',
      name: 'NotificationTemplate',
      component: NotificationTemplate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'notification-template/new',
      name: 'NotificationTemplateCreate',
      component: NotificationTemplateUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'notification-template/:notificationTemplateId/edit',
      name: 'NotificationTemplateEdit',
      component: NotificationTemplateUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'notification-template/:notificationTemplateId/view',
      name: 'NotificationTemplateView',
      component: NotificationTemplateDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'partner-organization',
      name: 'PartnerOrganization',
      component: PartnerOrganization,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'partner-organization/new',
      name: 'PartnerOrganizationCreate',
      component: PartnerOrganizationUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'partner-organization/:partnerOrganizationId/edit',
      name: 'PartnerOrganizationEdit',
      component: PartnerOrganizationUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'partner-organization/:partnerOrganizationId/view',
      name: 'PartnerOrganizationView',
      component: PartnerOrganizationDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'preboarding-follow-up',
      name: 'PreboardingFollowUp',
      component: PreboardingFollowUp,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'preboarding-follow-up/new',
      name: 'PreboardingFollowUpCreate',
      component: PreboardingFollowUpUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'preboarding-follow-up/:preboardingFollowUpId/edit',
      name: 'PreboardingFollowUpEdit',
      component: PreboardingFollowUpUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'preboarding-follow-up/:preboardingFollowUpId/view',
      name: 'PreboardingFollowUpView',
      component: PreboardingFollowUpDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-workflow',
      name: 'RecruitmentWorkflow',
      component: RecruitmentWorkflow,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-application',
      name: 'RecruitmentApplication',
      component: RecruitmentApplication,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-application/new',
      name: 'RecruitmentApplicationCreate',
      component: RecruitmentApplicationUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-application/:recruitmentApplicationId/edit',
      name: 'RecruitmentApplicationEdit',
      component: RecruitmentApplicationUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-application/:recruitmentApplicationId/view',
      name: 'RecruitmentApplicationView',
      component: RecruitmentApplicationDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-evaluation',
      name: 'RecruitmentEvaluation',
      component: RecruitmentEvaluation,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-evaluation/new',
      name: 'RecruitmentEvaluationCreate',
      component: RecruitmentEvaluationUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-evaluation/:recruitmentEvaluationId/edit',
      name: 'RecruitmentEvaluationEdit',
      component: RecruitmentEvaluationUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-evaluation/:recruitmentEvaluationId/view',
      name: 'RecruitmentEvaluationView',
      component: RecruitmentEvaluationDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition',
      name: 'RecruitmentRequisition',
      component: RecruitmentRequisition,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition/new',
      name: 'RecruitmentRequisitionCreate',
      component: RecruitmentRequisitionUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition/:recruitmentRequisitionId/edit',
      name: 'RecruitmentRequisitionEdit',
      component: RecruitmentRequisitionUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition/:recruitmentRequisitionId/view',
      name: 'RecruitmentRequisitionView',
      component: RecruitmentRequisitionDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition-member',
      name: 'RecruitmentRequisitionMember',
      component: RecruitmentRequisitionMember,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition-member/new',
      name: 'RecruitmentRequisitionMemberCreate',
      component: RecruitmentRequisitionMemberUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition-member/:recruitmentRequisitionMemberId/edit',
      name: 'RecruitmentRequisitionMemberEdit',
      component: RecruitmentRequisitionMemberUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition-member/:recruitmentRequisitionMemberId/view',
      name: 'RecruitmentRequisitionMemberView',
      component: RecruitmentRequisitionMemberDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition-version',
      name: 'RecruitmentRequisitionVersion',
      component: RecruitmentRequisitionVersion,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition-version/new',
      name: 'RecruitmentRequisitionVersionCreate',
      component: RecruitmentRequisitionVersionUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition-version/:recruitmentRequisitionVersionId/edit',
      name: 'RecruitmentRequisitionVersionEdit',
      component: RecruitmentRequisitionVersionUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'recruitment-requisition-version/:recruitmentRequisitionVersionId/view',
      name: 'RecruitmentRequisitionVersionView',
      component: RecruitmentRequisitionVersionDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'selection-activity',
      name: 'SelectionActivity',
      component: SelectionActivity,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'selection-activity/new',
      name: 'SelectionActivityCreate',
      component: SelectionActivityUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'selection-activity/:selectionActivityId/edit',
      name: 'SelectionActivityEdit',
      component: SelectionActivityUpdate,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    {
      path: 'selection-activity/:selectionActivityId/view',
      name: 'SelectionActivityView',
      component: SelectionActivityDetails,
      meta: { authorities: [Authority.HR_RECRUITER, Authority.ADMIN] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
