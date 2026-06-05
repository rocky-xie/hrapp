package top.btmdc.hr.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import top.btmdc.hr.domain.*; // for static metamodels
import top.btmdc.hr.domain.PositionRiskEvaluation;
import top.btmdc.hr.repository.PositionRiskEvaluationRepository;
import top.btmdc.hr.service.criteria.PositionRiskEvaluationCriteria;
import top.btmdc.hr.service.dto.PositionRiskEvaluationDTO;
import top.btmdc.hr.service.mapper.PositionRiskEvaluationMapper;

/**
 * Service for executing complex queries for {@link PositionRiskEvaluation} entities in the database.
 * The main input is a {@link PositionRiskEvaluationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PositionRiskEvaluationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PositionRiskEvaluationQueryService extends QueryService<PositionRiskEvaluation> {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRiskEvaluationQueryService.class);

    private final PositionRiskEvaluationRepository positionRiskEvaluationRepository;

    private final PositionRiskEvaluationMapper positionRiskEvaluationMapper;

    public PositionRiskEvaluationQueryService(
        PositionRiskEvaluationRepository positionRiskEvaluationRepository,
        PositionRiskEvaluationMapper positionRiskEvaluationMapper
    ) {
        this.positionRiskEvaluationRepository = positionRiskEvaluationRepository;
        this.positionRiskEvaluationMapper = positionRiskEvaluationMapper;
    }

    /**
     * Return a {@link Page} of {@link PositionRiskEvaluationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PositionRiskEvaluationDTO> findByCriteria(PositionRiskEvaluationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PositionRiskEvaluation> specification = createSpecification(criteria);
        return positionRiskEvaluationRepository.findAll(specification, page).map(positionRiskEvaluationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PositionRiskEvaluationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PositionRiskEvaluation> specification = createSpecification(criteria);
        return positionRiskEvaluationRepository.count(specification);
    }

    /**
     * Function to convert {@link PositionRiskEvaluationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PositionRiskEvaluation> createSpecification(PositionRiskEvaluationCriteria criteria) {
        Specification<PositionRiskEvaluation> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PositionRiskEvaluation_.position, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PositionRiskEvaluation_.id),
                    buildRangeSpecification(criteria.getEvaluationDate(), PositionRiskEvaluation_.evaluationDate),
                    buildRangeSpecification(criteria.getOwnerCount(), PositionRiskEvaluation_.ownerCount),
                    buildRangeSpecification(criteria.getSubstitutableOwnerCount(), PositionRiskEvaluation_.substitutableOwnerCount),
                    buildSpecification(criteria.getHasSubstitute(), PositionRiskEvaluation_.hasSubstitute),
                    buildSpecification(criteria.getDocumentStatus(), PositionRiskEvaluation_.documentStatus),
                    buildSpecification(criteria.getCustomerOrSystemDependency(), PositionRiskEvaluation_.customerOrSystemDependency),
                    buildSpecification(criteria.getSuccessionReadiness(), PositionRiskEvaluation_.successionReadiness),
                    buildSpecification(criteria.getRiskLevel(), PositionRiskEvaluation_.riskLevel),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(PositionRiskEvaluation_.position, JoinType.LEFT).get(Position_.id)
                    )
                )
            );
        }
        return specification;
    }
}
