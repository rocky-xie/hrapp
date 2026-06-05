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
import top.btmdc.hr.domain.PositionMatch;
import top.btmdc.hr.repository.PositionMatchRepository;
import top.btmdc.hr.service.criteria.PositionMatchCriteria;
import top.btmdc.hr.service.dto.PositionMatchDTO;
import top.btmdc.hr.service.mapper.PositionMatchMapper;

/**
 * Service for executing complex queries for {@link PositionMatch} entities in the database.
 * The main input is a {@link PositionMatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PositionMatchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PositionMatchQueryService extends QueryService<PositionMatch> {

    private static final Logger LOG = LoggerFactory.getLogger(PositionMatchQueryService.class);

    private final PositionMatchRepository positionMatchRepository;

    private final PositionMatchMapper positionMatchMapper;

    public PositionMatchQueryService(PositionMatchRepository positionMatchRepository, PositionMatchMapper positionMatchMapper) {
        this.positionMatchRepository = positionMatchRepository;
        this.positionMatchMapper = positionMatchMapper;
    }

    /**
     * Return a {@link Page} of {@link PositionMatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PositionMatchDTO> findByCriteria(PositionMatchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PositionMatch> specification = createSpecification(criteria);
        return positionMatchRepository.findAll(specification, page).map(positionMatchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PositionMatchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PositionMatch> specification = createSpecification(criteria);
        return positionMatchRepository.count(specification);
    }

    /**
     * Function to convert {@link PositionMatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PositionMatch> createSpecification(PositionMatchCriteria criteria) {
        Specification<PositionMatch> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PositionMatch_.person, JoinType.LEFT);
                root.fetch(PositionMatch_.position, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PositionMatch_.id),
                    buildRangeSpecification(criteria.getMatchScore(), PositionMatch_.matchScore),
                    buildSpecification(criteria.getReadiness(), PositionMatch_.readiness),
                    buildSpecification(criteria.getRecommendation(), PositionMatch_.recommendation),
                    buildRangeSpecification(criteria.getAnalysisDate(), PositionMatch_.analysisDate),
                    buildSpecification(criteria.getPersonId(), root -> root.join(PositionMatch_.person, JoinType.LEFT).get(Person_.id)),
                    buildSpecification(criteria.getPositionId(), root ->
                        root.join(PositionMatch_.position, JoinType.LEFT).get(Position_.id)
                    )
                )
            );
        }
        return specification;
    }
}
