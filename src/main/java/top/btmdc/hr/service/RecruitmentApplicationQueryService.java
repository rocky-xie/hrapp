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
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.repository.RecruitmentApplicationRepository;
import top.btmdc.hr.service.criteria.RecruitmentApplicationCriteria;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;
import top.btmdc.hr.service.mapper.RecruitmentApplicationMapper;

/**
 * Service for executing complex queries for {@link RecruitmentApplication} entities in the database.
 * The main input is a {@link RecruitmentApplicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RecruitmentApplicationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecruitmentApplicationQueryService extends QueryService<RecruitmentApplication> {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentApplicationQueryService.class);

    private final RecruitmentApplicationRepository recruitmentApplicationRepository;

    private final RecruitmentApplicationMapper recruitmentApplicationMapper;

    public RecruitmentApplicationQueryService(
        RecruitmentApplicationRepository recruitmentApplicationRepository,
        RecruitmentApplicationMapper recruitmentApplicationMapper
    ) {
        this.recruitmentApplicationRepository = recruitmentApplicationRepository;
        this.recruitmentApplicationMapper = recruitmentApplicationMapper;
    }

    /**
     * Return a {@link Page} of {@link RecruitmentApplicationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecruitmentApplicationDTO> findByCriteria(RecruitmentApplicationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecruitmentApplication> specification = createSpecification(criteria);
        return recruitmentApplicationRepository.findAll(specification, page).map(recruitmentApplicationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecruitmentApplicationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<RecruitmentApplication> specification = createSpecification(criteria);
        return recruitmentApplicationRepository.count(specification);
    }

    /**
     * Function to convert {@link RecruitmentApplicationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RecruitmentApplication> createSpecification(RecruitmentApplicationCriteria criteria) {
        Specification<RecruitmentApplication> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(RecruitmentApplication_.candidate, JoinType.LEFT);
                root.fetch(RecruitmentApplication_.requisition, JoinType.LEFT);
                root.fetch(RecruitmentApplication_.requisitionVersion, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), RecruitmentApplication_.id),
                    buildStringSpecification(criteria.getApplicationCode(), RecruitmentApplication_.applicationCode),
                    buildSpecification(criteria.getRecruitmentType(), RecruitmentApplication_.recruitmentType),
                    buildSpecification(criteria.getCurrentStage(), RecruitmentApplication_.currentStage),
                    buildSpecification(criteria.getStatus(), RecruitmentApplication_.status),
                    buildSpecification(criteria.getDecision(), RecruitmentApplication_.decision),
                    buildStringSpecification(criteria.getSourceType(), RecruitmentApplication_.sourceType),
                    buildStringSpecification(criteria.getSourceDetail(), RecruitmentApplication_.sourceDetail),
                    buildStringSpecification(criteria.getBatchName(), RecruitmentApplication_.batchName),
                    buildRangeSpecification(criteria.getAppliedAt(), RecruitmentApplication_.appliedAt),
                    buildRangeSpecification(criteria.getClosedAt(), RecruitmentApplication_.closedAt),
                    buildRangeSpecification(criteria.getRetentionUntil(), RecruitmentApplication_.retentionUntil),
                    buildRangeSpecification(criteria.getConvertedPersonId(), RecruitmentApplication_.convertedPersonId),
                    buildRangeSpecification(criteria.getConvertedAt(), RecruitmentApplication_.convertedAt),
                    buildSpecification(criteria.getStagesId(), root ->
                        root.join(RecruitmentApplication_.stageses, JoinType.LEFT).get(ApplicationStage_.id)
                    ),
                    buildSpecification(criteria.getEventsId(), root ->
                        root.join(RecruitmentApplication_.eventses, JoinType.LEFT).get(ApplicationEvent_.id)
                    ),
                    buildSpecification(criteria.getActivitiesId(), root ->
                        root.join(RecruitmentApplication_.activitieses, JoinType.LEFT).get(SelectionActivity_.id)
                    ),
                    buildSpecification(criteria.getEvaluationsId(), root ->
                        root.join(RecruitmentApplication_.evaluationses, JoinType.LEFT).get(RecruitmentEvaluation_.id)
                    ),
                    buildSpecification(criteria.getDecisionsId(), root ->
                        root.join(RecruitmentApplication_.decisionses, JoinType.LEFT).get(HiringDecision_.id)
                    ),
                    buildSpecification(criteria.getMessagesId(), root ->
                        root.join(RecruitmentApplication_.messageses, JoinType.LEFT).get(NotificationMessage_.id)
                    ),
                    buildSpecification(criteria.getFollowUpsId(), root ->
                        root.join(RecruitmentApplication_.followUpses, JoinType.LEFT).get(PreboardingFollowUp_.id)
                    ),
                    buildSpecification(criteria.getDispatchAssignmentsId(), root ->
                        root.join(RecruitmentApplication_.dispatchAssignmentses, JoinType.LEFT).get(DispatchAssignment_.id)
                    ),
                    buildSpecification(criteria.getCandidateId(), root ->
                        root.join(RecruitmentApplication_.candidate, JoinType.LEFT).get(ExternalCandidate_.id)
                    ),
                    buildSpecification(criteria.getRequisitionId(), root ->
                        root.join(RecruitmentApplication_.requisition, JoinType.LEFT).get(RecruitmentRequisition_.id)
                    ),
                    buildSpecification(criteria.getRequisitionVersionId(), root ->
                        root.join(RecruitmentApplication_.requisitionVersion, JoinType.LEFT).get(RecruitmentRequisitionVersion_.id)
                    )
                )
            );
        }
        return specification;
    }
}
