package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.RecruitmentRequisitionVersion;

/**
 * Spring Data JPA repository for the RecruitmentRequisitionVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruitmentRequisitionVersionRepository
    extends JpaRepository<RecruitmentRequisitionVersion, Long>, JpaSpecificationExecutor<RecruitmentRequisitionVersion> {}
