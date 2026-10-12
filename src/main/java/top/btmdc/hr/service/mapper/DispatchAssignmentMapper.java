package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.DispatchAssignment;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.service.dto.DispatchAssignmentDTO;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;

/**
 * Mapper for the entity {@link DispatchAssignment} and its DTO {@link DispatchAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DispatchAssignmentMapper extends EntityMapper<DispatchAssignmentDTO, DispatchAssignment> {
    @Mapping(target = "application", source = "application", qualifiedByName = "recruitmentApplicationId")
    DispatchAssignmentDTO toDto(DispatchAssignment s);

    @Named("recruitmentApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationCode", source = "applicationCode")
    RecruitmentApplicationDTO toDtoRecruitmentApplicationId(RecruitmentApplication recruitmentApplication);
}
