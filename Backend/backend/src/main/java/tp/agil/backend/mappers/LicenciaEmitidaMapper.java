package tp.agil.backend.mappers;

import tp.agil.backend.dtos.LicenciaEmitidaDTO;
import tp.agil.backend.entities.LicenciaActiva;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LicenciaEmitidaMapper {
    LicenciaEmitidaDTO entityToDto(LicenciaActiva licenciaActiva);
    LicenciaActiva dtoToEntity(LicenciaEmitidaDTO licenciaEmitidaDTO);
}
