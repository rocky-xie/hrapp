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
import top.btmdc.hr.domain.SkillAssessment;
import top.btmdc.hr.repository.SkillAssessmentRepository;
import top.btmdc.hr.service.criteria.SkillAssessmentCriteria;
import top.btmdc.hr.service.dto.SkillAssessmentDTO;
import top.btmdc.hr.service.mapper.SkillAssessmentMapper;

/**
 * Service for executing complex queries for {@link SkillAssessment} entities in the database.
 * The main input is a {@link SkillAssessmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SkillAssessmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SkillAssessmentQueryService extends QueryService<SkillAssessment> {

    private static final Logger LOG = LoggerFactory.getLogger(SkillAssessmentQueryService.class);

    private final SkillAssessmentRepository skillAssessmentRepository;

    private final SkillAssessmentMapper skillAssessmentMapper;

    public SkillAssessmentQueryService(SkillAssessmentRepository skillAssessmentRepository, SkillAssessmentMapper skillAssessmentMapper) {
        this.skillAssessmentRepository = skillAssessmentRepository;
        this.skillAssessmentMapper = skillAssessmentMapper;
    }

    /**
     * Return a {@link Page} of {@link SkillAssessmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SkillAssessmentDTO> findByCriteria(SkillAssessmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SkillAssessment> specification = createSpecification(criteria);
        return skillAssessmentRepository.findAll(specification, page).map(skillAssessmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SkillAssessmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SkillAssessment> specification = createSpecification(criteria);
        return skillAssessmentRepository.count(specification);
    }

    /**
     * Function to convert {@link SkillAssessmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SkillAssessment> createSpecification(SkillAssessmentCriteria criteria) {
        Specification<SkillAssessment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(SkillAssessment_.person, JoinType.LEFT);
                root.fetch(SkillAssessment_.skill, JoinType.LEFT);
                root.fetch(SkillAssessment_.assessor, JoinType.LEFT);
                root.fetch(SkillAssessment_.newLevel, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), SkillAssessment_.id),
                    buildRangeSpecification(criteria.getAssessmentDate(), SkillAssessment_.assessmentDate),
                    buildSpecification(criteria.getResult(), SkillAssessment_.result),
                    buildSpecification(criteria.getPersonId(), root -> root.join(SkillAssessment_.person, JoinType.LEFT).get(Person_.id)),
                    buildSpecification(criteria.getSkillId(), root -> root.join(SkillAssessment_.skill, JoinType.LEFT).get(Skill_.id)),
                    buildSpecification(criteria.getAssessorId(), root ->
                        root.join(SkillAssessment_.assessor, JoinType.LEFT).get(Person_.id)
                    ),
                    buildSpecification(criteria.getNewLevelId(), root ->
                        root.join(SkillAssessment_.newLevel, JoinType.LEFT).get(SkillLevel_.id)
                    )
                )
            );
        }
        return specification;
    }
}
