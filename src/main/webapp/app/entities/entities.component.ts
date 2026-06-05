import { defineComponent, provide } from 'vue';

import UserService from '@/entities/user/user.service';

import CandidateProfileService from './candidate-profile/candidate-profile.service';
import EvaluationService from './evaluation/evaluation.service';
import ImprovementPlanService from './improvement-plan/improvement-plan.service';
import KeyResponsibilityCategoryService from './key-responsibility-category/key-responsibility-category.service';
import PersonService from './person/person.service';
import PersonRiskService from './person-risk/person-risk.service';
import PersonSkillService from './person-skill/person-skill.service';
import PositionService from './position/position.service';
import PositionAssignmentService from './position-assignment/position-assignment.service';
import PositionMatchService from './position-match/position-match.service';
import PositionSkillRequirementService from './position-skill-requirement/position-skill-requirement.service';
import SkillService from './skill/skill.service';
import SkillAssessmentService from './skill-assessment/skill-assessment.service';
import SkillLevelService from './skill-level/skill-level.service';
import SkillUpgradeRecordService from './skill-upgrade-record/skill-upgrade-record.service';
import StaffSubstitutionService from './staff-substitution/staff-substitution.service';
import SuccessionCandidateService from './succession-candidate/succession-candidate.service';
import PositionRiskService from './position-risk/position-risk.service';
import PositionRiskEvaluationService from './position-risk-evaluation/position-risk-evaluation.service';
import TrainingGoalService from './training-goal/training-goal.service';
import TrainingRecordService from './training-record/training-record.service';
import TrustObservationService from './trust-observation/trust-observation.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('positionService', () => new PositionService());
    provide('personService', () => new PersonService());
    provide('skillService', () => new SkillService());
    provide('skillLevelService', () => new SkillLevelService());
    provide('personSkillService', () => new PersonSkillService());
    provide('positionSkillRequirementService', () => new PositionSkillRequirementService());
    provide('positionAssignmentService', () => new PositionAssignmentService());
    provide('positionMatchService', () => new PositionMatchService());
    provide('successionCandidateService', () => new SuccessionCandidateService());
    provide('positionRiskService', () => new PositionRiskService());
    provide('personRiskService', () => new PersonRiskService());
    provide('skillUpgradeRecordService', () => new SkillUpgradeRecordService());
    provide('staffSubstitutionService', () => new StaffSubstitutionService());
    provide('positionRiskEvaluationService', () => new PositionRiskEvaluationService());
    provide('trainingGoalService', () => new TrainingGoalService());
    provide('trainingRecordService', () => new TrainingRecordService());
    provide('skillAssessmentService', () => new SkillAssessmentService());
    provide('trustObservationService', () => new TrustObservationService());
    provide('evaluationService', () => new EvaluationService());
    provide('improvementPlanService', () => new ImprovementPlanService());
    provide('keyResponsibilityCategoryService', () => new KeyResponsibilityCategoryService());
    provide('candidateProfileService', () => new CandidateProfileService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
