package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.CandidateResume;

/**
 * Spring Data JPA repository for the CandidateResume entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateResumeRepository extends JpaRepository<CandidateResume, Long>, JpaSpecificationExecutor<CandidateResume> {
    java.util.List<CandidateResume> findBySha256(String sha256);

    java.util.Optional<CandidateResume> findFirstByCandidate_IdOrderByVersionNumberDesc(Long candidateId);

    java.util.List<CandidateResume> findByCandidate_Id(Long candidateId);
}
