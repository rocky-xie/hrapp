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
import top.btmdc.hr.domain.PersonRisk;
import top.btmdc.hr.repository.PersonRiskRepository;
import top.btmdc.hr.service.criteria.PersonRiskCriteria;
import top.btmdc.hr.service.dto.PersonRiskDTO;
import top.btmdc.hr.service.mapper.PersonRiskMapper;

/**
 * Service for executing complex queries for {@link PersonRisk} entities in the database.
 * The main input is a {@link PersonRiskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PersonRiskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonRiskQueryService extends QueryService<PersonRisk> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonRiskQueryService.class);

    private final PersonRiskRepository personRiskRepository;

    private final PersonRiskMapper personRiskMapper;

    public PersonRiskQueryService(PersonRiskRepository personRiskRepository, PersonRiskMapper personRiskMapper) {
        this.personRiskRepository = personRiskRepository;
        this.personRiskMapper = personRiskMapper;
    }

    /**
     * Return a {@link Page} of {@link PersonRiskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonRiskDTO> findByCriteria(PersonRiskCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonRisk> specification = createSpecification(criteria);
        return personRiskRepository.findAll(specification, page).map(personRiskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonRiskCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PersonRisk> specification = createSpecification(criteria);
        return personRiskRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonRiskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PersonRisk> createSpecification(PersonRiskCriteria criteria) {
        Specification<PersonRisk> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PersonRisk_.person, JoinType.LEFT);
                root.fetch(PersonRisk_.position, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PersonRisk_.id),
                    buildSpecification(criteria.getRiskType(), PersonRisk_.riskType),
                    buildSpecification(criteria.getRiskLevel(), PersonRisk_.riskLevel),
                    buildRangeSpecification(criteria.getIdentifiedDate(), PersonRisk_.identifiedDate),
                    buildRangeSpecification(criteria.getTargetDate(), PersonRisk_.targetDate),
                    buildRangeSpecification(criteria.getClosedDate(), PersonRisk_.closedDate),
                    buildSpecification(criteria.getPersonId(), root -> root.join(PersonRisk_.person, JoinType.LEFT).get(Person_.id)),
                    buildSpecification(criteria.getPositionId(), root -> root.join(PersonRisk_.position, JoinType.LEFT).get(Position_.id))
                )
            );
        }
        return specification;
    }
}
