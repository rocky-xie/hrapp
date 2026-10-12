package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.RecruitmentApplication;

/**
 * Spring Data JPA repository for the RecruitmentApplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruitmentApplicationRepository
    extends JpaRepository<RecruitmentApplication, Long>, JpaSpecificationExecutor<RecruitmentApplication> {}
