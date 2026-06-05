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
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.repository.SkillLevelRepository;
import top.btmdc.hr.service.criteria.SkillLevelCriteria;
import top.btmdc.hr.service.dto.SkillLevelDTO;
import top.btmdc.hr.service.mapper.SkillLevelMapper;

/**
 * Service for executing complex queries for {@link SkillLevel} entities in the database.
 * The main input is a {@link SkillLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SkillLevelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SkillLevelQueryService extends QueryService<SkillLevel> {

    private static final Logger LOG = LoggerFactory.getLogger(SkillLevelQueryService.class);

    private final SkillLevelRepository skillLevelRepository;

    private final SkillLevelMapper skillLevelMapper;

    public SkillLevelQueryService(SkillLevelRepository skillLevelRepository, SkillLevelMapper skillLevelMapper) {
        this.skillLevelRepository = skillLevelRepository;
        this.skillLevelMapper = skillLevelMapper;
    }

    /**
     * Return a {@link Page} of {@link SkillLevelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SkillLevelDTO> findByCriteria(SkillLevelCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SkillLevel> specification = createSpecification(criteria);
        return skillLevelRepository.findAll(specification, page).map(skillLevelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SkillLevelCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SkillLevel> specification = createSpecification(criteria);
        return skillLevelRepository.count(specification);
    }

    /**
     * Function to convert {@link SkillLevelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SkillLevel> createSpecification(SkillLevelCriteria criteria) {
        Specification<SkillLevel> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), SkillLevel_.id),
                    buildSpecification(criteria.getCode(), SkillLevel_.code),
                    buildStringSpecification(criteria.getLevelName(), SkillLevel_.levelName),
                    buildRangeSpecification(criteria.getSortOrder(), SkillLevel_.sortOrder)
                )
            );
        }
        return specification;
    }
}
