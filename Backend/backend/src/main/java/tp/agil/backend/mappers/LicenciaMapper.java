package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.entities.Licencia;

/**
 * @author Marcos Debona
 */

@Mapper(componentModel = "spring")
public interface LicenciaMapper {
    LicenciaDTO entityToDto(Licencia licencia);
    Licencia dtoToEntity(LicenciaDTO licenciaDTO);
}