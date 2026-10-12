package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.DispatchAssignment;
import top.btmdc.hr.domain.DispatchReview;
import top.btmdc.hr.service.dto.DispatchAssignmentDTO;
import top.btmdc.hr.service.dto.DispatchReviewDTO;

/**
 * Mapper for the entity {@link DispatchReview} and its DTO {@link DispatchReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface DispatchReviewMapper extends EntityMapper<DispatchReviewDTO, DispatchReview> {
    @Mapping(target = "dispatchAssignment", source = "dispatchAssignment", qualifiedByName = "dispatchAssignmentId")
    DispatchReviewDTO toDto(DispatchReview s);

    @Named("dispatchAssignmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "startDate", source = "startDate")
    DispatchAssignmentDTO toDtoDispatchAssignmentId(DispatchAssignment dispatchAssignment);
}
