package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.service.dto.PositionDTO;

/**
 * Mapper for the entity {@link Position} and its DTO {@link PositionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {}
