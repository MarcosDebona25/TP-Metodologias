package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import tp.agil.backend.dtos.LicenciaEmitidaDTO;
import tp.agil.backend.entities.LicenciaExpirada;

@Mapper(componentModel = "spring")
public interface LicenciaExpiradaMapper {
    LicenciaEmitidaDTO entityToDto(LicenciaExpirada licenciaActiva);
    LicenciaExpirada dtoToEntity(LicenciaEmitidaDTO licenciaEmitidaDTO);
}