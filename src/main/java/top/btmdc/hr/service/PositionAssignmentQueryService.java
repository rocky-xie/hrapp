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
import top.btmdc.hr.domain.PositionAssignment;
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.service.criteria.PositionAssignmentCriteria;
import top.btmdc.hr.service.dto.PositionAssignmentDTO;
import top.btmdc.hr.service.mapper.PositionAssignmentMapper;

/**
 * Service for executing complex queries for {@link PositionAssignment} entities in the database.
 * The main input is a {@link PositionAssignmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PositionAssignmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PositionAssignmentQueryService extends QueryService<PositionAssignment> {

    private static final Logger LOG = LoggerFactory.getLogger(PositionAssignmentQueryService.class);

    private final PositionAssignmentRepository positionAssignmentRepository;

    private final PositionAssignmentMapper positionAssignmentMapper;

    public PositionAssignmentQueryService(
        PositionAssignmentRepository positionAssignmentRepository,
        PositionAssignmentMapper positionAssignmentMapper
    ) {
        this.positionAssignmentRepository = positionAssignmentRepository;
        this.positionAssignmentMapper = positionAssignmentMapper;
    }

    /**
     * Return a {@link Page} of {@link PositionAssignmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PositionAssignmentDTO> findByCriteria(PositionAssignmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PositionAssignment> specification = createSpecification(criteria);
        return positionAssignmentRepository.findAll(specification, page).map(positionAssignmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PositionAssignmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PositionAssignment> specification = createSpecification(criteria);
        return positionAssignmentRepository.count(specification);
    }

    /**
     * Function to convert {@link PositionAssignmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PositionAssignment> createSpecification(PositionAssignmentCriteria criteria) {
        Specification<PositionAssignment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PositionAssignment_.person, JoinType.LEFT);
                root.fetch(PositionAssignment_.position, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PositionAssignment_.id),
                    buildSpecification(criteria.getPrimaryOwner(), PositionAssignment_.primaryOwner),
                    buildRangeSpecification(criteria.getStartDate(), PositionAssignment_.startDate),
                    buildRangeSpecification(criteria.getEndDate(), PositionAssignment_.endDate),
                    buildSpecification(criteria.getActive(), PositionAssignment_.active),
                    buildSpecification(criteria.getPersonId(), root ->
                        root.join(PositionAssignment_.person, JoinType.LEFT).get(Person_.id)
                    ),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(PositionAssignment_.position, JoinType.LEFT).get(Position_.id)
                    )
                )
            );
        }
        return specification;
    }
}
