package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.PreboardingFollowUp;

/**
 * Spring Data JPA repository for the PreboardingFollowUp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreboardingFollowUpRepository
    extends JpaRepository<PreboardingFollowUp, Long>, JpaSpecificationExecutor<PreboardingFollowUp> {}
