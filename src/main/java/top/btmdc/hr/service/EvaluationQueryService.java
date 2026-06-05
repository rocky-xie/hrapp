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
import top.btmdc.hr.domain.Evaluation;
import top.btmdc.hr.repository.EvaluationRepository;
import top.btmdc.hr.service.criteria.EvaluationCriteria;
import top.btmdc.hr.service.dto.EvaluationDTO;
import top.btmdc.hr.service.mapper.EvaluationMapper;

/**
 * Service for executing complex queries for {@link Evaluation} entities in the database.
 * The main input is a {@link EvaluationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EvaluationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EvaluationQueryService extends QueryService<Evaluation> {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationQueryService.class);

    private final EvaluationRepository evaluationRepository;

    private final EvaluationMapper evaluationMapper;

    public EvaluationQueryService(EvaluationRepository evaluationRepository, EvaluationMapper evaluationMapper) {
        this.evaluationRepository = evaluationRepository;
        this.evaluationMapper = evaluationMapper;
    }

    /**
     * Return a {@link Page} of {@link EvaluationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EvaluationDTO> findByCriteria(EvaluationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Evaluation> specification = createSpecification(criteria);
        return evaluationRepository.findAll(specification, page).map(evaluationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EvaluationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Evaluation> specification = createSpecification(criteria);
        return evaluationRepository.count(specification);
    }

    /**
     * Function to convert {@link EvaluationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Evaluation> createSpecification(EvaluationCriteria criteria) {
        Specification<Evaluation> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Evaluation_.person, JoinType.LEFT);
                root.fetch(Evaluation_.position, JoinType.LEFT);
                root.fetch(Evaluation_.trainingGoal, JoinType.LEFT);
                root.fetch(Evaluation_.evaluator, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Evaluation_.id),
                    buildStringSpecification(criteria.getEvaluationName(), Evaluation_.evaluationName),
                    buildRangeSpecification(criteria.getEvaluationDate(), Evaluation_.evaluationDate),
                    buildStringSpecification(criteria.getPeriodLabel(), Evaluation_.periodLabel),
                    buildSpecification(criteria.getProgressStatus(), Evaluation_.progressStatus),
                    buildSpecification(criteria.getResult(), Evaluation_.result),
                    buildSpecification(criteria.getPositionAdjustmentNeeded(), Evaluation_.positionAdjustmentNeeded),
                    buildSpecification(criteria.getPersonId(), root -> root.join(Evaluation_.person, JoinType.LEFT).get(Person_.id)),
                    buildSpecification(criteria.getPositionId(), root -> root.join(Evaluation_.position, JoinType.LEFT).get(Position_.id)),
                    buildSpecification(criteria.getTrainingGoalId(), root ->
                        root.join(Evaluation_.trainingGoal, JoinType.LEFT).get(TrainingGoal_.id)
                    ),
                    buildSpecification(criteria.getEvaluatorId(), root -> root.join(Evaluation_.evaluator, JoinType.LEFT).get(Person_.id))
                )
            );
        }
        return specification;
    }
}
