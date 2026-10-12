package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.PartnerOrganization;
import top.btmdc.hr.service.dto.PartnerOrganizationDTO;

/**
 * Mapper for the entity {@link PartnerOrganization} and its DTO {@link PartnerOrganizationDTO}.
 */
@Mapper(componentModel = "spring")
public interface PartnerOrganizationMapper extends EntityMapper<PartnerOrganizationDTO, PartnerOrganization> {}
