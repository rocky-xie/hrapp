package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.ApplicationStage;

/**
 * Spring Data JPA repository for the ApplicationStage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationStageRepository extends JpaRepository<ApplicationStage, Long>, JpaSpecificationExecutor<ApplicationStage> {}
