package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.ExternalCandidate;

/**
 * Spring Data JPA repository for the ExternalCandidate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExternalCandidateRepository extends JpaRepository<ExternalCandidate, Long>, JpaSpecificationExecutor<ExternalCandidate> {}
