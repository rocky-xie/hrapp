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
import top.btmdc.hr.domain.SelectionActivity;
import top.btmdc.hr.repository.SelectionActivityRepository;
import top.btmdc.hr.service.criteria.SelectionActivityCriteria;
import top.btmdc.hr.service.dto.SelectionActivityDTO;
import top.btmdc.hr.service.mapper.SelectionActivityMapper;

/**
 * Service for executing complex queries for {@link SelectionActivity} entities in the database.
 * The main input is a {@link SelectionActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SelectionActivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SelectionActivityQueryService extends QueryService<SelectionActivity> {

    private static final Logger LOG = LoggerFactory.getLogger(SelectionActivityQueryService.class);

    private final SelectionActivityRepository selectionActivityRepository;

    private final SelectionActivityMapper selectionActivityMapper;

    public SelectionActivityQueryService(
        SelectionActivityRepository selectionActivityRepository,
        SelectionActivityMapper selectionActivityMapper
    ) {
        this.selectionActivityRepository = selectionActivityRepository;
        this.selectionActivityMapper = selectionActivityMapper;
    }

    /**
     * Return a {@link Page} of {@link SelectionActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SelectionActivityDTO> findByCriteria(SelectionActivityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SelectionActivity> specification = createSpecification(criteria);
        return selectionActivityRepository.findAll(specification, page).map(selectionActivityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SelectionActivityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SelectionActivity> specification = createSpecification(criteria);
        return selectionActivityRepository.count(specification);
    }

    /**
     * Function to convert {@link SelectionActivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SelectionActivity> createSpecification(SelectionActivityCriteria criteria) {
        Specification<SelectionActivity> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(SelectionActivity_.application, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), SelectionActivity_.id),
                    buildSpecification(criteria.getStageType(), SelectionActivity_.stageType),
                    buildSpecification(criteria.getActivityType(), SelectionActivity_.activityType),
                    buildRangeSpecification(criteria.getStartAt(), SelectionActivity_.startAt),
                    buildRangeSpecification(criteria.getEndAt(), SelectionActivity_.endAt),
                    buildRangeSpecification(criteria.getMeetingRoomId(), SelectionActivity_.meetingRoomId),
                    buildStringSpecification(criteria.getLocation(), SelectionActivity_.location),
                    buildStringSpecification(criteria.getStatus(), SelectionActivity_.status),
                    buildRangeSpecification(criteria.getScheduleVersion(), SelectionActivity_.scheduleVersion),
                    buildStringSpecification(criteria.getExternalCalendarProvider(), SelectionActivity_.externalCalendarProvider),
                    buildStringSpecification(criteria.getExternalEventId(), SelectionActivity_.externalEventId),
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(SelectionActivity_.application, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
