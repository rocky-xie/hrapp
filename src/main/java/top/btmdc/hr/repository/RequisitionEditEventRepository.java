package top.btmdc.hr.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.RequisitionEditEvent;

@Repository
public interface RequisitionEditEventRepository extends JpaRepository<RequisitionEditEvent, Long> {
    List<RequisitionEditEvent> findAllByRequisition_IdOrderByOccurredAtDesc(Long requisitionId);
}
