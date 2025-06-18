package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.entities.LicenciaActiva;

@Mapper(componentModel = "spring")
public interface LicenciaActivaMapper {
    LicenciaDTO entityToDto(LicenciaActiva licenciaActiva);
    LicenciaActiva dtoToEntity(LicenciaDTO licenciaDTO);
}