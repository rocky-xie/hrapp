package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.TrustObservation;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.TrustObservationDTO;

/**
 * Mapper for the entity {@link TrustObservation} and its DTO {@link TrustObservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrustObservationMapper extends EntityMapper<TrustObservationDTO, TrustObservation> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "observer", source = "observer", qualifiedByName = "personPersonName")
    TrustObservationDTO toDto(TrustObservation s);

    @Named("personPersonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personName", source = "personName")
    PersonDTO toDtoPersonPersonName(Person person);
}
