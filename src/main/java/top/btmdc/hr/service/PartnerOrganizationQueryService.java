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
import top.btmdc.hr.domain.PartnerOrganization;
import top.btmdc.hr.repository.PartnerOrganizationRepository;
import top.btmdc.hr.service.criteria.PartnerOrganizationCriteria;
import top.btmdc.hr.service.dto.PartnerOrganizationDTO;
import top.btmdc.hr.service.mapper.PartnerOrganizationMapper;

/**
 * Service for executing complex queries for {@link PartnerOrganization} entities in the database.
 * The main input is a {@link PartnerOrganizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PartnerOrganizationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartnerOrganizationQueryService extends QueryService<PartnerOrganization> {

    private static final Logger LOG = LoggerFactory.getLogger(PartnerOrganizationQueryService.class);

    private final PartnerOrganizationRepository partnerOrganizationRepository;

    private final PartnerOrganizationMapper partnerOrganizationMapper;

    public PartnerOrganizationQueryService(
        PartnerOrganizationRepository partnerOrganizationRepository,
        PartnerOrganizationMapper partnerOrganizationMapper
    ) {
        this.partnerOrganizationRepository = partnerOrganizationRepository;
        this.partnerOrganizationMapper = partnerOrganizationMapper;
    }

    /**
     * Return a {@link Page} of {@link PartnerOrganizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartnerOrganizationDTO> findByCriteria(PartnerOrganizationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartnerOrganization> specification = createSpecification(criteria);
        return partnerOrganizationRepository.findAll(specification, page).map(partnerOrganizationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartnerOrganizationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartnerOrganization> specification = createSpecification(criteria);
        return partnerOrganizationRepository.count(specification);
    }

    /**
     * Function to convert {@link PartnerOrganizationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartnerOrganization> createSpecification(PartnerOrganizationCriteria criteria) {
        Specification<PartnerOrganization> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartnerOrganization_.id),
                    buildStringSpecification(criteria.getPartnerCode(), PartnerOrganization_.partnerCode),
                    buildStringSpecification(criteria.getName(), PartnerOrganization_.name),
                    buildSpecification(criteria.getDispatchEnabled(), PartnerOrganization_.dispatchEnabled),
                    buildRangeSpecification(criteria.getDefaultObservationDays(), PartnerOrganization_.defaultObservationDays),
                    buildSpecification(criteria.getActive(), PartnerOrganization_.active)
                )
            );
        }
        return specification;
    }
}
