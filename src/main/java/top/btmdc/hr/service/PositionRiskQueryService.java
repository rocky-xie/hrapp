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
import top.btmdc.hr.domain.PositionRisk;
import top.btmdc.hr.repository.PositionRiskRepository;
import top.btmdc.hr.service.criteria.PositionRiskCriteria;
import top.btmdc.hr.service.dto.PositionRiskDTO;
import top.btmdc.hr.service.mapper.PositionRiskMapper;

/**
 * Service for executing complex queries for {@link PositionRisk} entities in the database.
 * The main input is a {@link PositionRiskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PositionRiskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PositionRiskQueryService extends QueryService<PositionRisk> {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRiskQueryService.class);

    private final PositionRiskRepository positionRiskRepository;

    private final PositionRiskMapper positionRiskMapper;

    public PositionRiskQueryService(PositionRiskRepository positionRiskRepository, PositionRiskMapper positionRiskMapper) {
        this.positionRiskRepository = positionRiskRepository;
        this.positionRiskMapper = positionRiskMapper;
    }

    /**
     * Return a {@link Page} of {@link PositionRiskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PositionRiskDTO> findByCriteria(PositionRiskCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PositionRisk> specification = createSpecification(criteria);
        return positionRiskRepository.findAll(specification, page).map(positionRiskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PositionRiskCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PositionRisk> specification = createSpecification(criteria);
        return positionRiskRepository.count(specification);
    }

    /**
     * Function to convert {@link PositionRiskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PositionRisk> createSpecification(PositionRiskCriteria criteria) {
        Specification<PositionRisk> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PositionRisk_.position, JoinType.LEFT);
                root.fetch(PositionRisk_.category, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PositionRisk_.id),
                    buildSpecification(criteria.getRiskType(), PositionRisk_.riskType),
                    buildSpecification(criteria.getRiskLevel(), PositionRisk_.riskLevel),
                    buildSpecification(criteria.getDocumentStatus(), PositionRisk_.documentStatus),
                    buildSpecification(criteria.getBackupStatus(), PositionRisk_.backupStatus),
                    buildSpecification(criteria.getCustomerOrSystemDependency(), PositionRisk_.customerOrSystemDependency),
                    buildRangeSpecification(criteria.getIdentifiedDate(), PositionRisk_.identifiedDate),
                    buildRangeSpecification(criteria.getTargetDate(), PositionRisk_.targetDate),
                    buildRangeSpecification(criteria.getClosedDate(), PositionRisk_.closedDate),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(PositionRisk_.position, JoinType.LEFT).get(Position_.id)
                    ),
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(PositionRisk_.category, JoinType.LEFT).get(KeyResponsibilityCategory_.id)
                    )
                )
            );
        }
        return specification;
    }
}
