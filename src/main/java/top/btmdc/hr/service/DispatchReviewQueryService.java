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
import top.btmdc.hr.domain.DispatchReview;
import top.btmdc.hr.repository.DispatchReviewRepository;
import top.btmdc.hr.service.criteria.DispatchReviewCriteria;
import top.btmdc.hr.service.dto.DispatchReviewDTO;
import top.btmdc.hr.service.mapper.DispatchReviewMapper;

/**
 * Service for executing complex queries for {@link DispatchReview} entities in the database.
 * The main input is a {@link DispatchReviewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DispatchReviewDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DispatchReviewQueryService extends QueryService<DispatchReview> {

    private static final Logger LOG = LoggerFactory.getLogger(DispatchReviewQueryService.class);

    private final DispatchReviewRepository dispatchReviewRepository;

    private final DispatchReviewMapper dispatchReviewMapper;

    public DispatchReviewQueryService(DispatchReviewRepository dispatchReviewRepository, DispatchReviewMapper dispatchReviewMapper) {
        this.dispatchReviewRepository = dispatchReviewRepository;
        this.dispatchReviewMapper = dispatchReviewMapper;
    }

    /**
     * Return a {@link Page} of {@link DispatchReviewDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DispatchReviewDTO> findByCriteria(DispatchReviewCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DispatchReview> specification = createSpecification(criteria);
        return dispatchReviewRepository.findAll(specification, page).map(dispatchReviewMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DispatchReviewCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DispatchReview> specification = createSpecification(criteria);
        return dispatchReviewRepository.count(specification);
    }

    /**
     * Function to convert {@link DispatchReviewCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DispatchReview> createSpecification(DispatchReviewCriteria criteria) {
        Specification<DispatchReview> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(DispatchReview_.dispatchAssignment, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), DispatchReview_.id),
                    buildRangeSpecification(criteria.getReviewedAt(), DispatchReview_.reviewedAt),
                    buildStringSpecification(criteria.getReviewerUsername(), DispatchReview_.reviewerUsername),
                    buildStringSpecification(criteria.getRecommendation(), DispatchReview_.recommendation),
                    buildRangeSpecification(criteria.getExtensionEndDate(), DispatchReview_.extensionEndDate),
                    buildStringSpecification(criteria.getFinalDecision(), DispatchReview_.finalDecision),
                    buildSpecification(criteria.getDispatchAssignmentId(), root ->
                        root.join(DispatchReview_.dispatchAssignment, JoinType.LEFT).get(DispatchAssignment_.id)
                    )
                )
            );
        }
        return specification;
    }
}
