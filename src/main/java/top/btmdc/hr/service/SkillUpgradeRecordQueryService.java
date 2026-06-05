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
import top.btmdc.hr.domain.SkillUpgradeRecord;
import top.btmdc.hr.repository.SkillUpgradeRecordRepository;
import top.btmdc.hr.service.criteria.SkillUpgradeRecordCriteria;
import top.btmdc.hr.service.dto.SkillUpgradeRecordDTO;
import top.btmdc.hr.service.mapper.SkillUpgradeRecordMapper;

/**
 * Service for executing complex queries for {@link SkillUpgradeRecord} entities in the database.
 * The main input is a {@link SkillUpgradeRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SkillUpgradeRecordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SkillUpgradeRecordQueryService extends QueryService<SkillUpgradeRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(SkillUpgradeRecordQueryService.class);

    private final SkillUpgradeRecordRepository skillUpgradeRecordRepository;

    private final SkillUpgradeRecordMapper skillUpgradeRecordMapper;

    public SkillUpgradeRecordQueryService(
        SkillUpgradeRecordRepository skillUpgradeRecordRepository,
        SkillUpgradeRecordMapper skillUpgradeRecordMapper
    ) {
        this.skillUpgradeRecordRepository = skillUpgradeRecordRepository;
        this.skillUpgradeRecordMapper = skillUpgradeRecordMapper;
    }

    /**
     * Return a {@link Page} of {@link SkillUpgradeRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SkillUpgradeRecordDTO> findByCriteria(SkillUpgradeRecordCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SkillUpgradeRecord> specification = createSpecification(criteria);
        return skillUpgradeRecordRepository.findAll(specification, page).map(skillUpgradeRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SkillUpgradeRecordCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SkillUpgradeRecord> specification = createSpecification(criteria);
        return skillUpgradeRecordRepository.count(specification);
    }

    /**
     * Function to convert {@link SkillUpgradeRecordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SkillUpgradeRecord> createSpecification(SkillUpgradeRecordCriteria criteria) {
        Specification<SkillUpgradeRecord> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(SkillUpgradeRecord_.person, JoinType.LEFT);
                root.fetch(SkillUpgradeRecord_.skill, JoinType.LEFT);
                root.fetch(SkillUpgradeRecord_.oldLevel, JoinType.LEFT);
                root.fetch(SkillUpgradeRecord_.newLevel, JoinType.LEFT);
                root.fetch(SkillUpgradeRecord_.assessor, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), SkillUpgradeRecord_.id),
                    buildSpecification(criteria.getChangeType(), SkillUpgradeRecord_.changeType),
                    buildRangeSpecification(criteria.getChangeDate(), SkillUpgradeRecord_.changeDate),
                    buildStringSpecification(criteria.getReason(), SkillUpgradeRecord_.reason),
                    buildStringSpecification(criteria.getBeforeLevelLabel(), SkillUpgradeRecord_.beforeLevelLabel),
                    buildStringSpecification(criteria.getAfterLevelLabel(), SkillUpgradeRecord_.afterLevelLabel),
                    buildSpecification(criteria.getPersonId(), root ->
                        root.join(SkillUpgradeRecord_.person, JoinType.LEFT).get(Person_.id)
                    ),
                    buildSpecification(criteria.getSkillId(), root -> root.join(SkillUpgradeRecord_.skill, JoinType.LEFT).get(Skill_.id)),
                    buildSpecification(criteria.getOldLevelId(), root ->
                        root.join(SkillUpgradeRecord_.oldLevel, JoinType.LEFT).get(SkillLevel_.id)
                    ),
                    buildSpecification(criteria.getNewLevelId(), root ->
                        root.join(SkillUpgradeRecord_.newLevel, JoinType.LEFT).get(SkillLevel_.id)
                    ),
                    buildSpecification(criteria.getAssessorId(), root ->
                        root.join(SkillUpgradeRecord_.assessor, JoinType.LEFT).get(Person_.id)
                    )
                )
            );
        }
        return specification;
    }
}
