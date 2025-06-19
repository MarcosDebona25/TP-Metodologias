package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import tp.agil.backend.dtos.LicenciaActivaDTO;
import tp.agil.backend.entities.LicenciaActiva;

@Mapper(componentModel = "spring")
public interface LicenciaActivaMapper {
    LicenciaActivaDTO entityToDto(LicenciaActiva licenciaActiva);
    LicenciaActiva dtoToEntity(LicenciaActivaDTO licenciaActivaDTO);
}