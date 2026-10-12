package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.RecruitmentRequisition;

/**
 * Spring Data JPA repository for the RecruitmentRequisition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruitmentRequisitionRepository
    extends JpaRepository<RecruitmentRequisition, Long>, JpaSpecificationExecutor<RecruitmentRequisition> {}
