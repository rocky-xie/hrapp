package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.NotificationMessage;

/**
 * Spring Data JPA repository for the NotificationMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationMessageRepository
    extends JpaRepository<NotificationMessage, Long>, JpaSpecificationExecutor<NotificationMessage> {}
