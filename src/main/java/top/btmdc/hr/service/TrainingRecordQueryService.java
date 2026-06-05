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
import top.btmdc.hr.domain.TrainingRecord;
import top.btmdc.hr.repository.TrainingRecordRepository;
import top.btmdc.hr.service.criteria.TrainingRecordCriteria;
import top.btmdc.hr.service.dto.TrainingRecordDTO;
import top.btmdc.hr.service.mapper.TrainingRecordMapper;

/**
 * Service for executing complex queries for {@link TrainingRecord} entities in the database.
 * The main input is a {@link TrainingRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TrainingRecordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrainingRecordQueryService extends QueryService<TrainingRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingRecordQueryService.class);

    private final TrainingRecordRepository trainingRecordRepository;

    private final TrainingRecordMapper trainingRecordMapper;

    public TrainingRecordQueryService(TrainingRecordRepository trainingRecordRepository, TrainingRecordMapper trainingRecordMapper) {
        this.trainingRecordRepository = trainingRecordRepository;
        this.trainingRecordMapper = trainingRecordMapper;
    }

    /**
     * Return a {@link Page} of {@link TrainingRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrainingRecordDTO> findByCriteria(TrainingRecordCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TrainingRecord> specification = createSpecification(criteria);
        return trainingRecordRepository.findAll(specification, page).map(trainingRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrainingRecordCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TrainingRecord> specification = createSpecification(criteria);
        return trainingRecordRepository.count(specification);
    }

    /**
     * Function to convert {@link TrainingRecordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TrainingRecord> createSpecification(TrainingRecordCriteria criteria) {
        Specification<TrainingRecord> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(TrainingRecord_.person, JoinType.LEFT);
                root.fetch(TrainingRecord_.trainingGoal, JoinType.LEFT);
                root.fetch(TrainingRecord_.position, JoinType.LEFT);
                root.fetch(TrainingRecord_.mentor, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), TrainingRecord_.id),
                    buildRangeSpecification(criteria.getTrainingDate(), TrainingRecord_.trainingDate),
                    buildSpecification(criteria.getTrainingType(), TrainingRecord_.trainingType),
                    buildStringSpecification(criteria.getTopic(), TrainingRecord_.topic),
                    buildSpecification(criteria.getPersonId(), root -> root.join(TrainingRecord_.person, JoinType.LEFT).get(Person_.id)),
                    buildSpecification(criteria.getTrainingGoalId(), root ->
                        root.join(TrainingRecord_.trainingGoal, JoinType.LEFT).get(TrainingGoal_.id)
                    ),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(TrainingRecord_.position, JoinType.LEFT).get(Position_.id)
                    ),
                    buildSpecification(criteria.getMentorId(), root -> root.join(TrainingRecord_.mentor, JoinType.LEFT).get(Person_.id))
                )
            );
        }
        return specification;
    }
}
