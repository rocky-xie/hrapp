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
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.repository.ExternalCandidateRepository;
import top.btmdc.hr.service.criteria.ExternalCandidateCriteria;
import top.btmdc.hr.service.dto.ExternalCandidateDTO;
import top.btmdc.hr.service.mapper.ExternalCandidateMapper;

/**
 * Service for executing complex queries for {@link ExternalCandidate} entities in the database.
 * The main input is a {@link ExternalCandidateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExternalCandidateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExternalCandidateQueryService extends QueryService<ExternalCandidate> {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalCandidateQueryService.class);

    private final ExternalCandidateRepository externalCandidateRepository;

    private final ExternalCandidateMapper externalCandidateMapper;

    public ExternalCandidateQueryService(
        ExternalCandidateRepository externalCandidateRepository,
        ExternalCandidateMapper externalCandidateMapper
    ) {
        this.externalCandidateRepository = externalCandidateRepository;
        this.externalCandidateMapper = externalCandidateMapper;
    }

    /**
     * Return a {@link Page} of {@link ExternalCandidateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExternalCandidateDTO> findByCriteria(ExternalCandidateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExternalCandidate> specification = createSpecification(criteria);
        return externalCandidateRepository.findAll(specification, page).map(externalCandidateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExternalCandidateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExternalCandidate> specification = createSpecification(criteria);
        return externalCandidateRepository.count(specification);
    }

    /**
     * Function to convert {@link ExternalCandidateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExternalCandidate> createSpecification(ExternalCandidateCriteria criteria) {
        Specification<ExternalCandidate> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ExternalCandidate_.id),
                    buildStringSpecification(criteria.getFullName(), ExternalCandidate_.fullName),
                    buildStringSpecification(criteria.getNameReading(), ExternalCandidate_.nameReading),
                    buildStringSpecification(criteria.getEnglishName(), ExternalCandidate_.englishName),
                    buildRangeSpecification(criteria.getBirthDate(), ExternalCandidate_.birthDate),
                    buildStringSpecification(criteria.getGender(), ExternalCandidate_.gender),
                    buildStringSpecification(criteria.getPhone(), ExternalCandidate_.phone),
                    buildStringSpecification(criteria.getEmail(), ExternalCandidate_.email),
                    buildRangeSpecification(criteria.getConvertedPersonId(), ExternalCandidate_.convertedPersonId),
                    buildRangeSpecification(criteria.getCreatedAt(), ExternalCandidate_.createdAt),
                    buildRangeSpecification(criteria.getUpdatedAt(), ExternalCandidate_.updatedAt),
                    buildSpecification(criteria.getResumesId(), root ->
                        root.join(ExternalCandidate_.resumeses, JoinType.LEFT).get(CandidateResume_.id)
                    ),
                    buildSpecification(criteria.getApplicationsId(), root ->
                        root.join(ExternalCandidate_.applicationses, JoinType.LEFT).get(RecruitmentApplication_.id)
                    )
                )
            );
        }
        return specification;
    }
}
