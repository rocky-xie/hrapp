package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.HiringDecision;

/**
 * Spring Data JPA repository for the HiringDecision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HiringDecisionRepository extends JpaRepository<HiringDecision, Long>, JpaSpecificationExecutor<HiringDecision> {}
