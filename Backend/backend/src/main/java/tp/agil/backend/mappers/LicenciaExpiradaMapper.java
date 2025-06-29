package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tp.agil.backend.dtos.LicenciaExpiradaDTO;
import tp.agil.backend.entities.LicenciaExpirada;

@Mapper(componentModel = "spring")
public interface LicenciaExpiradaMapper {
    LicenciaExpiradaDTO entityToDto(LicenciaExpirada licenciaExpirada);
    LicenciaExpirada dtoToEntity(LicenciaExpiradaDTO licenciaExpiradaDTO);
}