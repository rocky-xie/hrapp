package top.btmdc.hr.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionAssignment;
import top.btmdc.hr.domain.PositionRiskEvaluation;
import top.btmdc.hr.domain.PositionSkillRequirement;
import top.btmdc.hr.domain.StaffSubstitution;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.repository.*;
import top.btmdc.hr.service.dto.report.*;

@Service
@Transactional
public class SkillGapReportService {

    private static final Logger LOG = LoggerFactory.getLogger(SkillGapReportService.class);

    private final PositionRepository positionRepository;
    private final PositionSkillRequirementRepository positionSkillRequirementRepository;
    private final PersonSkillRepository personSkillRepository;
    private final PositionAssignmentRepository positionAssignmentRepository;
    private final StaffSubstitutionRepository staffSubstitutionRepository;
    private final PositionRiskEvaluationRepository positionRiskEvaluationRepository;

    public SkillGapReportService(
        PositionRepository positionRepository,
        PositionSkillRequirementRepository positionSkillRequirementRepository,
        PersonSkillRepository personSkillRepository,
        PositionAssignmentRepository positionAssignmentRepository,
        StaffSubstitutionRepository staffSubstitutionRepository,
        PositionRiskEvaluationRepository positionRiskEvaluationRepository
    ) {
        this.positionRepository = positionRepository;
        this.positionSkillRequirementRepository = positionSkillRequirementRepository;
        this.personSkillRepository = personSkillRepository;
        this.positionAssignmentRepository = positionAssignmentRepository;
        this.staffSubstitutionRepository = staffSubstitutionRepository;
        this.positionRiskEvaluationRepository = positionRiskEvaluationRepository;
    }

    @Transactional(readOnly = true)
    public SkillGapReportDTO generateReport(List<Long> positionIds, ReportCriteria criteria) {
        LOG.debug("Generating skill gap report for positionIds: {}", positionIds);
        if (criteria == null) {
            criteria = new ReportCriteria();
        }

        List<Position> positions = positionRepository.findAllById(positionIds);
        List<PositionGapDTO> positionGaps = new ArrayList<>();

        for (Position position : positions) {
            positionGaps.add(buildPositionGap(position, criteria));
        }

        SkillGapReportDTO report = new SkillGapReportDTO();
        report.setReportDate(LocalDate.now());
        report.setTotalPositions(positionGaps.size());
        report.setPositions(positionGaps);
        return report;
    }

    private PositionGapDTO buildPositionGap(Position position, ReportCriteria criteria) {
        List<PositionSkillRequirement> requirements = positionSkillRequirementRepository.findByPositionIdWithSkillAndRequiredLevel(
            position.getId()
        );

        if (criteria.getMinImportance() != null) {
            requirements = requirements
                .stream()
                .filter(r -> r.getImportance() != null && r.getImportance().ordinal() <= criteria.getMinImportance().ordinal())
                .toList();
        }

        PositionGapDTO gap = new PositionGapDTO();
        gap.setPositionId(position.getId());
        gap.setPositionName(position.getPositionName());
        gap.setTotalRequiredSkills(requirements.size());
        gap.setRiskLevel(findRiskLevel(position.getId()));

        if (criteria.isIncludeOwners()) {
            gap.setOwners(buildOwnerGaps(position.getId(), requirements));
        }
        if (criteria.isIncludeCandidates()) {
            gap.setCandidates(buildCandidateGaps(position.getId(), requirements));
        }

        List<AggregatedGapDTO> aggregated = new ArrayList<>();
        Map<Long, AggregatedGapDTO> aggMap = new HashMap<>();
        for (PersonGapDTO personGap : concat(gap.getOwners(), gap.getCandidates())) {
            for (SkillGapDTO skillGap : personGap.getGaps()) {
                AggregatedGapDTO agg = aggMap.computeIfAbsent(skillGap.getSkillId(), k -> {
                    AggregatedGapDTO a = new AggregatedGapDTO();
                    a.setSkillId(skillGap.getSkillId());
                    a.setSkillName(skillGap.getSkillName());
                    a.setImportance(skillGap.getImportance());
                    a.setRequiredLevelCode(skillGap.getRequiredLevelCode());
                    a.setRequiredLevelSortOrder(skillGap.getRequiredLevelSortOrder());
                    return a;
                });
                agg.setTotalDeficient(agg.getTotalDeficient() + 1);
                int deficit =
                    skillGap.getCurrentLevelSortOrder() == null
                        ? skillGap.getRequiredLevelSortOrder()
                        : skillGap.getRequiredLevelSortOrder() - skillGap.getCurrentLevelSortOrder();
                if (deficit > agg.getMaxDeficitLevel()) {
                    agg.setMaxDeficitLevel(deficit);
                }
            }
        }
        aggregated.addAll(aggMap.values());
        aggregated.sort(Comparator.comparingInt(AggregatedGapDTO::getTotalDeficient).reversed());
        gap.setAggregatedGaps(aggregated);

        return gap;
    }

    private List<PersonGapDTO> buildOwnerGaps(Long positionId, List<PositionSkillRequirement> requirements) {
        List<PositionAssignment> assignments = positionAssignmentRepository.findActiveByPositionIdWithPerson(positionId);
        List<PersonGapDTO> result = new ArrayList<>();
        for (PositionAssignment assignment : assignments) {
            Person person = assignment.getPerson();
            if (person != null) {
                result.add(buildPersonGap(person, requirements));
            }
        }
        return result;
    }

    private List<PersonGapDTO> buildCandidateGaps(Long positionId, List<PositionSkillRequirement> requirements) {
        List<StaffSubstitution> substitutions = staffSubstitutionRepository.findByPositionIdWithPerson(positionId);
        List<PersonGapDTO> result = new ArrayList<>();
        for (StaffSubstitution sub : substitutions) {
            Person person = sub.getCandidatePerson();
            if (person != null) {
                result.add(buildPersonGap(person, requirements));
            }
        }
        return result;
    }

    private PersonGapDTO buildPersonGap(Person person, List<PositionSkillRequirement> requirements) {
        Map<Long, PersonSkill> personSkills = personSkillRepository
            .findByPersonIdWithSkillAndLevel(person.getId())
            .stream()
            .filter(ps -> ps.getSkill() != null && ps.getSkill().getId() != null)
            .collect(Collectors.toMap(ps -> ps.getSkill().getId(), Function.identity(), this::pickHigherLevel));

        List<SkillGapDTO> gaps = new ArrayList<>();
        int coveredCount = 0;

        for (PositionSkillRequirement req : requirements) {
            PersonSkill ps = req.getSkill() == null ? null : personSkills.get(req.getSkill().getId());
            int personLevel = levelValue(ps);
            int reqLevel = requiredLevelValue(req);

            if (personLevel >= reqLevel) {
                coveredCount++;
            } else {
                SkillGapDTO gap = new SkillGapDTO();
                if (req.getSkill() != null) {
                    gap.setSkillId(req.getSkill().getId());
                    gap.setSkillName(req.getSkill().getSkillName());
                }
                if (req.getRequiredLevel() != null) {
                    gap.setRequiredLevelCode(req.getRequiredLevel().getCode() != null ? req.getRequiredLevel().getCode().name() : null);
                    gap.setRequiredLevelSortOrder(req.getRequiredLevel().getSortOrder());
                }
                if (ps != null && ps.getCurrentLevel() != null) {
                    gap.setCurrentLevelCode(ps.getCurrentLevel().getCode() != null ? ps.getCurrentLevel().getCode().name() : null);
                    gap.setCurrentLevelSortOrder(ps.getCurrentLevel().getSortOrder());
                }
                gap.setImportance(req.getImportance());
                gaps.add(gap);
            }
        }

        int total = requirements.size();
        BigDecimal coverageRate =
            total == 0
                ? BigDecimal.valueOf(100)
                : BigDecimal.valueOf(coveredCount)
                      .multiply(BigDecimal.valueOf(100))
                      .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);

        PersonGapDTO dto = new PersonGapDTO();
        dto.setPersonId(person.getId());
        dto.setPersonName(person.getPersonName());
        dto.setTotalRequired(total);
        dto.setCoveredCount(coveredCount);
        dto.setCoverageRate(coverageRate);
        dto.setGaps(gaps);
        return dto;
    }

    private RiskLevel findRiskLevel(Long positionId) {
        return positionRiskEvaluationRepository
            .findFirstByPositionIdOrderByEvaluationDateDescIdDesc(positionId)
            .map(PositionRiskEvaluation::getRiskLevel)
            .orElse(null);
    }

    private PersonSkill pickHigherLevel(PersonSkill first, PersonSkill second) {
        return Comparator.comparingInt(this::levelValue).compare(first, second) >= 0 ? first : second;
    }

    private int levelValue(PersonSkill personSkill) {
        if (personSkill == null || personSkill.getCurrentLevel() == null || personSkill.getCurrentLevel().getSortOrder() == null) {
            return -1;
        }
        return personSkill.getCurrentLevel().getSortOrder();
    }

    private int requiredLevelValue(PositionSkillRequirement requirement) {
        if (requirement == null || requirement.getRequiredLevel() == null || requirement.getRequiredLevel().getSortOrder() == null) {
            return -1;
        }
        return requirement.getRequiredLevel().getSortOrder();
    }

    private List<PersonGapDTO> concat(List<PersonGapDTO> a, List<PersonGapDTO> b) {
        List<PersonGapDTO> result = new ArrayList<>();
        if (a != null) result.addAll(a);
        if (b != null) result.addAll(b);
        return result;
    }
}
