package top.btmdc.hr.service;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionRiskEvaluation;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.repository.PositionRiskEvaluationRepository;
import top.btmdc.hr.repository.StaffSubstitutionRepository;
import top.btmdc.hr.service.dto.PositionRiskEvaluationDTO;
import top.btmdc.hr.service.mapper.PositionRiskEvaluationMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.PositionRiskEvaluation}.
 */
@Service
@Transactional
public class PositionRiskEvaluationService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRiskEvaluationService.class);

    private final PositionRiskEvaluationRepository positionRiskEvaluationRepository;

    private final PositionRepository positionRepository;

    private final PositionAssignmentRepository positionAssignmentRepository;

    private final StaffSubstitutionRepository staffSubstitutionRepository;

    private final PositionRiskEvaluationMapper positionRiskEvaluationMapper;

    public PositionRiskEvaluationService(
        PositionRiskEvaluationRepository positionRiskEvaluationRepository,
        PositionRepository positionRepository,
        PositionAssignmentRepository positionAssignmentRepository,
        StaffSubstitutionRepository staffSubstitutionRepository,
        PositionRiskEvaluationMapper positionRiskEvaluationMapper
    ) {
        this.positionRiskEvaluationRepository = positionRiskEvaluationRepository;
        this.positionRepository = positionRepository;
        this.positionAssignmentRepository = positionAssignmentRepository;
        this.staffSubstitutionRepository = staffSubstitutionRepository;
        this.positionRiskEvaluationMapper = positionRiskEvaluationMapper;
    }

    /**
     * Save a positionRiskEvaluation.
     *
     * @param positionRiskEvaluationDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionRiskEvaluationDTO save(PositionRiskEvaluationDTO positionRiskEvaluationDTO) {
        LOG.debug("Request to save PositionRiskEvaluation : {}", positionRiskEvaluationDTO);
        PositionRiskEvaluation positionRiskEvaluation = positionRiskEvaluationMapper.toEntity(positionRiskEvaluationDTO);
        positionRiskEvaluation = positionRiskEvaluationRepository.save(positionRiskEvaluation);
        return positionRiskEvaluationMapper.toDto(positionRiskEvaluation);
    }

    /**
     * Update a positionRiskEvaluation.
     *
     * @param positionRiskEvaluationDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionRiskEvaluationDTO update(PositionRiskEvaluationDTO positionRiskEvaluationDTO) {
        LOG.debug("Request to update PositionRiskEvaluation : {}", positionRiskEvaluationDTO);
        PositionRiskEvaluation positionRiskEvaluation = positionRiskEvaluationMapper.toEntity(positionRiskEvaluationDTO);
        positionRiskEvaluation = positionRiskEvaluationRepository.save(positionRiskEvaluation);
        return positionRiskEvaluationMapper.toDto(positionRiskEvaluation);
    }

    /**
     * Evaluate position risk based on the unified position risk decision table.
     *
     * @param positionId the position id.
     * @param documentStatus current document status.
     * @param customerOrSystemDependency customer or system dependency level.
     * @param successionReadiness successor readiness.
     * @return the persisted risk evaluation.
     */
    public PositionRiskEvaluationDTO evaluate(
        Long positionId,
        DocumentStatus documentStatus,
        ImportanceLevel customerOrSystemDependency,
        ReadinessLevel successionReadiness
    ) {
        return evaluate(positionId, documentStatus, customerOrSystemDependency, successionReadiness, false);
    }

    public PositionRiskEvaluationDTO evaluate(
        Long positionId,
        DocumentStatus documentStatus,
        ImportanceLevel customerOrSystemDependency,
        ReadinessLevel successionReadiness,
        boolean preview
    ) {
        LOG.debug(
            "Request to evaluate PositionRiskEvaluation : positionId={}, documentStatus={}, customerOrSystemDependency={}, successionReadiness={}, preview={}",
            positionId,
            documentStatus,
            customerOrSystemDependency,
            successionReadiness,
            preview
        );
        if (positionId == null) {
            throw new IllegalArgumentException("positionId is required");
        }

        Position position = positionRepository
            .findById(positionId)
            .orElseThrow(() -> new IllegalArgumentException("position not found: " + positionId));

        int ownerCount = positionAssignmentRepository.findActiveByPositionIdWithPerson(positionId).size();
        int substitutableOwnerCount = Math.toIntExact(staffSubstitutionRepository.countByPositionIdAndSubstitutableTrue(positionId));
        boolean hasSubstitute = substitutableOwnerCount > 0;
        RiskLevel riskLevel = decideRiskLevel(
            position,
            ownerCount,
            hasSubstitute,
            documentStatus,
            customerOrSystemDependency,
            successionReadiness
        );

        PositionRiskEvaluation entity = new PositionRiskEvaluation()
            .position(position)
            .evaluationDate(LocalDate.now())
            .ownerCount(ownerCount)
            .substitutableOwnerCount(substitutableOwnerCount)
            .hasSubstitute(hasSubstitute)
            .documentStatus(documentStatus)
            .customerOrSystemDependency(customerOrSystemDependency)
            .successionReadiness(successionReadiness)
            .riskLevel(riskLevel)
            .riskReason(
                buildRiskReason(
                    position,
                    ownerCount,
                    substitutableOwnerCount,
                    hasSubstitute,
                    documentStatus,
                    customerOrSystemDependency,
                    successionReadiness,
                    riskLevel
                )
            )
            .recommendedAction(recommendedAction(riskLevel));

        if (preview) {
            return positionRiskEvaluationMapper.toDto(entity);
        }
        return positionRiskEvaluationMapper.toDto(positionRiskEvaluationRepository.save(entity));
    }

    /**
     * Partially update a positionRiskEvaluation.
     *
     * @param positionRiskEvaluationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PositionRiskEvaluationDTO> partialUpdate(PositionRiskEvaluationDTO positionRiskEvaluationDTO) {
        LOG.debug("Request to partially update PositionRiskEvaluation : {}", positionRiskEvaluationDTO);

        return positionRiskEvaluationRepository
            .findById(positionRiskEvaluationDTO.getId())
            .map(existingPositionRiskEvaluation -> {
                positionRiskEvaluationMapper.partialUpdate(existingPositionRiskEvaluation, positionRiskEvaluationDTO);

                return existingPositionRiskEvaluation;
            })
            .map(positionRiskEvaluationRepository::save)
            .map(positionRiskEvaluationMapper::toDto);
    }

    /**
     * Get all the positionRiskEvaluations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PositionRiskEvaluationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return positionRiskEvaluationRepository.findAllWithEagerRelationships(pageable).map(positionRiskEvaluationMapper::toDto);
    }

    /**
     * Get one positionRiskEvaluation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PositionRiskEvaluationDTO> findOne(Long id) {
        LOG.debug("Request to get PositionRiskEvaluation : {}", id);
        return positionRiskEvaluationRepository.findOneWithEagerRelationships(id).map(positionRiskEvaluationMapper::toDto);
    }

    /**
     * Delete the positionRiskEvaluation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PositionRiskEvaluation : {}", id);
        positionRiskEvaluationRepository.deleteById(id);
    }

    private RiskLevel decideRiskLevel(
        Position position,
        int ownerCount,
        boolean hasSubstitute,
        DocumentStatus documentStatus,
        ImportanceLevel customerOrSystemDependency,
        ReadinessLevel successionReadiness
    ) {
        int minimumOwnerCount = position.getMinimumOwnerCount() == null ? 0 : position.getMinimumOwnerCount();
        boolean keyPosition = Boolean.TRUE.equals(position.getKeyPosition());
        boolean dependencyHigh = customerOrSystemDependency == ImportanceLevel.HIGH;

        if (ownerCount == 0) {
            return RiskLevel.HIGH;
        }
        if (keyPosition && ownerCount < minimumOwnerCount) {
            return RiskLevel.HIGH;
        }
        if (keyPosition && !hasSubstitute) {
            return RiskLevel.HIGH;
        }
        if ((documentStatus == DocumentStatus.MISSING || documentStatus == DocumentStatus.OUTDATED) && dependencyHigh) {
            return RiskLevel.HIGH;
        }
        if (successionReadiness == ReadinessLevel.NONE && dependencyHigh) {
            return RiskLevel.HIGH;
        }
        if (!keyPosition && ownerCount < minimumOwnerCount) {
            return RiskLevel.MEDIUM;
        }
        if (!hasSubstitute) {
            return RiskLevel.MEDIUM;
        }
        if (successionReadiness == ReadinessLevel.THREE_MONTHS || successionReadiness == ReadinessLevel.SIX_TO_TWELVE_MONTHS) {
            return RiskLevel.MEDIUM;
        }
        if (documentStatus == DocumentStatus.PARTIAL || customerOrSystemDependency == ImportanceLevel.MEDIUM) {
            return RiskLevel.MEDIUM;
        }
        if (ownerCount >= minimumOwnerCount && hasSubstitute && documentStatus == DocumentStatus.AVAILABLE) {
            return RiskLevel.LOW;
        }
        return RiskLevel.UNKNOWN;
    }

    private String buildRiskReason(
        Position position,
        int ownerCount,
        int substitutableOwnerCount,
        boolean hasSubstitute,
        DocumentStatus documentStatus,
        ImportanceLevel customerOrSystemDependency,
        ReadinessLevel successionReadiness,
        RiskLevel riskLevel
    ) {
        return "Position %s has %d active owner(s), minimumOwnerCount=%s, %d valid substitute candidate(s), keyPosition=%s, documentStatus=%s, customerOrSystemDependency=%s, successionReadiness=%s; evaluated risk=%s.".formatted(
            position.getPositionName(),
            ownerCount,
            position.getMinimumOwnerCount(),
            substitutableOwnerCount,
            Boolean.TRUE.equals(position.getKeyPosition()),
            documentStatus,
            customerOrSystemDependency,
            successionReadiness,
            riskLevel
        );
    }

    private String recommendedAction(RiskLevel riskLevel) {
        return switch (riskLevel) {
            case HIGH -> "Immediately establish a second owner, minimum handover documentation, and a cultivation plan.";
            case MEDIUM -> "Arrange shadow learning, skill gap improvement, documentation cleanup, and periodic review.";
            case LOW -> "Review periodically to avoid stale skill, document, and substitution data.";
            case UNKNOWN -> "Collect more assignment, document, dependency, and successor readiness evidence before evaluation.";
        };
    }
}
