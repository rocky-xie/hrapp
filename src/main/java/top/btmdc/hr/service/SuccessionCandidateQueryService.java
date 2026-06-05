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
import top.btmdc.hr.domain.SuccessionCandidate;
import top.btmdc.hr.repository.SuccessionCandidateRepository;
import top.btmdc.hr.service.criteria.SuccessionCandidateCriteria;
import top.btmdc.hr.service.dto.SuccessionCandidateDTO;
import top.btmdc.hr.service.mapper.SuccessionCandidateMapper;

/**
 * Service for executing complex queries for {@link SuccessionCandidate} entities in the database.
 * The main input is a {@link SuccessionCandidateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SuccessionCandidateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuccessionCandidateQueryService extends QueryService<SuccessionCandidate> {

    private static final Logger LOG = LoggerFactory.getLogger(SuccessionCandidateQueryService.class);

    private final SuccessionCandidateRepository successionCandidateRepository;

    private final SuccessionCandidateMapper successionCandidateMapper;

    public SuccessionCandidateQueryService(
        SuccessionCandidateRepository successionCandidateRepository,
        SuccessionCandidateMapper successionCandidateMapper
    ) {
        this.successionCandidateRepository = successionCandidateRepository;
        this.successionCandidateMapper = successionCandidateMapper;
    }

    /**
     * Return a {@link Page} of {@link SuccessionCandidateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SuccessionCandidateDTO> findByCriteria(SuccessionCandidateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SuccessionCandidate> specification = createSpecification(criteria);
        return successionCandidateRepository.findAll(specification, page).map(successionCandidateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuccessionCandidateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SuccessionCandidate> specification = createSpecification(criteria);
        return successionCandidateRepository.count(specification);
    }

    /**
     * Function to convert {@link SuccessionCandidateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SuccessionCandidate> createSpecification(SuccessionCandidateCriteria criteria) {
        Specification<SuccessionCandidate> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(SuccessionCandidate_.position, JoinType.LEFT);
                root.fetch(SuccessionCandidate_.currentOwner, JoinType.LEFT);
                root.fetch(SuccessionCandidate_.candidate, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), SuccessionCandidate_.id),
                    buildSpecification(criteria.getSuccessionReadiness(), SuccessionCandidate_.successionReadiness),
                    buildStringSpecification(criteria.getEstimatedTimeToReady(), SuccessionCandidate_.estimatedTimeToReady),
                    buildSpecification(criteria.getRiskAfterTraining(), SuccessionCandidate_.riskAfterTraining),
                    buildRangeSpecification(criteria.getReviewDate(), SuccessionCandidate_.reviewDate),
                    buildRangeSpecification(criteria.getPriority(), SuccessionCandidate_.priority),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(SuccessionCandidate_.position, JoinType.LEFT).get(Position_.id)
                    ),
                    buildSpecification(criteria.getCurrentOwnerId(), root ->
                        root.join(SuccessionCandidate_.currentOwner, JoinType.LEFT).get(Person_.id)
                    ),
                    buildSpecification(criteria.getCandidateId(), root ->
                        root.join(SuccessionCandidate_.candidate, JoinType.LEFT).get(Person_.id)
                    )
                )
            );
        }
        return specification;
    }
}
