package top.btmdc.hr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import top.btmdc.hr.domain.*; // for static metamodels
import top.btmdc.hr.domain.NotificationTemplate;
import top.btmdc.hr.repository.NotificationTemplateRepository;
import top.btmdc.hr.service.criteria.NotificationTemplateCriteria;
import top.btmdc.hr.service.dto.NotificationTemplateDTO;
import top.btmdc.hr.service.mapper.NotificationTemplateMapper;

/**
 * Service for executing complex queries for {@link NotificationTemplate} entities in the database.
 * The main input is a {@link NotificationTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NotificationTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificationTemplateQueryService extends QueryService<NotificationTemplate> {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationTemplateQueryService.class);

    private final NotificationTemplateRepository notificationTemplateRepository;

    private final NotificationTemplateMapper notificationTemplateMapper;

    public NotificationTemplateQueryService(
        NotificationTemplateRepository notificationTemplateRepository,
        NotificationTemplateMapper notificationTemplateMapper
    ) {
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.notificationTemplateMapper = notificationTemplateMapper;
    }

    /**
     * Return a {@link Page} of {@link NotificationTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationTemplateDTO> findByCriteria(NotificationTemplateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NotificationTemplate> specification = createSpecification(criteria);
        return notificationTemplateRepository.findAll(specification, page).map(notificationTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificationTemplateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NotificationTemplate> specification = createSpecification(criteria);
        return notificationTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificationTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NotificationTemplate> createSpecification(NotificationTemplateCriteria criteria) {
        Specification<NotificationTemplate> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), NotificationTemplate_.id),
                    buildStringSpecification(criteria.getEventType(), NotificationTemplate_.eventType),
                    buildSpecification(criteria.getRecruitmentType(), NotificationTemplate_.recruitmentType),
                    buildStringSpecification(criteria.getLanguage(), NotificationTemplate_.language),
                    buildRangeSpecification(criteria.getVersionNumber(), NotificationTemplate_.versionNumber),
                    buildStringSpecification(criteria.getSubjectTemplate(), NotificationTemplate_.subjectTemplate),
                    buildSpecification(criteria.getExternalSafe(), NotificationTemplate_.externalSafe),
                    buildSpecification(criteria.getPublished(), NotificationTemplate_.published)
                )
            );
        }
        return specification;
    }
}
