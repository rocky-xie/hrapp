package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.CandidateProfile;

/**
 * Spring Data JPA repository for the CandidateProfile entity.
 */
@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long>, JpaSpecificationExecutor<CandidateProfile> {
    default Optional<CandidateProfile> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CandidateProfile> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CandidateProfile> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select candidateProfile from CandidateProfile candidateProfile left join fetch candidateProfile.person left join fetch candidateProfile.position left join fetch candidateProfile.observer",
        countQuery = "select count(candidateProfile) from CandidateProfile candidateProfile"
    )
    Page<CandidateProfile> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select candidateProfile from CandidateProfile candidateProfile left join fetch candidateProfile.person left join fetch candidateProfile.position left join fetch candidateProfile.observer"
    )
    List<CandidateProfile> findAllWithToOneRelationships();

    @Query(
        "select candidateProfile from CandidateProfile candidateProfile left join fetch candidateProfile.person left join fetch candidateProfile.position left join fetch candidateProfile.observer where candidateProfile.id =:id"
    )
    Optional<CandidateProfile> findOneWithToOneRelationships(@Param("id") Long id);
}
