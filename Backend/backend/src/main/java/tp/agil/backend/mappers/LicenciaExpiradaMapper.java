package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.entities.LicenciaExpirada;

@Mapper(componentModel = "spring")
public interface LicenciaExpiradaMapper {
    LicenciaDTO entityToDto(LicenciaExpirada licenciaActiva);
    LicenciaExpirada dtoToEntity(LicenciaDTO licenciaDTO);
}