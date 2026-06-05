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
import top.btmdc.hr.domain.CandidateProfile;
import top.btmdc.hr.repository.CandidateProfileRepository;
import top.btmdc.hr.service.criteria.CandidateProfileCriteria;
import top.btmdc.hr.service.dto.CandidateProfileDTO;
import top.btmdc.hr.service.mapper.CandidateProfileMapper;

/**
 * Service for executing complex queries for {@link CandidateProfile} entities in the database.
 * The main input is a {@link CandidateProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CandidateProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CandidateProfileQueryService extends QueryService<CandidateProfile> {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateProfileQueryService.class);

    private final CandidateProfileRepository candidateProfileRepository;

    private final CandidateProfileMapper candidateProfileMapper;

    public CandidateProfileQueryService(
        CandidateProfileRepository candidateProfileRepository,
        CandidateProfileMapper candidateProfileMapper
    ) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.candidateProfileMapper = candidateProfileMapper;
    }

    /**
     * Return a {@link Page} of {@link CandidateProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CandidateProfileDTO> findByCriteria(CandidateProfileCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CandidateProfile> specification = createSpecification(criteria);
        return candidateProfileRepository.findAll(specification, page).map(candidateProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CandidateProfileCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CandidateProfile> specification = createSpecification(criteria);
        return candidateProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link CandidateProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CandidateProfile> createSpecification(CandidateProfileCriteria criteria) {
        Specification<CandidateProfile> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CandidateProfile_.person, JoinType.LEFT);
                root.fetch(CandidateProfile_.position, JoinType.LEFT);
                root.fetch(CandidateProfile_.observer, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CandidateProfile_.id),
                    buildRangeSpecification(criteria.getCandidateDate(), CandidateProfile_.candidateDate),
                    buildStringSpecification(criteria.getCultivateDirection(), CandidateProfile_.cultivateDirection),
                    buildSpecification(criteria.getStability(), CandidateProfile_.stability),
                    buildSpecification(criteria.getLearningAbility(), CandidateProfile_.learningAbility),
                    buildSpecification(criteria.getCommunicationCoordination(), CandidateProfile_.communicationCoordination),
                    buildSpecification(criteria.getBusinessUnderstanding(), CandidateProfile_.businessUnderstanding),
                    buildSpecification(criteria.getResponsibility(), CandidateProfile_.responsibility),
                    buildSpecification(criteria.getRiskAwareness(), CandidateProfile_.riskAwareness),
                    buildSpecification(criteria.getJudgement(), CandidateProfile_.judgement),
                    buildSpecification(criteria.getPersonId(), root -> root.join(CandidateProfile_.person, JoinType.LEFT).get(Person_.id)),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(CandidateProfile_.position, JoinType.LEFT).get(Position_.id)
                    ),
                    buildSpecification(criteria.getObserverId(), root ->
                        root.join(CandidateProfile_.observer, JoinType.LEFT).get(Person_.id)
                    )
                )
            );
        }
        return specification;
    }
}
