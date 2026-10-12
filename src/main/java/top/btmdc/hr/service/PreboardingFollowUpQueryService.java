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
import top.btmdc.hr.domain.PreboardingFollowUp;
import top.btmdc.hr.repository.PreboardingFollowUpRepository;
import top.btmdc.hr.service.criteria.PreboardingFollowUpCriteria;
import top.btmdc.hr.service.dto.PreboardingFollowUpDTO;
import top.btmdc.hr.service.mapper.PreboardingFollowUpMapper;

/**
 * Service for executing complex queries for {@link PreboardingFollowUp} entities in the database.
 * The main input is a {@link PreboardingFollowUpCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PreboardingFollowUpDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PreboardingFollowUpQueryService extends QueryService<PreboardingFollowUp> {

    private static final Logger LOG = LoggerFactory.getLogger(PreboardingFollowUpQueryService.class);

    private final PreboardingFollowUpRepository preboardingFollowUpRepository;

    private final PreboardingFollowUpMapper preboardingFollowUpMapper;

    public PreboardingFollowUpQueryService(
        PreboardingFollowUpRepository preboardingFollowUpRepository,
        PreboardingFollowUpMapper preboardingFollowUpMapper
    ) {
        this.preboardingFollowUpRepository = preboardingFollowUpRepository;
        this.preboardingFollowUpMapper = preboardingFollowUpMapper;
    }

    /**
     * Return a {@link Page} of {@link PreboardingFollowUpDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PreboardingFollowUpDTO> findByCriteria(PreboardingFollowUpCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PreboardingFollowUp> specification = createSpecification(criteria);
        return preboardingFollowUpRepository.findAll(specification, page).map(preboardingFollowUpMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PreboardingFollowUpCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PreboardingFollowUp> specification = createSpecification(criteria);
        return preboardingFollowUpRepository.count(specification);
    }

    /**
     * Function to convert {@link PreboardingFollowUpCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PreboardingFollowUp> createSpecification(PreboardingFollowUpCriteria criteria) {
        Specification<PreboardingFollowUp> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PreboardingFollowUp_.application, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PreboardingFollowUp_.id),
                    buildRangeSpecification(criteria.getCandidateId(), PreboardingFollowUp_.candidateId),
                    buildRangeSpecification(criteria.getPersonId(), PreboardingFollowUp_.personId),
                    buildRangeSpecification(criteria.getPlannedAt(), PreboardingFollowUp_.plannedAt),
                    buildRangeSpecification(criteria.getContactedAt(), PreboardingFollowUp_.contactedAt),
                    buildStringSpecification(criteria.getOwnerUsername(), PreboardingFollowUp_.ownerUsername),
                    buildStringSpecification(criteria.getContactMethod(), PreboardingFollowUp_.contactMethod),
                    buildStringSpecification(criteria.getCandidateIntent(), PreboardingFollowUp_.candidateIntent),
                    buildStringSpecification(criteria.getRiskLevel(), PreboardingFollowUp_.riskLevel),
                    buildRangeSpecification(criteria.getNextContactAt(), PreboardingFollowUp_.nextContactAt),
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(PreboardingFollowUp_.application, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
