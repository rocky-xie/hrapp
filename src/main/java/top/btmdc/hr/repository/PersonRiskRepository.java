package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.PersonRisk;

/**
 * Spring Data JPA repository for the PersonRisk entity.
 */
@Repository
public interface PersonRiskRepository extends JpaRepository<PersonRisk, Long>, JpaSpecificationExecutor<PersonRisk> {
    default Optional<PersonRisk> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PersonRisk> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PersonRisk> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select personRisk from PersonRisk personRisk left join fetch personRisk.person left join fetch personRisk.position",
        countQuery = "select count(personRisk) from PersonRisk personRisk"
    )
    Page<PersonRisk> findAllWithToOneRelationships(Pageable pageable);

    @Query("select personRisk from PersonRisk personRisk left join fetch personRisk.person left join fetch personRisk.position")
    List<PersonRisk> findAllWithToOneRelationships();

    @Query(
        "select personRisk from PersonRisk personRisk left join fetch personRisk.person left join fetch personRisk.position where personRisk.id =:id"
    )
    Optional<PersonRisk> findOneWithToOneRelationships(@Param("id") Long id);
}
