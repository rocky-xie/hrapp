package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.NotificationTemplate;

/**
 * Spring Data JPA repository for the NotificationTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationTemplateRepository
    extends JpaRepository<NotificationTemplate, Long>, JpaSpecificationExecutor<NotificationTemplate> {}
