package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.StaffSubstitution;

/**
 * Spring Data JPA repository for the StaffSubstitution entity.
 */
@Repository
public interface StaffSubstitutionRepository extends JpaRepository<StaffSubstitution, Long>, JpaSpecificationExecutor<StaffSubstitution> {
    default Optional<StaffSubstitution> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StaffSubstitution> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StaffSubstitution> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select staffSubstitution from StaffSubstitution staffSubstitution left join fetch staffSubstitution.position left join fetch staffSubstitution.candidatePerson",
        countQuery = "select count(staffSubstitution) from StaffSubstitution staffSubstitution"
    )
    Page<StaffSubstitution> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select staffSubstitution from StaffSubstitution staffSubstitution left join fetch staffSubstitution.position left join fetch staffSubstitution.candidatePerson"
    )
    List<StaffSubstitution> findAllWithToOneRelationships();

    @Query(
        "select staffSubstitution from StaffSubstitution staffSubstitution left join fetch staffSubstitution.position left join fetch staffSubstitution.candidatePerson where staffSubstitution.id =:id"
    )
    Optional<StaffSubstitution> findOneWithToOneRelationships(@Param("id") Long id);

    boolean existsByPositionIdAndSubstitutableTrue(Long positionId);

    long countByPositionIdAndSubstitutableTrue(Long positionId);

    Optional<StaffSubstitution> findOneByPositionIdAndCandidatePersonId(Long positionId, Long candidatePersonId);

    @Query(
        "select staffSubstitution from StaffSubstitution staffSubstitution left join fetch staffSubstitution.position left join fetch staffSubstitution.candidatePerson where staffSubstitution.position.id =:positionId"
    )
    List<StaffSubstitution> findByPositionIdWithPerson(@Param("positionId") Long positionId);
}
