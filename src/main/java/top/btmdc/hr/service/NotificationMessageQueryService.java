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
import top.btmdc.hr.domain.NotificationMessage;
import top.btmdc.hr.repository.NotificationMessageRepository;
import top.btmdc.hr.service.criteria.NotificationMessageCriteria;
import top.btmdc.hr.service.dto.NotificationMessageDTO;
import top.btmdc.hr.service.mapper.NotificationMessageMapper;

/**
 * Service for executing complex queries for {@link NotificationMessage} entities in the database.
 * The main input is a {@link NotificationMessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NotificationMessageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificationMessageQueryService extends QueryService<NotificationMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationMessageQueryService.class);

    private final NotificationMessageRepository notificationMessageRepository;

    private final NotificationMessageMapper notificationMessageMapper;

    public NotificationMessageQueryService(
        NotificationMessageRepository notificationMessageRepository,
        NotificationMessageMapper notificationMessageMapper
    ) {
        this.notificationMessageRepository = notificationMessageRepository;
        this.notificationMessageMapper = notificationMessageMapper;
    }

    /**
     * Return a {@link Page} of {@link NotificationMessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationMessageDTO> findByCriteria(NotificationMessageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NotificationMessage> specification = createSpecification(criteria);
        return notificationMessageRepository.findAll(specification, page).map(notificationMessageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificationMessageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NotificationMessage> specification = createSpecification(criteria);
        return notificationMessageRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificationMessageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NotificationMessage> createSpecification(NotificationMessageCriteria criteria) {
        Specification<NotificationMessage> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(NotificationMessage_.application, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), NotificationMessage_.id),
                    buildStringSpecification(criteria.getEventType(), NotificationMessage_.eventType),
                    buildRangeSpecification(criteria.getTemplateId(), NotificationMessage_.templateId),
                    buildSpecification(criteria.getStatus(), NotificationMessage_.status),
                    buildStringSpecification(criteria.getSendMode(), NotificationMessage_.sendMode),
                    buildStringSpecification(criteria.getFinalSubject(), NotificationMessage_.finalSubject),
                    buildRangeSpecification(criteria.getRetryCount(), NotificationMessage_.retryCount),
                    buildRangeSpecification(criteria.getMaxRetries(), NotificationMessage_.maxRetries),
                    buildStringSpecification(criteria.getCreatedBy(), NotificationMessage_.createdBy),
                    buildStringSpecification(criteria.getConfirmedBy(), NotificationMessage_.confirmedBy),
                    buildRangeSpecification(criteria.getCreatedAt(), NotificationMessage_.createdAt),
                    buildRangeSpecification(criteria.getConfirmedAt(), NotificationMessage_.confirmedAt),
                    buildRangeSpecification(criteria.getSentAt(), NotificationMessage_.sentAt),
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(NotificationMessage_.application, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
