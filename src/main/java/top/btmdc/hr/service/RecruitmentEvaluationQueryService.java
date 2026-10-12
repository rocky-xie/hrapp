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
import top.btmdc.hr.domain.RecruitmentEvaluation;
import top.btmdc.hr.repository.RecruitmentEvaluationRepository;
import top.btmdc.hr.service.criteria.RecruitmentEvaluationCriteria;
import top.btmdc.hr.service.dto.RecruitmentEvaluationDTO;
import top.btmdc.hr.service.mapper.RecruitmentEvaluationMapper;

/**
 * Service for executing complex queries for {@link RecruitmentEvaluation} entities in the database.
 * The main input is a {@link RecruitmentEvaluationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RecruitmentEvaluationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecruitmentEvaluationQueryService extends QueryService<RecruitmentEvaluation> {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentEvaluationQueryService.class);

    private final RecruitmentEvaluationRepository recruitmentEvaluationRepository;

    private final RecruitmentEvaluationMapper recruitmentEvaluationMapper;

    public RecruitmentEvaluationQueryService(
        RecruitmentEvaluationRepository recruitmentEvaluationRepository,
        RecruitmentEvaluationMapper recruitmentEvaluationMapper
    ) {
        this.recruitmentEvaluationRepository = recruitmentEvaluationRepository;
        this.recruitmentEvaluationMapper = recruitmentEvaluationMapper;
    }

    /**
     * Return a {@link Page} of {@link RecruitmentEvaluationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecruitmentEvaluationDTO> findByCriteria(RecruitmentEvaluationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecruitmentEvaluation> specification = createSpecification(criteria);
        return recruitmentEvaluationRepository.findAll(specification, page).map(recruitmentEvaluationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecruitmentEvaluationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<RecruitmentEvaluation> specification = createSpecification(criteria);
        return recruitmentEvaluationRepository.count(specification);
    }

    /**
     * Function to convert {@link RecruitmentEvaluationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RecruitmentEvaluation> createSpecification(RecruitmentEvaluationCriteria criteria) {
        Specification<RecruitmentEvaluation> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(RecruitmentEvaluation_.application, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), RecruitmentEvaluation_.id),
                    buildSpecification(criteria.getStageType(), RecruitmentEvaluation_.stageType),
                    buildRangeSpecification(criteria.getActivityId(), RecruitmentEvaluation_.activityId),
                    buildRangeSpecification(criteria.getTemplateVersionId(), RecruitmentEvaluation_.templateVersionId),
                    buildStringSpecification(criteria.getEvaluatorUsername(), RecruitmentEvaluation_.evaluatorUsername),
                    buildStringSpecification(criteria.getRecordedByUsername(), RecruitmentEvaluation_.recordedByUsername),
                    buildStringSpecification(criteria.getStatus(), RecruitmentEvaluation_.status),
                    buildStringSpecification(criteria.getOverallRecommendation(), RecruitmentEvaluation_.overallRecommendation),
                    buildRangeSpecification(criteria.getTotalScore(), RecruitmentEvaluation_.totalScore),
                    buildRangeSpecification(criteria.getEvaluatedAt(), RecruitmentEvaluation_.evaluatedAt),
                    buildRangeSpecification(criteria.getSubmittedAt(), RecruitmentEvaluation_.submittedAt),
                    buildRangeSpecification(criteria.getPublishedAt(), RecruitmentEvaluation_.publishedAt),
                    buildRangeSpecification(criteria.getCorrectionOfId(), RecruitmentEvaluation_.correctionOfId),
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(RecruitmentEvaluation_.application, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
