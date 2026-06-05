package top.btmdc.hr.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.Evaluation;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionAssignment;
import top.btmdc.hr.domain.PositionRiskEvaluation;
import top.btmdc.hr.domain.StaffSubstitution;
import top.btmdc.hr.domain.SuccessionCandidate;
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.domain.enumeration.PlanStatus;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.repository.EvaluationRepository;
import top.btmdc.hr.repository.PersonRepository;
import top.btmdc.hr.repository.PersonSkillRepository;
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.repository.PositionRiskEvaluationRepository;
import top.btmdc.hr.repository.StaffSubstitutionRepository;
import top.btmdc.hr.repository.SuccessionCandidateRepository;
import top.btmdc.hr.repository.TrainingGoalRepository;
import top.btmdc.hr.service.dto.DataQualityIssueDTO;

@Service
@Transactional(readOnly = true)
public class DataQualityService {

    private static final Logger LOG = LoggerFactory.getLogger(DataQualityService.class);

    private final PersonRepository personRepository;
    private final PositionRepository positionRepository;
    private final PositionAssignmentRepository positionAssignmentRepository;
    private final TrainingGoalRepository trainingGoalRepository;
    private final StaffSubstitutionRepository staffSubstitutionRepository;
    private final PersonSkillRepository personSkillRepository;
    private final PositionRiskEvaluationRepository positionRiskEvaluationRepository;
    private final SuccessionCandidateRepository successionCandidateRepository;
    private final EvaluationRepository evaluationRepository;

    public DataQualityService(
        PersonRepository personRepository,
        PositionRepository positionRepository,
        PositionAssignmentRepository positionAssignmentRepository,
        TrainingGoalRepository trainingGoalRepository,
        StaffSubstitutionRepository staffSubstitutionRepository,
        PersonSkillRepository personSkillRepository,
        PositionRiskEvaluationRepository positionRiskEvaluationRepository,
        SuccessionCandidateRepository successionCandidateRepository,
        EvaluationRepository evaluationRepository
    ) {
        this.personRepository = personRepository;
        this.positionRepository = positionRepository;
        this.positionAssignmentRepository = positionAssignmentRepository;
        this.trainingGoalRepository = trainingGoalRepository;
        this.staffSubstitutionRepository = staffSubstitutionRepository;
        this.personSkillRepository = personSkillRepository;
        this.positionRiskEvaluationRepository = positionRiskEvaluationRepository;
        this.successionCandidateRepository = successionCandidateRepository;
        this.evaluationRepository = evaluationRepository;
    }

    public List<DataQualityIssueDTO> runAllChecks() {
        LOG.debug("Running all data quality checks");
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        issues.addAll(checkPersons());
        issues.addAll(checkPositions());
        issues.addAll(checkPositionAssignments());
        issues.addAll(checkTrainingGoals());
        issues.addAll(checkStaffSubstitutions());
        issues.addAll(checkPersonSkills());
        issues.addAll(checkPositionRiskEvaluations());
        issues.addAll(checkSuccessionCandidates());
        issues.addAll(checkEvaluations());
        return issues;
    }

    List<DataQualityIssueDTO> checkPersons() {
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        for (Person person : personRepository.findAll()) {
            String label = person.getPersonName() != null ? person.getPersonName() : "ID: " + person.getId();
            if (person.getPersonName() == null || person.getPersonName().isBlank()) {
                issues.add(createIssue("Person", "ERROR", "personName", person.getId(), label, "personName is missing"));
            }
            if (person.getJoinDate() == null) {
                issues.add(createIssue("Person", "WARNING", "joinDate", person.getId(), label, "joinDate is not set"));
            }
            if (person.getDepartment() == null || person.getDepartment().isBlank()) {
                issues.add(createIssue("Person", "WARNING", "department", person.getId(), label, "department is not set"));
            }
        }
        return issues;
    }

    List<DataQualityIssueDTO> checkPositions() {
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        for (Position position : positionRepository.findAll()) {
            String label = position.getPositionName() != null ? position.getPositionName() : "ID: " + position.getId();
            if (position.getPositionName() == null || position.getPositionName().isBlank()) {
                issues.add(createIssue("Position", "ERROR", "positionName", position.getId(), label, "positionName is missing"));
            }
            if (Boolean.TRUE.equals(position.getActive()) && position.getPlannedHeadcount() == null) {
                issues.add(
                    createIssue(
                        "Position",
                        "WARNING",
                        "plannedHeadcount",
                        position.getId(),
                        label,
                        "active position has no plannedHeadcount"
                    )
                );
            }
            if (Boolean.TRUE.equals(position.getKeyPosition()) && position.getReviewCycle() == null) {
                issues.add(createIssue("Position", "WARNING", "reviewCycle", position.getId(), label, "key position has no reviewCycle"));
            }
        }
        return issues;
    }

    List<DataQualityIssueDTO> checkPositionAssignments() {
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (PositionAssignment assignment : positionAssignmentRepository.findAllWithEagerRelationships()) {
            String label = assignment.getPerson() != null ? assignment.getPerson().getPersonName() : "ID: " + assignment.getId();
            if (assignment.getStartDate() == null) {
                issues.add(createIssue("PositionAssignment", "WARNING", "startDate", assignment.getId(), label, "startDate is not set"));
            }
            if (Boolean.TRUE.equals(assignment.getActive()) && assignment.getEndDate() != null && assignment.getEndDate().isBefore(today)) {
                issues.add(
                    createIssue(
                        "PositionAssignment",
                        "ERROR",
                        "endDate",
                        assignment.getId(),
                        label,
                        "assignment is active but endDate is in the past"
                    )
                );
            }
            if (Boolean.FALSE.equals(assignment.getActive()) && assignment.getEndDate() == null) {
                issues.add(
                    createIssue("PositionAssignment", "WARNING", "endDate", assignment.getId(), label, "inactive assignment has no endDate")
                );
            }
        }
        return issues;
    }

    List<DataQualityIssueDTO> checkTrainingGoals() {
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        LocalDate today = LocalDate.now();
        List<TrainingGoal> goals = trainingGoalRepository.findAllWithEagerRelationships();
        for (TrainingGoal goal : goals) {
            String label = goal.getGoalName() != null ? goal.getGoalName() : "ID: " + goal.getId();
            if (goal.getTargetDate() == null) {
                issues.add(createIssue("TrainingGoal", "WARNING", "targetDate", goal.getId(), label, "targetDate is not set"));
            }
            if (
                goal.getTargetDate() != null &&
                goal.getTargetDate().isBefore(today) &&
                goal.getStatus() != PlanStatus.COMPLETED &&
                goal.getStatus() != PlanStatus.CANCELLED
            ) {
                issues.add(
                    createIssue(
                        "TrainingGoal",
                        "ERROR",
                        "targetDate",
                        goal.getId(),
                        label,
                        "targetDate is past but goal is not completed or cancelled"
                    )
                );
            }
            if (goal.getPerson() == null && goal.getPosition() == null) {
                issues.add(createIssue("TrainingGoal", "WARNING", "RELATIONSHIP", goal.getId(), label, "no person or position linked"));
            }
            if (goal.getTargetLevel() == null) {
                issues.add(createIssue("TrainingGoal", "WARNING", "targetLevel", goal.getId(), label, "targetLevel is not set"));
            }
        }
        return issues;
    }

    List<DataQualityIssueDTO> checkStaffSubstitutions() {
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        LocalDate today = LocalDate.now();
        List<StaffSubstitution> substitutions = staffSubstitutionRepository.findAllWithEagerRelationships();
        for (StaffSubstitution sub : substitutions) {
            String label = sub.getCandidatePerson() != null ? sub.getCandidatePerson().getPersonName() : "ID: " + sub.getId();
            if (sub.getExpiryDate() != null && sub.getExpiryDate().isBefore(today)) {
                issues.add(createIssue("StaffSubstitution", "WARNING", "expiryDate", sub.getId(), label, "substitution has expired"));
            }
            if (sub.getReviewDate() != null && sub.getReviewDate().isBefore(today)) {
                issues.add(createIssue("StaffSubstitution", "WARNING", "reviewDate", sub.getId(), label, "reviewDate is overdue"));
            }
        }
        return issues;
    }

    List<DataQualityIssueDTO> checkPersonSkills() {
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        LocalDate today = LocalDate.now();
        List<PersonSkill> skills = personSkillRepository.findAllWithEagerRelationships();
        for (PersonSkill ps : skills) {
            String label = ps.getPerson() != null ? ps.getPerson().getPersonName() : "ID: " + ps.getId();
            if (ps.getNextReviewDate() != null && ps.getNextReviewDate().isBefore(today)) {
                issues.add(createIssue("PersonSkill", "WARNING", "nextReviewDate", ps.getId(), label, "nextReviewDate is overdue"));
            }
        }
        return issues;
    }

    List<DataQualityIssueDTO> checkPositionRiskEvaluations() {
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        LocalDate today = LocalDate.now();
        List<PositionRiskEvaluation> evaluations = positionRiskEvaluationRepository.findAllWithEagerRelationships();
        for (PositionRiskEvaluation eval : evaluations) {
            String label = eval.getPosition() != null ? eval.getPosition().getPositionName() : "ID: " + eval.getId();
            if (eval.getEvaluationDate() != null && eval.getEvaluationDate().isBefore(today.minusMonths(6))) {
                issues.add(
                    createIssue(
                        "PositionRiskEvaluation",
                        "WARNING",
                        "evaluationDate",
                        eval.getId(),
                        label,
                        "evaluation is over 6 months old"
                    )
                );
            }
            if (eval.getRiskLevel() == RiskLevel.HIGH && (eval.getRiskReason() == null || eval.getRiskReason().isBlank())) {
                issues.add(
                    createIssue("PositionRiskEvaluation", "ERROR", "riskReason", eval.getId(), label, "HIGH risk but riskReason is missing")
                );
            }
        }
        return issues;
    }

    List<DataQualityIssueDTO> checkSuccessionCandidates() {
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        LocalDate today = LocalDate.now();
        List<SuccessionCandidate> candidates = successionCandidateRepository.findAllWithEagerRelationships();
        for (SuccessionCandidate sc : candidates) {
            String label = sc.getCandidate() != null ? sc.getCandidate().getPersonName() : "ID: " + sc.getId();
            if (sc.getReviewDate() != null && sc.getReviewDate().isBefore(today)) {
                issues.add(createIssue("SuccessionCandidate", "WARNING", "reviewDate", sc.getId(), label, "reviewDate is overdue"));
            }
            if (sc.getReviewDate() == null) {
                issues.add(createIssue("SuccessionCandidate", "INFO", "reviewDate", sc.getId(), label, "reviewDate is not set"));
            }
        }
        return issues;
    }

    List<DataQualityIssueDTO> checkEvaluations() {
        List<DataQualityIssueDTO> issues = new ArrayList<>();
        List<Evaluation> evaluations = evaluationRepository.findAllWithEagerRelationships();
        for (Evaluation eval : evaluations) {
            String label = eval.getEvaluationName() != null ? eval.getEvaluationName() : "ID: " + eval.getId();
            if (eval.getResult() == null) {
                issues.add(createIssue("Evaluation", "WARNING", "result", eval.getId(), label, "result is not set"));
            }
            if (eval.getEvaluator() == null) {
                issues.add(createIssue("Evaluation", "INFO", "evaluator", eval.getId(), label, "evaluator is not set"));
            }
        }
        return issues;
    }

    private DataQualityIssueDTO createIssue(
        String entityType,
        String severity,
        String field,
        Long entityId,
        String entityLabel,
        String message
    ) {
        DataQualityIssueDTO issue = new DataQualityIssueDTO();
        issue.setEntityType(entityType);
        issue.setSeverity(severity);
        issue.setField(field);
        issue.setEntityId(entityId);
        issue.setEntityLabel(entityLabel);
        issue.setMessage(message);
        return issue;
    }
}
