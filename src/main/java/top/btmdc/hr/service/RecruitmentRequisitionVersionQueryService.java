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
import top.btmdc.hr.domain.RecruitmentRequisitionVersion;
import top.btmdc.hr.repository.RecruitmentRequisitionVersionRepository;
import top.btmdc.hr.service.criteria.RecruitmentRequisitionVersionCriteria;
import top.btmdc.hr.service.dto.RecruitmentRequisitionVersionDTO;
import top.btmdc.hr.service.mapper.RecruitmentRequisitionVersionMapper;

/**
 * Service for executing complex queries for {@link RecruitmentRequisitionVersion} entities in the database.
 * The main input is a {@link RecruitmentRequisitionVersionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RecruitmentRequisitionVersionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecruitmentRequisitionVersionQueryService extends QueryService<RecruitmentRequisitionVersion> {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRequisitionVersionQueryService.class);

    private final RecruitmentRequisitionVersionRepository recruitmentRequisitionVersionRepository;

    private final RecruitmentRequisitionVersionMapper recruitmentRequisitionVersionMapper;

    public RecruitmentRequisitionVersionQueryService(
        RecruitmentRequisitionVersionRepository recruitmentRequisitionVersionRepository,
        RecruitmentRequisitionVersionMapper recruitmentRequisitionVersionMapper
    ) {
        this.recruitmentRequisitionVersionRepository = recruitmentRequisitionVersionRepository;
        this.recruitmentRequisitionVersionMapper = recruitmentRequisitionVersionMapper;
    }

    /**
     * Return a {@link Page} of {@link RecruitmentRequisitionVersionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecruitmentRequisitionVersionDTO> findByCriteria(RecruitmentRequisitionVersionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecruitmentRequisitionVersion> specification = createSpecification(criteria);
        return recruitmentRequisitionVersionRepository.findAll(specification, page).map(recruitmentRequisitionVersionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecruitmentRequisitionVersionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<RecruitmentRequisitionVersion> specification = createSpecification(criteria);
        return recruitmentRequisitionVersionRepository.count(specification);
    }

    /**
     * Function to convert {@link RecruitmentRequisitionVersionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RecruitmentRequisitionVersion> createSpecification(RecruitmentRequisitionVersionCriteria criteria) {
        Specification<RecruitmentRequisitionVersion> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(RecruitmentRequisitionVersion_.requisition, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), RecruitmentRequisitionVersion_.id),
                    buildRangeSpecification(criteria.getVersionNumber(), RecruitmentRequisitionVersion_.versionNumber),
                    buildRangeSpecification(criteria.getLockedAt(), RecruitmentRequisitionVersion_.lockedAt),
                    buildStringSpecification(criteria.getLockedBy(), RecruitmentRequisitionVersion_.lockedBy),
                    buildSpecification(criteria.getApplicationsId(), root ->
                        root.join(RecruitmentRequisitionVersion_.applicationses, JoinType.LEFT).get(RecruitmentApplication_.id)
                    ),
                    buildSpecification(criteria.getRequisitionId(), root ->
                        root.join(RecruitmentRequisitionVersion_.requisition, JoinType.LEFT).get(RecruitmentRequisition_.id)
                    )
                )
            );
        }
        return specification;
    }
}
