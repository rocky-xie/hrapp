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
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.repository.PersonSkillRepository;
import top.btmdc.hr.service.criteria.PersonSkillCriteria;
import top.btmdc.hr.service.dto.PersonSkillDTO;
import top.btmdc.hr.service.mapper.PersonSkillMapper;

/**
 * Service for executing complex queries for {@link PersonSkill} entities in the database.
 * The main input is a {@link PersonSkillCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PersonSkillDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonSkillQueryService extends QueryService<PersonSkill> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonSkillQueryService.class);

    private final PersonSkillRepository personSkillRepository;

    private final PersonSkillMapper personSkillMapper;

    public PersonSkillQueryService(PersonSkillRepository personSkillRepository, PersonSkillMapper personSkillMapper) {
        this.personSkillRepository = personSkillRepository;
        this.personSkillMapper = personSkillMapper;
    }

    /**
     * Return a {@link Page} of {@link PersonSkillDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonSkillDTO> findByCriteria(PersonSkillCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonSkill> specification = createSpecification(criteria);
        return personSkillRepository.findAll(specification, page).map(personSkillMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonSkillCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PersonSkill> specification = createSpecification(criteria);
        return personSkillRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonSkillCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PersonSkill> createSpecification(PersonSkillCriteria criteria) {
        Specification<PersonSkill> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PersonSkill_.person, JoinType.LEFT);
                root.fetch(PersonSkill_.skill, JoinType.LEFT);
                root.fetch(PersonSkill_.currentLevel, JoinType.LEFT);
                root.fetch(PersonSkill_.previousLevel, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PersonSkill_.id),
                    buildRangeSpecification(criteria.getAssessmentDate(), PersonSkill_.assessmentDate),
                    buildRangeSpecification(criteria.getNextReviewDate(), PersonSkill_.nextReviewDate),
                    buildSpecification(criteria.getConfidence(), PersonSkill_.confidence),
                    buildSpecification(criteria.getPersonId(), root -> root.join(PersonSkill_.person, JoinType.LEFT).get(Person_.id)),
                    buildSpecification(criteria.getSkillId(), root -> root.join(PersonSkill_.skill, JoinType.LEFT).get(Skill_.id)),
                    buildSpecification(criteria.getCurrentLevelId(), root ->
                        root.join(PersonSkill_.currentLevel, JoinType.LEFT).get(SkillLevel_.id)
                    ),
                    buildSpecification(criteria.getPreviousLevelId(), root ->
                        root.join(PersonSkill_.previousLevel, JoinType.LEFT).get(SkillLevel_.id)
                    )
                )
            );
        }
        return specification;
    }
}
