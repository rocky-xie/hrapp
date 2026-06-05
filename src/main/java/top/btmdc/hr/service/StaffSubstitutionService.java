package top.btmdc.hr.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionSkillRequirement;
import top.btmdc.hr.domain.StaffSubstitution;
import top.btmdc.hr.domain.enumeration.RequirementImportance;
import top.btmdc.hr.repository.PersonRepository;
import top.btmdc.hr.repository.PersonSkillRepository;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.repository.PositionSkillRequirementRepository;
import top.btmdc.hr.repository.StaffSubstitutionRepository;
import top.btmdc.hr.service.dto.StaffSubstitutionDTO;
import top.btmdc.hr.service.mapper.StaffSubstitutionMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.StaffSubstitution}.
 */
@Service
@Transactional
public class StaffSubstitutionService {

    private static final Logger LOG = LoggerFactory.getLogger(StaffSubstitutionService.class);

    private final StaffSubstitutionRepository staffSubstitutionRepository;

    private final PositionRepository positionRepository;

    private final PersonRepository personRepository;

    private final PositionSkillRequirementRepository positionSkillRequirementRepository;

    private final PersonSkillRepository personSkillRepository;

    private final StaffSubstitutionMapper staffSubstitutionMapper;

    public StaffSubstitutionService(
        StaffSubstitutionRepository staffSubstitutionRepository,
        PositionRepository positionRepository,
        PersonRepository personRepository,
        PositionSkillRequirementRepository positionSkillRequirementRepository,
        PersonSkillRepository personSkillRepository,
        StaffSubstitutionMapper staffSubstitutionMapper
    ) {
        this.staffSubstitutionRepository = staffSubstitutionRepository;
        this.positionRepository = positionRepository;
        this.personRepository = personRepository;
        this.positionSkillRequirementRepository = positionSkillRequirementRepository;
        this.personSkillRepository = personSkillRepository;
        this.staffSubstitutionMapper = staffSubstitutionMapper;
    }

    /**
     * Save a staffSubstitution.
     *
     * @param staffSubstitutionDTO the entity to save.
     * @return the persisted entity.
     */
    public StaffSubstitutionDTO save(StaffSubstitutionDTO staffSubstitutionDTO) {
        LOG.debug("Request to save StaffSubstitution : {}", staffSubstitutionDTO);
        StaffSubstitution staffSubstitution = staffSubstitutionMapper.toEntity(staffSubstitutionDTO);
        staffSubstitution = staffSubstitutionRepository.save(staffSubstitution);
        return staffSubstitutionMapper.toDto(staffSubstitution);
    }

    /**
     * Update a staffSubstitution.
     *
     * @param staffSubstitutionDTO the entity to save.
     * @return the persisted entity.
     */
    public StaffSubstitutionDTO update(StaffSubstitutionDTO staffSubstitutionDTO) {
        LOG.debug("Request to update StaffSubstitution : {}", staffSubstitutionDTO);
        StaffSubstitution staffSubstitution = staffSubstitutionMapper.toEntity(staffSubstitutionDTO);
        staffSubstitution = staffSubstitutionRepository.save(staffSubstitution);
        return staffSubstitutionMapper.toDto(staffSubstitution);
    }

    /**
     * Calculate whether a candidate person can substitute a position based on required skill coverage.
     *
     * @param positionId the target position id.
     * @param candidatePersonId the candidate person id.
     * @param thresholdRate the minimum coverage rate, defaults to 80 when null.
     * @return the persisted substitution evaluation.
     */
    public StaffSubstitutionDTO calculate(Long positionId, Long candidatePersonId, BigDecimal thresholdRate) {
        LOG.debug(
            "Request to calculate StaffSubstitution : positionId={}, candidatePersonId={}, thresholdRate={}",
            positionId,
            candidatePersonId,
            thresholdRate
        );

        if (positionId == null || candidatePersonId == null) {
            throw new IllegalArgumentException("positionId and candidatePersonId are required");
        }

        BigDecimal effectiveThreshold = thresholdRate == null ? BigDecimal.valueOf(80) : thresholdRate;
        Position position = positionRepository
            .findById(positionId)
            .orElseThrow(() -> new IllegalArgumentException("position not found: " + positionId));
        Person candidatePerson = personRepository
            .findById(candidatePersonId)
            .orElseThrow(() -> new IllegalArgumentException("candidatePerson not found: " + candidatePersonId));

        Map<Long, PersonSkill> candidateSkillsBySkillId = personSkillRepository
            .findByPersonIdWithSkillAndLevel(candidatePersonId)
            .stream()
            .filter(personSkill -> personSkill.getSkill() != null && personSkill.getSkill().getId() != null)
            .collect(Collectors.toMap(personSkill -> personSkill.getSkill().getId(), Function.identity(), this::pickHigherLevel));

        List<PositionSkillRequirement> requirements = positionSkillRequirementRepository.findByPositionIdWithSkillAndRequiredLevel(
            positionId
        );
        int totalSkillCount = requirements.size();
        int coveredSkillCount = 0;
        int requiredSkillCount = 0;
        int coveredRequiredCount = 0;
        StringBuilder missingSkills = new StringBuilder();
        boolean anyRequiredMissing = false;

        for (PositionSkillRequirement requirement : requirements) {
            PersonSkill candidateSkill =
                requirement.getSkill() == null ? null : candidateSkillsBySkillId.get(requirement.getSkill().getId());
            boolean isCovered = candidateSkill != null && levelValue(candidateSkill) >= requiredLevelValue(requirement);

            if (requirement.getImportance() == RequirementImportance.REQUIRED) {
                requiredSkillCount++;
                if (isCovered) {
                    coveredRequiredCount++;
                } else {
                    anyRequiredMissing = true;
                    appendMissingSkill(missingSkills, requirement);
                }
            } else if (isCovered) {
                coveredSkillCount++;
            } else {
                appendMissingSkill(missingSkills, requirement);
            }
        }

        BigDecimal coverageRate;
        if (totalSkillCount == 0) {
            coverageRate = BigDecimal.valueOf(100);
        } else if (anyRequiredMissing) {
            coverageRate = BigDecimal.valueOf(0);
            coveredSkillCount = coveredRequiredCount;
        } else {
            coveredSkillCount += coveredRequiredCount;
            coverageRate = BigDecimal.valueOf(coveredSkillCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalSkillCount), 2, RoundingMode.HALF_UP);
        }

        LocalDate evaluationDate = LocalDate.now();
        boolean substitutable = coverageRate.compareTo(effectiveThreshold) >= 0;
        String missingSkillsText = missingSkills.toString();
        StaffSubstitution staffSubstitution = staffSubstitutionRepository
            .findOneByPositionIdAndCandidatePersonId(positionId, candidatePersonId)
            .orElseGet(() -> new StaffSubstitution().position(position).candidatePerson(candidatePerson));
        staffSubstitution
            .coverageRate(coverageRate)
            .thresholdRate(effectiveThreshold)
            .totalSkillCount(totalSkillCount)
            .coveredSkillCount(coveredSkillCount)
            .missingSkills(missingSkillsText)
            .substitutable(substitutable)
            .evaluationDate(evaluationDate)
            .reason(
                appendReasonHistory(
                    staffSubstitution.getReason(),
                    evaluationDate,
                    coveredSkillCount,
                    totalSkillCount,
                    coverageRate,
                    effectiveThreshold,
                    missingSkillsText,
                    substitutable,
                    anyRequiredMissing,
                    totalSkillCount == 0
                )
            );

        return staffSubstitutionMapper.toDto(staffSubstitutionRepository.save(staffSubstitution));
    }

    /**
     * Partially update a staffSubstitution.
     *
     * @param staffSubstitutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StaffSubstitutionDTO> partialUpdate(StaffSubstitutionDTO staffSubstitutionDTO) {
        LOG.debug("Request to partially update StaffSubstitution : {}", staffSubstitutionDTO);

        return staffSubstitutionRepository
            .findById(staffSubstitutionDTO.getId())
            .map(existingStaffSubstitution -> {
                staffSubstitutionMapper.partialUpdate(existingStaffSubstitution, staffSubstitutionDTO);

                return existingStaffSubstitution;
            })
            .map(staffSubstitutionRepository::save)
            .map(staffSubstitutionMapper::toDto);
    }

    /**
     * Get all the staffSubstitutions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<StaffSubstitutionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return staffSubstitutionRepository.findAllWithEagerRelationships(pageable).map(staffSubstitutionMapper::toDto);
    }

    /**
     * Get one staffSubstitution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StaffSubstitutionDTO> findOne(Long id) {
        LOG.debug("Request to get StaffSubstitution : {}", id);
        return staffSubstitutionRepository.findOneWithEagerRelationships(id).map(staffSubstitutionMapper::toDto);
    }

    /**
     * Delete the staffSubstitution by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete StaffSubstitution : {}", id);
        staffSubstitutionRepository.deleteById(id);
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

    private void appendMissingSkill(StringBuilder missingSkills, PositionSkillRequirement requirement) {
        if (!missingSkills.isEmpty()) {
            missingSkills.append(System.lineSeparator());
        }
        if (requirement.getSkill() == null) {
            missingSkills.append("UNKNOWN");
            return;
        }
        missingSkills.append(requirement.getSkill().getSkillName());
        if (requirement.getRequiredLevel() != null && requirement.getRequiredLevel().getCode() != null) {
            missingSkills.append(" >= ").append(requirement.getRequiredLevel().getCode());
        }
    }

    private String appendReasonHistory(
        String existingReason,
        LocalDate evaluationDate,
        int coveredSkillCount,
        int totalSkillCount,
        BigDecimal coverageRate,
        BigDecimal thresholdRate,
        String missingSkills,
        boolean substitutable,
        boolean anyRequiredMissing,
        boolean noRequirements
    ) {
        String status;
        if (noRequirements) {
            status = "INSUFFICIENT_REQUIREMENT_DATA";
        } else if (anyRequiredMissing) {
            status = "MISSING_REQUIRED_SKILL — " + (substitutable ? "SUBSTITUTABLE" : "NOT_SUBSTITUTABLE");
        } else {
            status = substitutable ? "SUBSTITUTABLE" : "NOT_SUBSTITUTABLE";
        }
        String line = "[%s] coverage=%s%%, covered=%d/%d, threshold=%s%%, missing=%s, result=%s.".formatted(
            evaluationDate,
            coverageRate.stripTrailingZeros().toPlainString(),
            coveredSkillCount,
            totalSkillCount,
            thresholdRate.stripTrailingZeros().toPlainString(),
            missingSkills == null || missingSkills.isBlank() ? "NONE" : missingSkills.replace(System.lineSeparator(), "/"),
            status
        );
        if (noRequirements) {
            line += " Position has no skill requirements defined.";
        } else if (anyRequiredMissing) {
            line += " REQUIRED skills not all met — coverage set to 0%.";
        }
        if (existingReason == null || existingReason.isBlank()) {
            return line;
        }
        return existingReason + System.lineSeparator() + line;
    }
}
