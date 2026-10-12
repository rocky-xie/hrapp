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
import top.btmdc.hr.domain.ApplicationStage;
import top.btmdc.hr.repository.ApplicationStageRepository;
import top.btmdc.hr.service.criteria.ApplicationStageCriteria;
import top.btmdc.hr.service.dto.ApplicationStageDTO;
import top.btmdc.hr.service.mapper.ApplicationStageMapper;

/**
 * Service for executing complex queries for {@link ApplicationStage} entities in the database.
 * The main input is a {@link ApplicationStageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ApplicationStageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationStageQueryService extends QueryService<ApplicationStage> {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStageQueryService.class);

    private final ApplicationStageRepository applicationStageRepository;

    private final ApplicationStageMapper applicationStageMapper;

    public ApplicationStageQueryService(
        ApplicationStageRepository applicationStageRepository,
        ApplicationStageMapper applicationStageMapper
    ) {
        this.applicationStageRepository = applicationStageRepository;
        this.applicationStageMapper = applicationStageMapper;
    }

    /**
     * Return a {@link Page} of {@link ApplicationStageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationStageDTO> findByCriteria(ApplicationStageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ApplicationStage> specification = createSpecification(criteria);
        return applicationStageRepository.findAll(specification, page).map(applicationStageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicationStageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ApplicationStage> specification = createSpecification(criteria);
        return applicationStageRepository.count(specification);
    }

    /**
     * Function to convert {@link ApplicationStageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ApplicationStage> createSpecification(ApplicationStageCriteria criteria) {
        Specification<ApplicationStage> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ApplicationStage_.application, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ApplicationStage_.id),
                    buildSpecification(criteria.getStageType(), ApplicationStage_.stageType),
                    buildRangeSpecification(criteria.getSequenceNumber(), ApplicationStage_.sequenceNumber),
                    buildStringSpecification(criteria.getStageStatus(), ApplicationStage_.stageStatus),
                    buildRangeSpecification(criteria.getStartedAt(), ApplicationStage_.startedAt),
                    buildRangeSpecification(criteria.getCompletedAt(), ApplicationStage_.completedAt),
                    buildSpecification(criteria.getSelected(), ApplicationStage_.selected),
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(ApplicationStage_.application, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
