package top.btmdc.hr.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.ActionItem;
import top.btmdc.hr.domain.enumeration.ActionSourceType;
import top.btmdc.hr.domain.enumeration.ActionStatus;

@Repository
public interface ActionItemRepository extends JpaRepository<ActionItem, Long>, JpaSpecificationExecutor<ActionItem> {
    List<ActionItem> findByStatusInOrderByPriorityAscDueDateAsc(Collection<ActionStatus> statuses);
    Page<ActionItem> findByStatus(ActionStatus status, Pageable pageable);
    long countByStatusIn(Collection<ActionStatus> statuses);
    Optional<ActionItem> findFirstBySourceTypeAndSourceIdAndStatusIn(
        ActionSourceType sourceType,
        Long sourceId,
        Collection<ActionStatus> statuses
    );
}
