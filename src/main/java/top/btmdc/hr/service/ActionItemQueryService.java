package top.btmdc.hr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import top.btmdc.hr.domain.ActionItem;
import top.btmdc.hr.domain.ActionItem_;
import top.btmdc.hr.repository.ActionItemRepository;
import top.btmdc.hr.service.criteria.ActionItemCriteria;
import top.btmdc.hr.service.dto.ActionItemDTO;
import top.btmdc.hr.service.mapper.ActionItemMapper;

/**
 * Service for executing complex queries for {@link ActionItem} entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class ActionItemQueryService extends QueryService<ActionItem> {

    private static final Logger LOG = LoggerFactory.getLogger(ActionItemQueryService.class);

    private final ActionItemRepository actionItemRepository;

    private final ActionItemMapper actionItemMapper;

    public ActionItemQueryService(ActionItemRepository actionItemRepository, ActionItemMapper actionItemMapper) {
        this.actionItemRepository = actionItemRepository;
        this.actionItemMapper = actionItemMapper;
    }

    @Transactional(readOnly = true)
    public Page<ActionItemDTO> findByCriteria(ActionItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ActionItem> specification = createSpecification(criteria);
        return actionItemRepository.findAll(specification, page).map(actionItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(ActionItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ActionItem> specification = createSpecification(criteria);
        return actionItemRepository.count(specification);
    }

    protected Specification<ActionItem> createSpecification(ActionItemCriteria criteria) {
        Specification<ActionItem> specification = Specification.unrestricted();
        if (criteria != null) {
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ActionItem_.id),
                    buildSpecification(criteria.getSourceType(), ActionItem_.sourceType),
                    buildRangeSpecification(criteria.getSourceId(), ActionItem_.sourceId),
                    buildStringSpecification(criteria.getSourceEntityType(), ActionItem_.sourceEntityType),
                    buildStringSpecification(criteria.getDescription(), ActionItem_.description),
                    buildStringSpecification(criteria.getAssignee(), ActionItem_.assignee),
                    buildRangeSpecification(criteria.getDueDate(), ActionItem_.dueDate),
                    buildSpecification(criteria.getStatus(), ActionItem_.status),
                    buildSpecification(criteria.getPriority(), ActionItem_.priority),
                    buildRangeSpecification(criteria.getCreatedAt(), ActionItem_.createdAt),
                    buildRangeSpecification(criteria.getCompletedAt(), ActionItem_.completedAt)
                )
            );
        }
        return specification;
    }
}
