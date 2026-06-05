package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.PositionMatch;

/**
 * Spring Data JPA repository for the PositionMatch entity.
 */
@Repository
public interface PositionMatchRepository extends JpaRepository<PositionMatch, Long>, JpaSpecificationExecutor<PositionMatch> {
    default Optional<PositionMatch> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PositionMatch> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PositionMatch> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select positionMatch from PositionMatch positionMatch left join fetch positionMatch.person left join fetch positionMatch.position",
        countQuery = "select count(positionMatch) from PositionMatch positionMatch"
    )
    Page<PositionMatch> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select positionMatch from PositionMatch positionMatch left join fetch positionMatch.person left join fetch positionMatch.position"
    )
    List<PositionMatch> findAllWithToOneRelationships();

    @Query(
        "select positionMatch from PositionMatch positionMatch left join fetch positionMatch.person left join fetch positionMatch.position where positionMatch.id =:id"
    )
    Optional<PositionMatch> findOneWithToOneRelationships(@Param("id") Long id);
}
