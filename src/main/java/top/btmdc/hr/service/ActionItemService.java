package top.btmdc.hr.service;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.ActionItem;
import top.btmdc.hr.domain.enumeration.ActionPriority;
import top.btmdc.hr.domain.enumeration.ActionSourceType;
import top.btmdc.hr.domain.enumeration.ActionStatus;
import top.btmdc.hr.repository.ActionItemRepository;
import top.btmdc.hr.service.dto.ActionItemDTO;
import top.btmdc.hr.service.mapper.ActionItemMapper;

@Service
@Transactional
public class ActionItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ActionItemService.class);

    private final ActionItemRepository actionItemRepository;
    private final ActionItemMapper actionItemMapper;

    public ActionItemService(ActionItemRepository actionItemRepository, ActionItemMapper actionItemMapper) {
        this.actionItemRepository = actionItemRepository;
        this.actionItemMapper = actionItemMapper;
    }

    public ActionItemDTO save(ActionItemDTO dto) {
        LOG.debug("Request to save ActionItem : {}", dto);
        ActionItem entity = actionItemMapper.toEntity(dto);
        applyCreateDefaults(entity);
        entity = actionItemRepository.save(entity);
        return actionItemMapper.toDto(entity);
    }

    public ActionItemDTO update(ActionItemDTO dto) {
        LOG.debug("Request to update ActionItem : {}", dto);
        ActionItem existing = actionItemRepository.findById(dto.getId()).orElseThrow();
        ActionStatus status = existing.getStatus();
        LocalDate createdAt = existing.getCreatedAt();
        LocalDate completedAt = existing.getCompletedAt();
        actionItemMapper.partialUpdate(existing, dto);
        existing.setStatus(status);
        existing.setCreatedAt(createdAt);
        existing.setCompletedAt(completedAt);
        if (existing.getPriority() == null) {
            existing.setPriority(ActionPriority.P2_MEDIUM);
        }
        existing = actionItemRepository.save(existing);
        return actionItemMapper.toDto(existing);
    }

    public Optional<ActionItemDTO> partialUpdate(ActionItemDTO dto) {
        return actionItemRepository
            .findById(dto.getId())
            .map(existing -> {
                ActionStatus status = existing.getStatus();
                LocalDate createdAt = existing.getCreatedAt();
                LocalDate completedAt = existing.getCompletedAt();
                actionItemMapper.partialUpdate(existing, dto);
                existing.setStatus(status);
                existing.setCreatedAt(createdAt);
                existing.setCompletedAt(completedAt);
                if (existing.getPriority() == null) {
                    existing.setPriority(ActionPriority.P2_MEDIUM);
                }
                return existing;
            })
            .map(actionItemRepository::save)
            .map(actionItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ActionItemDTO> findAll(Pageable pageable) {
        return actionItemRepository.findAll(pageable).map(actionItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<ActionItemDTO> findOne(Long id) {
        return actionItemRepository.findById(id).map(actionItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<ActionItemDTO> findOpenItems() {
        return actionItemRepository
            .findByStatusInOrderByPriorityAscDueDateAsc(openStatuses())
            .stream()
            .map(actionItemMapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public long countOpen() {
        return actionItemRepository.countByStatusIn(openStatuses());
    }

    public Optional<ActionItemDTO> start(Long id) {
        return actionItemRepository
            .findById(id)
            .map(item -> {
                if (item.getStatus() != ActionStatus.OPEN) {
                    throw new InvalidActionItemTransitionException(
                        "Cannot start actionItem with status " + item.getStatus() + "; only OPEN can be started"
                    );
                }
                item.setStatus(ActionStatus.IN_PROGRESS);
                item.setCompletedAt(null);
                return item;
            })
            .map(actionItemRepository::save)
            .map(actionItemMapper::toDto);
    }

    public Optional<ActionItemDTO> complete(Long id) {
        return actionItemRepository
            .findById(id)
            .map(item -> {
                if (item.getStatus() != ActionStatus.OPEN && item.getStatus() != ActionStatus.IN_PROGRESS) {
                    throw new InvalidActionItemTransitionException(
                        "Cannot complete actionItem with status " + item.getStatus() + "; only OPEN or IN_PROGRESS can be completed"
                    );
                }
                item.setStatus(ActionStatus.COMPLETED);
                item.setCompletedAt(LocalDate.now());
                return item;
            })
            .map(actionItemRepository::save)
            .map(actionItemMapper::toDto);
    }

    public Optional<ActionItemDTO> cancel(Long id) {
        return actionItemRepository
            .findById(id)
            .map(item -> {
                if (item.getStatus() != ActionStatus.OPEN && item.getStatus() != ActionStatus.IN_PROGRESS) {
                    throw new InvalidActionItemTransitionException(
                        "Cannot cancel actionItem with status " + item.getStatus() + "; only OPEN or IN_PROGRESS can be cancelled"
                    );
                }
                item.setStatus(ActionStatus.CANCELLED);
                item.setCompletedAt(null);
                return item;
            })
            .map(actionItemRepository::save)
            .map(actionItemMapper::toDto);
    }

    public ActionItemDTO createFromSource(ActionSourceType sourceType, String description, String assignee, ActionPriority priority) {
        ActionItem entity = new ActionItem()
            .description(description)
            .assignee(assignee)
            .sourceType(sourceType)
            .priority(priority != null ? priority : ActionPriority.P2_MEDIUM)
            .status(ActionStatus.OPEN)
            .createdAt(LocalDate.now());
        return actionItemMapper.toDto(actionItemRepository.save(entity));
    }

    public void delete(Long id) {
        LOG.debug("Request to delete ActionItem : {}", id);
        actionItemRepository.deleteById(id);
    }

    private void applyCreateDefaults(ActionItem entity) {
        if (entity.getStatus() == null) {
            entity.setStatus(ActionStatus.OPEN);
        }
        if (entity.getPriority() == null) {
            entity.setPriority(ActionPriority.P2_MEDIUM);
        }
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDate.now());
        }
        if (entity.getStatus() != ActionStatus.COMPLETED) {
            entity.setCompletedAt(null);
        }
        if (entity.getStatus() == ActionStatus.COMPLETED && entity.getCompletedAt() == null) {
            entity.setCompletedAt(LocalDate.now());
        }
    }

    private EnumSet<ActionStatus> openStatuses() {
        return EnumSet.of(ActionStatus.OPEN, ActionStatus.IN_PROGRESS);
    }
}
