package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.Position;

/**
 * Spring Data JPA repository for the Position entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PositionRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {}
