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
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.repository.PersonRepository;
import top.btmdc.hr.service.criteria.PersonCriteria;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.mapper.PersonMapper;

/**
 * Service for executing complex queries for {@link Person} entities in the database.
 * The main input is a {@link PersonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PersonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonQueryService extends QueryService<Person> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonQueryService.class);

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    public PersonQueryService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    /**
     * Return a {@link Page} of {@link PersonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonDTO> findByCriteria(PersonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.findAll(specification, page).map(personMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Person> createSpecification(PersonCriteria criteria) {
        Specification<Person> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Person_.id),
                    buildStringSpecification(criteria.getEmployeeCode(), Person_.employeeCode),
                    buildStringSpecification(criteria.getPersonName(), Person_.personName),
                    buildRangeSpecification(criteria.getAge(), Person_.age),
                    buildSpecification(criteria.getGender(), Person_.gender),
                    buildStringSpecification(criteria.getDepartment(), Person_.department),
                    buildStringSpecification(criteria.getCurrentRole(), Person_.currentRole),
                    buildSpecification(criteria.getEmploymentStatus(), Person_.employmentStatus),
                    buildRangeSpecification(criteria.getJoinDate(), Person_.joinDate),
                    buildSpecification(criteria.getMentorFlag(), Person_.mentorFlag),
                    buildSpecification(criteria.getCoreCandidateFlag(), Person_.coreCandidateFlag)
                )
            );
        }
        return specification;
    }
}
