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
import top.btmdc.hr.domain.CandidateResume;
import top.btmdc.hr.repository.CandidateResumeRepository;
import top.btmdc.hr.service.criteria.CandidateResumeCriteria;
import top.btmdc.hr.service.dto.CandidateResumeDTO;
import top.btmdc.hr.service.mapper.CandidateResumeMapper;

/**
 * Service for executing complex queries for {@link CandidateResume} entities in the database.
 * The main input is a {@link CandidateResumeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CandidateResumeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CandidateResumeQueryService extends QueryService<CandidateResume> {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateResumeQueryService.class);

    private final CandidateResumeRepository candidateResumeRepository;

    private final CandidateResumeMapper candidateResumeMapper;

    public CandidateResumeQueryService(CandidateResumeRepository candidateResumeRepository, CandidateResumeMapper candidateResumeMapper) {
        this.candidateResumeRepository = candidateResumeRepository;
        this.candidateResumeMapper = candidateResumeMapper;
    }

    /**
     * Return a {@link Page} of {@link CandidateResumeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CandidateResumeDTO> findByCriteria(CandidateResumeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CandidateResume> specification = createSpecification(criteria);
        return candidateResumeRepository.findAll(specification, page).map(candidateResumeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CandidateResumeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CandidateResume> specification = createSpecification(criteria);
        return candidateResumeRepository.count(specification);
    }

    /**
     * Function to convert {@link CandidateResumeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CandidateResume> createSpecification(CandidateResumeCriteria criteria) {
        Specification<CandidateResume> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CandidateResume_.candidate, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CandidateResume_.id),
                    buildRangeSpecification(criteria.getVersionNumber(), CandidateResume_.versionNumber),
                    buildRangeSpecification(criteria.getReceivedAt(), CandidateResume_.receivedAt),
                    buildStringSpecification(criteria.getOriginalFileName(), CandidateResume_.originalFileName),
                    buildStringSpecification(criteria.getMediaType(), CandidateResume_.mediaType),
                    buildRangeSpecification(criteria.getFileSize(), CandidateResume_.fileSize),
                    buildStringSpecification(criteria.getSha256(), CandidateResume_.sha256),
                    buildStringSpecification(criteria.getStorageKey(), CandidateResume_.storageKey),
                    buildSpecification(criteria.getActiveVersion(), CandidateResume_.activeVersion),
                    buildStringSpecification(criteria.getUploadedBy(), CandidateResume_.uploadedBy),
                    buildSpecification(criteria.getCandidateId(), root ->
                        root.join(CandidateResume_.candidate, JoinType.LEFT).get(ExternalCandidate_.id)
                    )
                )
            );
        }
        return specification;
    }
}
