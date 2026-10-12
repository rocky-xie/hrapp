package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.DispatchAssignment;

/**
 * Spring Data JPA repository for the DispatchAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DispatchAssignmentRepository
    extends JpaRepository<DispatchAssignment, Long>, JpaSpecificationExecutor<DispatchAssignment>
{
    boolean existsByApplication_Id(Long applicationId);

    java.util.Optional<DispatchAssignment> findByApplication_Id(Long applicationId);
}
