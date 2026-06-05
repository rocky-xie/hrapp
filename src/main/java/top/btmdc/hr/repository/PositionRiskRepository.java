package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.PositionRisk;

/**
 * Spring Data JPA repository for the PositionRisk entity.
 */
@Repository
public interface PositionRiskRepository extends JpaRepository<PositionRisk, Long>, JpaSpecificationExecutor<PositionRisk> {
    default Optional<PositionRisk> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PositionRisk> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PositionRisk> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select positionRisk from PositionRisk positionRisk left join fetch positionRisk.position left join fetch positionRisk.category",
        countQuery = "select count(positionRisk) from PositionRisk positionRisk"
    )
    Page<PositionRisk> findAllWithToOneRelationships(Pageable pageable);

    @Query("select positionRisk from PositionRisk positionRisk left join fetch positionRisk.position left join fetch positionRisk.category")
    List<PositionRisk> findAllWithToOneRelationships();

    @Query(
        "select positionRisk from PositionRisk positionRisk left join fetch positionRisk.position left join fetch positionRisk.category where positionRisk.id =:id"
    )
    Optional<PositionRisk> findOneWithToOneRelationships(@Param("id") Long id);
}
