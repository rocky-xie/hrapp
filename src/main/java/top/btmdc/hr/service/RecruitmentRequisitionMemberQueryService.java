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
import top.btmdc.hr.domain.RecruitmentRequisitionMember;
import top.btmdc.hr.repository.RecruitmentRequisitionMemberRepository;
import top.btmdc.hr.service.criteria.RecruitmentRequisitionMemberCriteria;
import top.btmdc.hr.service.dto.RecruitmentRequisitionMemberDTO;
import top.btmdc.hr.service.mapper.RecruitmentRequisitionMemberMapper;

/**
 * Service for executing complex queries for {@link RecruitmentRequisitionMember} entities in the database.
 * The main input is a {@link RecruitmentRequisitionMemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RecruitmentRequisitionMemberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecruitmentRequisitionMemberQueryService extends QueryService<RecruitmentRequisitionMember> {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRequisitionMemberQueryService.class);

    private final RecruitmentRequisitionMemberRepository recruitmentRequisitionMemberRepository;

    private final RecruitmentRequisitionMemberMapper recruitmentRequisitionMemberMapper;

    public RecruitmentRequisitionMemberQueryService(
        RecruitmentRequisitionMemberRepository recruitmentRequisitionMemberRepository,
        RecruitmentRequisitionMemberMapper recruitmentRequisitionMemberMapper
    ) {
        this.recruitmentRequisitionMemberRepository = recruitmentRequisitionMemberRepository;
        this.recruitmentRequisitionMemberMapper = recruitmentRequisitionMemberMapper;
    }

    /**
     * Return a {@link Page} of {@link RecruitmentRequisitionMemberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecruitmentRequisitionMemberDTO> findByCriteria(RecruitmentRequisitionMemberCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecruitmentRequisitionMember> specification = createSpecification(criteria);
        return recruitmentRequisitionMemberRepository.findAll(specification, page).map(recruitmentRequisitionMemberMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecruitmentRequisitionMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<RecruitmentRequisitionMember> specification = createSpecification(criteria);
        return recruitmentRequisitionMemberRepository.count(specification);
    }

    /**
     * Function to convert {@link RecruitmentRequisitionMemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RecruitmentRequisitionMember> createSpecification(RecruitmentRequisitionMemberCriteria criteria) {
        Specification<RecruitmentRequisitionMember> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(RecruitmentRequisitionMember_.requisition, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), RecruitmentRequisitionMember_.id),
                    buildStringSpecification(criteria.getUsername(), RecruitmentRequisitionMember_.username),
                    buildStringSpecification(criteria.getMemberType(), RecruitmentRequisitionMember_.memberType),
                    buildSpecification(criteria.getActive(), RecruitmentRequisitionMember_.active),
                    buildRangeSpecification(criteria.getCreatedAt(), RecruitmentRequisitionMember_.createdAt),
                    buildRangeSpecification(criteria.getRemovedAt(), RecruitmentRequisitionMember_.removedAt),
                    buildSpecification(criteria.getRequisitionId(), root ->
                        root.join(RecruitmentRequisitionMember_.requisition, JoinType.LEFT).get(RecruitmentRequisition_.id)
                    )
                )
            );
        }
        return specification;
    }
}
