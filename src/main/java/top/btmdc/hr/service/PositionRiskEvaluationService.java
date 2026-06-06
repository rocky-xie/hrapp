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
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.repository.PositionRiskEvaluationRepository;
import top.btmdc.hr.repository.StaffSubstitutionRepository;
import top.btmdc.hr.service.dto.PositionRiskEvaluationDTO;
import top.btmdc.hr.service.mapper.PositionRiskEvaluationMapper;
import top.btmdc.hr.service.positionrisk.PositionRiskDecision;
import top.btmdc.hr.service.positionrisk.PositionRiskInput;
import top.btmdc.hr.service.positionrisk.PositionRiskRuleEngine;

@Service
@Transactional
public class PositionRiskEvaluationService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRiskEvaluationService.class);

    private final PositionRiskEvaluationRepository positionRiskEvaluationRepository;
    private final PositionRepository positionRepository;
    private final PositionAssignmentRepository positionAssignmentRepository;
    private final StaffSubstitutionRepository staffSubstitutionRepository;
    private final PositionRiskEvaluationMapper positionRiskEvaluationMapper;
    private final PositionRiskRuleEngine ruleEngine;

    public PositionRiskEvaluationService(
        PositionRiskEvaluationRepository positionRiskEvaluationRepository,
        PositionRepository positionRepository,
        PositionAssignmentRepository positionAssignmentRepository,
        StaffSubstitutionRepository staffSubstitutionRepository,
        PositionRiskEvaluationMapper positionRiskEvaluationMapper,
        PositionRiskRuleEngine ruleEngine
    ) {
        this.positionRiskEvaluationRepository = positionRiskEvaluationRepository;
        this.positionRepository = positionRepository;
        this.positionAssignmentRepository = positionAssignmentRepository;
        this.staffSubstitutionRepository = staffSubstitutionRepository;
        this.positionRiskEvaluationMapper = positionRiskEvaluationMapper;
        this.ruleEngine = ruleEngine;
    }

    public PositionRiskEvaluationDTO save(PositionRiskEvaluationDTO positionRiskEvaluationDTO) {
        LOG.debug("Request to save PositionRiskEvaluation : {}", positionRiskEvaluationDTO);
        PositionRiskEvaluation positionRiskEvaluation = positionRiskEvaluationMapper.toEntity(positionRiskEvaluationDTO);
        positionRiskEvaluation = positionRiskEvaluationRepository.save(positionRiskEvaluation);
        return positionRiskEvaluationMapper.toDto(positionRiskEvaluation);
    }

    public PositionRiskEvaluationDTO update(PositionRiskEvaluationDTO positionRiskEvaluationDTO) {
        LOG.debug("Request to update PositionRiskEvaluation : {}", positionRiskEvaluationDTO);
        PositionRiskEvaluation positionRiskEvaluation = positionRiskEvaluationMapper.toEntity(positionRiskEvaluationDTO);
        positionRiskEvaluation = positionRiskEvaluationRepository.save(positionRiskEvaluation);
        return positionRiskEvaluationMapper.toDto(positionRiskEvaluation);
    }

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

        PositionRiskInput input = new PositionRiskInput(
            positionId,
            position.getPositionName(),
            Boolean.TRUE.equals(position.getKeyPosition()),
            ownerCount,
            position.getMinimumOwnerCount() == null ? 0 : position.getMinimumOwnerCount(),
            substitutableOwnerCount,
            hasSubstitute,
            documentStatus,
            customerOrSystemDependency,
            successionReadiness
        );

        PositionRiskDecision decision = ruleEngine.evaluate(input);

        PositionRiskEvaluation entity = new PositionRiskEvaluation()
            .position(position)
            .evaluationDate(LocalDate.now())
            .ownerCount(ownerCount)
            .substitutableOwnerCount(substitutableOwnerCount)
            .hasSubstitute(hasSubstitute)
            .documentStatus(documentStatus)
            .customerOrSystemDependency(customerOrSystemDependency)
            .successionReadiness(successionReadiness)
            .riskLevel(decision.getRiskLevel())
            .riskReason(decision.getRiskReason())
            .recommendedAction(decision.getRecommendedAction());

        if (preview) {
            return positionRiskEvaluationMapper.toDto(entity);
        }
        return positionRiskEvaluationMapper.toDto(positionRiskEvaluationRepository.save(entity));
    }

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

    public Page<PositionRiskEvaluationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return positionRiskEvaluationRepository.findAllWithEagerRelationships(pageable).map(positionRiskEvaluationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PositionRiskEvaluationDTO> findOne(Long id) {
        LOG.debug("Request to get PositionRiskEvaluation : {}", id);
        return positionRiskEvaluationRepository.findOneWithEagerRelationships(id).map(positionRiskEvaluationMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete PositionRiskEvaluation : {}", id);
        positionRiskEvaluationRepository.deleteById(id);
    }
}
