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
import top.btmdc.hr.domain.StaffSubstitution;
import top.btmdc.hr.repository.StaffSubstitutionRepository;
import top.btmdc.hr.service.criteria.StaffSubstitutionCriteria;
import top.btmdc.hr.service.dto.StaffSubstitutionDTO;
import top.btmdc.hr.service.mapper.StaffSubstitutionMapper;

/**
 * Service for executing complex queries for {@link StaffSubstitution} entities in the database.
 * The main input is a {@link StaffSubstitutionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link StaffSubstitutionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StaffSubstitutionQueryService extends QueryService<StaffSubstitution> {

    private static final Logger LOG = LoggerFactory.getLogger(StaffSubstitutionQueryService.class);

    private final StaffSubstitutionRepository staffSubstitutionRepository;

    private final StaffSubstitutionMapper staffSubstitutionMapper;

    public StaffSubstitutionQueryService(
        StaffSubstitutionRepository staffSubstitutionRepository,
        StaffSubstitutionMapper staffSubstitutionMapper
    ) {
        this.staffSubstitutionRepository = staffSubstitutionRepository;
        this.staffSubstitutionMapper = staffSubstitutionMapper;
    }

    /**
     * Return a {@link Page} of {@link StaffSubstitutionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StaffSubstitutionDTO> findByCriteria(StaffSubstitutionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StaffSubstitution> specification = createSpecification(criteria);
        return staffSubstitutionRepository.findAll(specification, page).map(staffSubstitutionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StaffSubstitutionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<StaffSubstitution> specification = createSpecification(criteria);
        return staffSubstitutionRepository.count(specification);
    }

    /**
     * Function to convert {@link StaffSubstitutionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StaffSubstitution> createSpecification(StaffSubstitutionCriteria criteria) {
        Specification<StaffSubstitution> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(StaffSubstitution_.position, JoinType.LEFT);
                root.fetch(StaffSubstitution_.candidatePerson, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), StaffSubstitution_.id),
                    buildRangeSpecification(criteria.getCoverageRate(), StaffSubstitution_.coverageRate),
                    buildRangeSpecification(criteria.getThresholdRate(), StaffSubstitution_.thresholdRate),
                    buildRangeSpecification(criteria.getTotalSkillCount(), StaffSubstitution_.totalSkillCount),
                    buildRangeSpecification(criteria.getCoveredSkillCount(), StaffSubstitution_.coveredSkillCount),
                    buildSpecification(criteria.getSubstitutable(), StaffSubstitution_.substitutable),
                    buildRangeSpecification(criteria.getEvaluationDate(), StaffSubstitution_.evaluationDate),
                    buildRangeSpecification(criteria.getReviewDate(), StaffSubstitution_.reviewDate),
                    buildRangeSpecification(criteria.getExpiryDate(), StaffSubstitution_.expiryDate),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(StaffSubstitution_.position, JoinType.LEFT).get(Position_.id)
                    ),
                    buildSpecification(criteria.getCandidatePersonId(), root ->
                        root.join(StaffSubstitution_.candidatePerson, JoinType.LEFT).get(Person_.id)
                    )
                )
            );
        }
        return specification;
    }
}
