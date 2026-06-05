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
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.repository.TrainingGoalRepository;
import top.btmdc.hr.service.criteria.TrainingGoalCriteria;
import top.btmdc.hr.service.dto.TrainingGoalDTO;
import top.btmdc.hr.service.mapper.TrainingGoalMapper;

/**
 * Service for executing complex queries for {@link TrainingGoal} entities in the database.
 * The main input is a {@link TrainingGoalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TrainingGoalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrainingGoalQueryService extends QueryService<TrainingGoal> {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingGoalQueryService.class);

    private final TrainingGoalRepository trainingGoalRepository;

    private final TrainingGoalMapper trainingGoalMapper;

    public TrainingGoalQueryService(TrainingGoalRepository trainingGoalRepository, TrainingGoalMapper trainingGoalMapper) {
        this.trainingGoalRepository = trainingGoalRepository;
        this.trainingGoalMapper = trainingGoalMapper;
    }

    /**
     * Return a {@link Page} of {@link TrainingGoalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrainingGoalDTO> findByCriteria(TrainingGoalCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TrainingGoal> specification = createSpecification(criteria);
        return trainingGoalRepository.findAll(specification, page).map(trainingGoalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrainingGoalCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TrainingGoal> specification = createSpecification(criteria);
        return trainingGoalRepository.count(specification);
    }

    /**
     * Function to convert {@link TrainingGoalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TrainingGoal> createSpecification(TrainingGoalCriteria criteria) {
        Specification<TrainingGoal> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(TrainingGoal_.person, JoinType.LEFT);
                root.fetch(TrainingGoal_.position, JoinType.LEFT);
                root.fetch(TrainingGoal_.skill, JoinType.LEFT);
                root.fetch(TrainingGoal_.targetLevel, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), TrainingGoal_.id),
                    buildStringSpecification(criteria.getGoalName(), TrainingGoal_.goalName),
                    buildRangeSpecification(criteria.getStartDate(), TrainingGoal_.startDate),
                    buildRangeSpecification(criteria.getTargetDate(), TrainingGoal_.targetDate),
                    buildSpecification(criteria.getStatus(), TrainingGoal_.status),
                    buildSpecification(criteria.getPersonId(), root -> root.join(TrainingGoal_.person, JoinType.LEFT).get(Person_.id)),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(TrainingGoal_.position, JoinType.LEFT).get(Position_.id)
                    ),
                    buildSpecification(criteria.getSkillId(), root -> root.join(TrainingGoal_.skill, JoinType.LEFT).get(Skill_.id)),
                    buildSpecification(criteria.getTargetLevelId(), root ->
                        root.join(TrainingGoal_.targetLevel, JoinType.LEFT).get(SkillLevel_.id)
                    )
                )
            );
        }
        return specification;
    }
}
