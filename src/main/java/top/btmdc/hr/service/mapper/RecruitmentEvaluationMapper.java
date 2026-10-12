package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.RecruitmentEvaluation;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;
import top.btmdc.hr.service.dto.RecruitmentEvaluationDTO;

/**
 * Mapper for the entity {@link RecruitmentEvaluation} and its DTO {@link RecruitmentEvaluationDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecruitmentEvaluationMapper extends EntityMapper<RecruitmentEvaluationDTO, RecruitmentEvaluation> {
    @Mapping(target = "application", source = "application", qualifiedByName = "recruitmentApplicationId")
    RecruitmentEvaluationDTO toDto(RecruitmentEvaluation s);

    @Named("recruitmentApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationCode", source = "applicationCode")
    RecruitmentApplicationDTO toDtoRecruitmentApplicationId(RecruitmentApplication recruitmentApplication);
}
