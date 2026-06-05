package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.TrustObservation;

/**
 * Spring Data JPA repository for the TrustObservation entity.
 */
@Repository
public interface TrustObservationRepository extends JpaRepository<TrustObservation, Long>, JpaSpecificationExecutor<TrustObservation> {
    default Optional<TrustObservation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TrustObservation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TrustObservation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select trustObservation from TrustObservation trustObservation left join fetch trustObservation.person left join fetch trustObservation.observer",
        countQuery = "select count(trustObservation) from TrustObservation trustObservation"
    )
    Page<TrustObservation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select trustObservation from TrustObservation trustObservation left join fetch trustObservation.person left join fetch trustObservation.observer"
    )
    List<TrustObservation> findAllWithToOneRelationships();

    @Query(
        "select trustObservation from TrustObservation trustObservation left join fetch trustObservation.person left join fetch trustObservation.observer where trustObservation.id =:id"
    )
    Optional<TrustObservation> findOneWithToOneRelationships(@Param("id") Long id);
}
