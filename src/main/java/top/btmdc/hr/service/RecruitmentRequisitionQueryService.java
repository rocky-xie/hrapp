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
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.repository.RecruitmentRequisitionRepository;
import top.btmdc.hr.service.criteria.RecruitmentRequisitionCriteria;
import top.btmdc.hr.service.dto.RecruitmentRequisitionDTO;
import top.btmdc.hr.service.mapper.RecruitmentRequisitionMapper;

/**
 * Service for executing complex queries for {@link RecruitmentRequisition} entities in the database.
 * The main input is a {@link RecruitmentRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RecruitmentRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecruitmentRequisitionQueryService extends QueryService<RecruitmentRequisition> {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRequisitionQueryService.class);

    private final RecruitmentRequisitionRepository recruitmentRequisitionRepository;

    private final RecruitmentRequisitionMapper recruitmentRequisitionMapper;

    public RecruitmentRequisitionQueryService(
        RecruitmentRequisitionRepository recruitmentRequisitionRepository,
        RecruitmentRequisitionMapper recruitmentRequisitionMapper
    ) {
        this.recruitmentRequisitionRepository = recruitmentRequisitionRepository;
        this.recruitmentRequisitionMapper = recruitmentRequisitionMapper;
    }

    /**
     * Return a {@link Page} of {@link RecruitmentRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecruitmentRequisitionDTO> findByCriteria(RecruitmentRequisitionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecruitmentRequisition> specification = createSpecification(criteria);
        return recruitmentRequisitionRepository.findAll(specification, page).map(recruitmentRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecruitmentRequisitionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<RecruitmentRequisition> specification = createSpecification(criteria);
        return recruitmentRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link RecruitmentRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RecruitmentRequisition> createSpecification(RecruitmentRequisitionCriteria criteria) {
        Specification<RecruitmentRequisition> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), RecruitmentRequisition_.id),
                    buildStringSpecification(criteria.getRequisitionCode(), RecruitmentRequisition_.requisitionCode),
                    buildStringSpecification(criteria.getTitle(), RecruitmentRequisition_.title),
                    buildStringSpecification(criteria.getDepartment(), RecruitmentRequisition_.department),
                    buildRangeSpecification(criteria.getPositionId(), RecruitmentRequisition_.positionId),
                    buildSpecification(criteria.getRecruitmentType(), RecruitmentRequisition_.recruitmentType),
                    buildSpecification(criteria.getLifecycle(), RecruitmentRequisition_.lifecycle),
                    buildSpecification(criteria.getEditStatus(), RecruitmentRequisition_.editStatus),
                    buildRangeSpecification(criteria.getPlannedHeadcount(), RecruitmentRequisition_.plannedHeadcount),
                    buildRangeSpecification(criteria.getHiredCount(), RecruitmentRequisition_.hiredCount),
                    buildRangeSpecification(criteria.getCheckedInCount(), RecruitmentRequisition_.checkedInCount),
                    buildStringSpecification(criteria.getPriority(), RecruitmentRequisition_.priority),
                    buildRangeSpecification(criteria.getTargetJoinDate(), RecruitmentRequisition_.targetJoinDate),
                    buildRangeSpecification(criteria.getDesiredStartDate(), RecruitmentRequisition_.desiredStartDate),
                    buildRangeSpecification(criteria.getDesiredEndDate(), RecruitmentRequisition_.desiredEndDate),
                    buildRangeSpecification(criteria.getCurrentVersion(), RecruitmentRequisition_.currentVersion),
                    buildRangeSpecification(criteria.getLockedAt(), RecruitmentRequisition_.lockedAt),
                    buildStringSpecification(criteria.getLockedBy(), RecruitmentRequisition_.lockedBy),
                    buildRangeSpecification(criteria.getUnlockedAt(), RecruitmentRequisition_.unlockedAt),
                    buildSpecification(criteria.getVersionsId(), root ->
                        root.join(RecruitmentRequisition_.versionses, JoinType.LEFT).get(RecruitmentRequisitionVersion_.id)
                    ),
                    buildSpecification(criteria.getMembersId(), root ->
                        root.join(RecruitmentRequisition_.memberses, JoinType.LEFT).get(RecruitmentRequisitionMember_.id)
                    ),
                    buildSpecification(criteria.getApplicationsId(), root ->
                        root.join(RecruitmentRequisition_.applicationses, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
