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
import top.btmdc.hr.domain.PositionSkillRequirement;
import top.btmdc.hr.repository.PositionSkillRequirementRepository;
import top.btmdc.hr.service.criteria.PositionSkillRequirementCriteria;
import top.btmdc.hr.service.dto.PositionSkillRequirementDTO;
import top.btmdc.hr.service.mapper.PositionSkillRequirementMapper;

/**
 * Service for executing complex queries for {@link PositionSkillRequirement} entities in the database.
 * The main input is a {@link PositionSkillRequirementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PositionSkillRequirementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PositionSkillRequirementQueryService extends QueryService<PositionSkillRequirement> {

    private static final Logger LOG = LoggerFactory.getLogger(PositionSkillRequirementQueryService.class);

    private final PositionSkillRequirementRepository positionSkillRequirementRepository;

    private final PositionSkillRequirementMapper positionSkillRequirementMapper;

    public PositionSkillRequirementQueryService(
        PositionSkillRequirementRepository positionSkillRequirementRepository,
        PositionSkillRequirementMapper positionSkillRequirementMapper
    ) {
        this.positionSkillRequirementRepository = positionSkillRequirementRepository;
        this.positionSkillRequirementMapper = positionSkillRequirementMapper;
    }

    /**
     * Return a {@link Page} of {@link PositionSkillRequirementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PositionSkillRequirementDTO> findByCriteria(PositionSkillRequirementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PositionSkillRequirement> specification = createSpecification(criteria);
        return positionSkillRequirementRepository.findAll(specification, page).map(positionSkillRequirementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PositionSkillRequirementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PositionSkillRequirement> specification = createSpecification(criteria);
        return positionSkillRequirementRepository.count(specification);
    }

    /**
     * Function to convert {@link PositionSkillRequirementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PositionSkillRequirement> createSpecification(PositionSkillRequirementCriteria criteria) {
        Specification<PositionSkillRequirement> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PositionSkillRequirement_.position, JoinType.LEFT);
                root.fetch(PositionSkillRequirement_.skill, JoinType.LEFT);
                root.fetch(PositionSkillRequirement_.requiredLevel, JoinType.LEFT);
                root.fetch(PositionSkillRequirement_.preferredLevel, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PositionSkillRequirement_.id),
                    buildSpecification(criteria.getImportance(), PositionSkillRequirement_.importance),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(PositionSkillRequirement_.position, JoinType.LEFT).get(Position_.id)
                    ),
                    buildSpecification(criteria.getSkillId(), root ->
                        root.join(PositionSkillRequirement_.skill, JoinType.LEFT).get(Skill_.id)
                    ),
                    buildSpecification(criteria.getRequiredLevelId(), root ->
                        root.join(PositionSkillRequirement_.requiredLevel, JoinType.LEFT).get(SkillLevel_.id)
                    ),
                    buildSpecification(criteria.getPreferredLevelId(), root ->
                        root.join(PositionSkillRequirement_.preferredLevel, JoinType.LEFT).get(SkillLevel_.id)
                    )
                )
            );
        }
        return specification;
    }
}
