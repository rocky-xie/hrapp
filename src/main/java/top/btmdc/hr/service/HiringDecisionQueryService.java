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
import top.btmdc.hr.domain.HiringDecision;
import top.btmdc.hr.repository.HiringDecisionRepository;
import top.btmdc.hr.service.criteria.HiringDecisionCriteria;
import top.btmdc.hr.service.dto.HiringDecisionDTO;
import top.btmdc.hr.service.mapper.HiringDecisionMapper;

/**
 * Service for executing complex queries for {@link HiringDecision} entities in the database.
 * The main input is a {@link HiringDecisionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HiringDecisionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HiringDecisionQueryService extends QueryService<HiringDecision> {

    private static final Logger LOG = LoggerFactory.getLogger(HiringDecisionQueryService.class);

    private final HiringDecisionRepository hiringDecisionRepository;

    private final HiringDecisionMapper hiringDecisionMapper;

    public HiringDecisionQueryService(HiringDecisionRepository hiringDecisionRepository, HiringDecisionMapper hiringDecisionMapper) {
        this.hiringDecisionRepository = hiringDecisionRepository;
        this.hiringDecisionMapper = hiringDecisionMapper;
    }

    /**
     * Return a {@link Page} of {@link HiringDecisionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HiringDecisionDTO> findByCriteria(HiringDecisionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HiringDecision> specification = createSpecification(criteria);
        return hiringDecisionRepository.findAll(specification, page).map(hiringDecisionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HiringDecisionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HiringDecision> specification = createSpecification(criteria);
        return hiringDecisionRepository.count(specification);
    }

    /**
     * Function to convert {@link HiringDecisionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HiringDecision> createSpecification(HiringDecisionCriteria criteria) {
        Specification<HiringDecision> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(HiringDecision_.application, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), HiringDecision_.id),
                    buildSpecification(criteria.getDecision(), HiringDecision_.decision),
                    buildStringSpecification(criteria.getDecisionSource(), HiringDecision_.decisionSource),
                    buildStringSpecification(criteria.getDecidedBy(), HiringDecision_.decidedBy),
                    buildRangeSpecification(criteria.getDecidedAt(), HiringDecision_.decidedAt),
                    buildRangeSpecification(criteria.getCorrectionOfId(), HiringDecision_.correctionOfId),
                    buildSpecification(criteria.getActive(), HiringDecision_.active),
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(HiringDecision_.application, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
