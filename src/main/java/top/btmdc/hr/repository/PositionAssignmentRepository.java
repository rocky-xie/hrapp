package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.PositionAssignment;

/**
 * Spring Data JPA repository for the PositionAssignment entity.
 */
@Repository
public interface PositionAssignmentRepository
    extends JpaRepository<PositionAssignment, Long>, JpaSpecificationExecutor<PositionAssignment>
{
    default Optional<PositionAssignment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PositionAssignment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PositionAssignment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select positionAssignment from PositionAssignment positionAssignment left join fetch positionAssignment.person left join fetch positionAssignment.position",
        countQuery = "select count(positionAssignment) from PositionAssignment positionAssignment"
    )
    Page<PositionAssignment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select positionAssignment from PositionAssignment positionAssignment left join fetch positionAssignment.person left join fetch positionAssignment.position"
    )
    List<PositionAssignment> findAllWithToOneRelationships();

    @Query(
        "select positionAssignment from PositionAssignment positionAssignment left join fetch positionAssignment.person left join fetch positionAssignment.position where positionAssignment.id =:id"
    )
    Optional<PositionAssignment> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select positionAssignment from PositionAssignment positionAssignment left join fetch positionAssignment.person where positionAssignment.position.id =:positionId and positionAssignment.active = true"
    )
    List<PositionAssignment> findActiveByPositionIdWithPerson(@Param("positionId") Long positionId);
}
