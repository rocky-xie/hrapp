package top.btmdc.hr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import top.btmdc.hr.domain.*; // for static metamodels
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.repository.SkillRepository;
import top.btmdc.hr.service.criteria.SkillCriteria;
import top.btmdc.hr.service.dto.SkillDTO;
import top.btmdc.hr.service.mapper.SkillMapper;

/**
 * Service for executing complex queries for {@link Skill} entities in the database.
 * The main input is a {@link SkillCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SkillDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SkillQueryService extends QueryService<Skill> {

    private static final Logger LOG = LoggerFactory.getLogger(SkillQueryService.class);

    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    public SkillQueryService(SkillRepository skillRepository, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    /**
     * Return a {@link Page} of {@link SkillDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SkillDTO> findByCriteria(SkillCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Skill> specification = createSpecification(criteria);
        return skillRepository.findAll(specification, page).map(skillMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SkillCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Skill> specification = createSpecification(criteria);
        return skillRepository.count(specification);
    }

    /**
     * Function to convert {@link SkillCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Skill> createSpecification(SkillCriteria criteria) {
        Specification<Skill> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Skill_.id),
                    buildStringSpecification(criteria.getSkillCode(), Skill_.skillCode),
                    buildStringSpecification(criteria.getSkillName(), Skill_.skillName),
                    buildSpecification(criteria.getSkillType(), Skill_.skillType),
                    buildSpecification(criteria.getMeasurableFlag(), Skill_.measurableFlag),
                    buildSpecification(criteria.getEvidenceType(), Skill_.evidenceType)
                )
            );
        }
        return specification;
    }
}
