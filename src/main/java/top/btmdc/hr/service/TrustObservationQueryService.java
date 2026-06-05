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
import top.btmdc.hr.domain.TrustObservation;
import top.btmdc.hr.repository.TrustObservationRepository;
import top.btmdc.hr.service.criteria.TrustObservationCriteria;
import top.btmdc.hr.service.dto.TrustObservationDTO;
import top.btmdc.hr.service.mapper.TrustObservationMapper;

/**
 * Service for executing complex queries for {@link TrustObservation} entities in the database.
 * The main input is a {@link TrustObservationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TrustObservationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrustObservationQueryService extends QueryService<TrustObservation> {

    private static final Logger LOG = LoggerFactory.getLogger(TrustObservationQueryService.class);

    private final TrustObservationRepository trustObservationRepository;

    private final TrustObservationMapper trustObservationMapper;

    public TrustObservationQueryService(
        TrustObservationRepository trustObservationRepository,
        TrustObservationMapper trustObservationMapper
    ) {
        this.trustObservationRepository = trustObservationRepository;
        this.trustObservationMapper = trustObservationMapper;
    }

    /**
     * Return a {@link Page} of {@link TrustObservationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrustObservationDTO> findByCriteria(TrustObservationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TrustObservation> specification = createSpecification(criteria);
        return trustObservationRepository.findAll(specification, page).map(trustObservationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrustObservationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TrustObservation> specification = createSpecification(criteria);
        return trustObservationRepository.count(specification);
    }

    /**
     * Function to convert {@link TrustObservationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TrustObservation> createSpecification(TrustObservationCriteria criteria) {
        Specification<TrustObservation> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(TrustObservation_.person, JoinType.LEFT);
                root.fetch(TrustObservation_.observer, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), TrustObservation_.id),
                    buildRangeSpecification(criteria.getObservationDate(), TrustObservation_.observationDate),
                    buildSpecification(criteria.getTrustStage(), TrustObservation_.trustStage),
                    buildSpecification(criteria.getPersonId(), root -> root.join(TrustObservation_.person, JoinType.LEFT).get(Person_.id)),
                    buildSpecification(criteria.getObserverId(), root ->
                        root.join(TrustObservation_.observer, JoinType.LEFT).get(Person_.id)
                    )
                )
            );
        }
        return specification;
    }
}
