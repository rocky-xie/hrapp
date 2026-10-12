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
import top.btmdc.hr.domain.DispatchAssignment;
import top.btmdc.hr.repository.DispatchAssignmentRepository;
import top.btmdc.hr.service.criteria.DispatchAssignmentCriteria;
import top.btmdc.hr.service.dto.DispatchAssignmentDTO;
import top.btmdc.hr.service.mapper.DispatchAssignmentMapper;

/**
 * Service for executing complex queries for {@link DispatchAssignment} entities in the database.
 * The main input is a {@link DispatchAssignmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DispatchAssignmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DispatchAssignmentQueryService extends QueryService<DispatchAssignment> {

    private static final Logger LOG = LoggerFactory.getLogger(DispatchAssignmentQueryService.class);

    private final DispatchAssignmentRepository dispatchAssignmentRepository;

    private final DispatchAssignmentMapper dispatchAssignmentMapper;

    public DispatchAssignmentQueryService(
        DispatchAssignmentRepository dispatchAssignmentRepository,
        DispatchAssignmentMapper dispatchAssignmentMapper
    ) {
        this.dispatchAssignmentRepository = dispatchAssignmentRepository;
        this.dispatchAssignmentMapper = dispatchAssignmentMapper;
    }

    /**
     * Return a {@link Page} of {@link DispatchAssignmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DispatchAssignmentDTO> findByCriteria(DispatchAssignmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DispatchAssignment> specification = createSpecification(criteria);
        return dispatchAssignmentRepository.findAll(specification, page).map(dispatchAssignmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DispatchAssignmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DispatchAssignment> specification = createSpecification(criteria);
        return dispatchAssignmentRepository.count(specification);
    }

    /**
     * Function to convert {@link DispatchAssignmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DispatchAssignment> createSpecification(DispatchAssignmentCriteria criteria) {
        Specification<DispatchAssignment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(DispatchAssignment_.application, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), DispatchAssignment_.id),
                    buildRangeSpecification(criteria.getCandidateId(), DispatchAssignment_.candidateId),
                    buildRangeSpecification(criteria.getPersonId(), DispatchAssignment_.personId),
                    buildRangeSpecification(criteria.getPartnerOrganizationId(), DispatchAssignment_.partnerOrganizationId),
                    buildStringSpecification(criteria.getDepartment(), DispatchAssignment_.department),
                    buildRangeSpecification(criteria.getPositionId(), DispatchAssignment_.positionId),
                    buildRangeSpecification(criteria.getStartDate(), DispatchAssignment_.startDate),
                    buildRangeSpecification(criteria.getExpectedEndDate(), DispatchAssignment_.expectedEndDate),
                    buildRangeSpecification(criteria.getObservationDays(), DispatchAssignment_.observationDays),
                    buildStringSpecification(criteria.getStatus(), DispatchAssignment_.status),
                    buildStringSpecification(criteria.getFinalDecision(), DispatchAssignment_.finalDecision),
                    buildSpecification(criteria.getReviewsId(), root ->
                        root.join(DispatchAssignment_.reviewses, JoinType.LEFT).get(DispatchReview_.id)
                    ),
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(DispatchAssignment_.application, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
