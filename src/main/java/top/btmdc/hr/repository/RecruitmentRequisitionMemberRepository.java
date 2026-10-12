package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.RecruitmentRequisitionMember;

/**
 * Spring Data JPA repository for the RecruitmentRequisitionMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruitmentRequisitionMemberRepository
    extends JpaRepository<RecruitmentRequisitionMember, Long>, JpaSpecificationExecutor<RecruitmentRequisitionMember> {}
