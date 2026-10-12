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
import top.btmdc.hr.domain.ApplicationEvent;
import top.btmdc.hr.repository.ApplicationEventRepository;
import top.btmdc.hr.service.criteria.ApplicationEventCriteria;
import top.btmdc.hr.service.dto.ApplicationEventDTO;
import top.btmdc.hr.service.mapper.ApplicationEventMapper;

/**
 * Service for executing complex queries for {@link ApplicationEvent} entities in the database.
 * The main input is a {@link ApplicationEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ApplicationEventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationEventQueryService extends QueryService<ApplicationEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationEventQueryService.class);

    private final ApplicationEventRepository applicationEventRepository;

    private final ApplicationEventMapper applicationEventMapper;

    public ApplicationEventQueryService(
        ApplicationEventRepository applicationEventRepository,
        ApplicationEventMapper applicationEventMapper
    ) {
        this.applicationEventRepository = applicationEventRepository;
        this.applicationEventMapper = applicationEventMapper;
    }

    /**
     * Return a {@link Page} of {@link ApplicationEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationEventDTO> findByCriteria(ApplicationEventCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ApplicationEvent> specification = createSpecification(criteria);
        return applicationEventRepository.findAll(specification, page).map(applicationEventMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicationEventCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ApplicationEvent> specification = createSpecification(criteria);
        return applicationEventRepository.count(specification);
    }

    /**
     * Function to convert {@link ApplicationEventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ApplicationEvent> createSpecification(ApplicationEventCriteria criteria) {
        Specification<ApplicationEvent> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ApplicationEvent_.application, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ApplicationEvent_.id),
                    buildSpecification(criteria.getStageType(), ApplicationEvent_.stageType),
                    buildStringSpecification(criteria.getEventType(), ApplicationEvent_.eventType),
                    buildStringSpecification(criteria.getPreviousValue(), ApplicationEvent_.previousValue),
                    buildStringSpecification(criteria.getNewValue(), ApplicationEvent_.newValue),
                    buildStringSpecification(criteria.getActor(), ApplicationEvent_.actor),
                    buildRangeSpecification(criteria.getOccurredAt(), ApplicationEvent_.occurredAt),
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(ApplicationEvent_.application, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
