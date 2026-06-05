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
import top.btmdc.hr.domain.ImprovementPlan;
import top.btmdc.hr.repository.ImprovementPlanRepository;
import top.btmdc.hr.service.criteria.ImprovementPlanCriteria;
import top.btmdc.hr.service.dto.ImprovementPlanDTO;
import top.btmdc.hr.service.mapper.ImprovementPlanMapper;

/**
 * Service for executing complex queries for {@link ImprovementPlan} entities in the database.
 * The main input is a {@link ImprovementPlanCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ImprovementPlanDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImprovementPlanQueryService extends QueryService<ImprovementPlan> {

    private static final Logger LOG = LoggerFactory.getLogger(ImprovementPlanQueryService.class);

    private final ImprovementPlanRepository improvementPlanRepository;

    private final ImprovementPlanMapper improvementPlanMapper;

    public ImprovementPlanQueryService(ImprovementPlanRepository improvementPlanRepository, ImprovementPlanMapper improvementPlanMapper) {
        this.improvementPlanRepository = improvementPlanRepository;
        this.improvementPlanMapper = improvementPlanMapper;
    }

    /**
     * Return a {@link Page} of {@link ImprovementPlanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImprovementPlanDTO> findByCriteria(ImprovementPlanCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImprovementPlan> specification = createSpecification(criteria);
        return improvementPlanRepository.findAll(specification, page).map(improvementPlanMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImprovementPlanCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ImprovementPlan> specification = createSpecification(criteria);
        return improvementPlanRepository.count(specification);
    }

    /**
     * Function to convert {@link ImprovementPlanCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImprovementPlan> createSpecification(ImprovementPlanCriteria criteria) {
        Specification<ImprovementPlan> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ImprovementPlan_.position, JoinType.LEFT);
                root.fetch(ImprovementPlan_.skill, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ImprovementPlan_.id),
                    buildStringSpecification(criteria.getPlanName(), ImprovementPlan_.planName),
                    buildSpecification(criteria.getPlanStatus(), ImprovementPlan_.planStatus),
                    buildStringSpecification(criteria.getOwnerName(), ImprovementPlan_.ownerName),
                    buildRangeSpecification(criteria.getStartDate(), ImprovementPlan_.startDate),
                    buildRangeSpecification(criteria.getTargetDate(), ImprovementPlan_.targetDate),
                    buildRangeSpecification(criteria.getCompletionDate(), ImprovementPlan_.completionDate),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(ImprovementPlan_.position, JoinType.LEFT).get(Position_.id)
                    ),
                    buildSpecification(criteria.getSkillId(), root -> root.join(ImprovementPlan_.skill, JoinType.LEFT).get(Skill_.id))
                )
            );
        }
        return specification;
    }
}
