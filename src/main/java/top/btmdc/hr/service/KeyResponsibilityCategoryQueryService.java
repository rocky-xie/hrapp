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
import top.btmdc.hr.domain.KeyResponsibilityCategory;
import top.btmdc.hr.repository.KeyResponsibilityCategoryRepository;
import top.btmdc.hr.service.criteria.KeyResponsibilityCategoryCriteria;
import top.btmdc.hr.service.dto.KeyResponsibilityCategoryDTO;
import top.btmdc.hr.service.mapper.KeyResponsibilityCategoryMapper;

/**
 * Service for executing complex queries for {@link KeyResponsibilityCategory} entities in the database.
 * The main input is a {@link KeyResponsibilityCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link KeyResponsibilityCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KeyResponsibilityCategoryQueryService extends QueryService<KeyResponsibilityCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(KeyResponsibilityCategoryQueryService.class);

    private final KeyResponsibilityCategoryRepository keyResponsibilityCategoryRepository;

    private final KeyResponsibilityCategoryMapper keyResponsibilityCategoryMapper;

    public KeyResponsibilityCategoryQueryService(
        KeyResponsibilityCategoryRepository keyResponsibilityCategoryRepository,
        KeyResponsibilityCategoryMapper keyResponsibilityCategoryMapper
    ) {
        this.keyResponsibilityCategoryRepository = keyResponsibilityCategoryRepository;
        this.keyResponsibilityCategoryMapper = keyResponsibilityCategoryMapper;
    }

    /**
     * Return a {@link Page} of {@link KeyResponsibilityCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KeyResponsibilityCategoryDTO> findByCriteria(KeyResponsibilityCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<KeyResponsibilityCategory> specification = createSpecification(criteria);
        return keyResponsibilityCategoryRepository.findAll(specification, page).map(keyResponsibilityCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KeyResponsibilityCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<KeyResponsibilityCategory> specification = createSpecification(criteria);
        return keyResponsibilityCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link KeyResponsibilityCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<KeyResponsibilityCategory> createSpecification(KeyResponsibilityCategoryCriteria criteria) {
        Specification<KeyResponsibilityCategory> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), KeyResponsibilityCategory_.id),
                    buildStringSpecification(criteria.getCategoryName(), KeyResponsibilityCategory_.categoryName)
                )
            );
        }
        return specification;
    }
}
