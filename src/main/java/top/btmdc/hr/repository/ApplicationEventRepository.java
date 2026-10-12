package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.ApplicationEvent;

/**
 * Spring Data JPA repository for the ApplicationEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationEventRepository extends JpaRepository<ApplicationEvent, Long>, JpaSpecificationExecutor<ApplicationEvent> {}
