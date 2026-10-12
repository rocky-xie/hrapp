package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.SelectionActivity;

/**
 * Spring Data JPA repository for the SelectionActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SelectionActivityRepository extends JpaRepository<SelectionActivity, Long>, JpaSpecificationExecutor<SelectionActivity> {}
