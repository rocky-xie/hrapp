package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.SuccessionCandidate;

/**
 * Spring Data JPA repository for the SuccessionCandidate entity.
 */
@Repository
public interface SuccessionCandidateRepository
    extends JpaRepository<SuccessionCandidate, Long>, JpaSpecificationExecutor<SuccessionCandidate>
{
    default Optional<SuccessionCandidate> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SuccessionCandidate> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SuccessionCandidate> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select successionCandidate from SuccessionCandidate successionCandidate left join fetch successionCandidate.position left join fetch successionCandidate.currentOwner left join fetch successionCandidate.candidate",
        countQuery = "select count(successionCandidate) from SuccessionCandidate successionCandidate"
    )
    Page<SuccessionCandidate> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select successionCandidate from SuccessionCandidate successionCandidate left join fetch successionCandidate.position left join fetch successionCandidate.currentOwner left join fetch successionCandidate.candidate"
    )
    List<SuccessionCandidate> findAllWithToOneRelationships();

    @Query(
        "select successionCandidate from SuccessionCandidate successionCandidate left join fetch successionCandidate.position left join fetch successionCandidate.currentOwner left join fetch successionCandidate.candidate where successionCandidate.id =:id"
    )
    Optional<SuccessionCandidate> findOneWithToOneRelationships(@Param("id") Long id);
}
