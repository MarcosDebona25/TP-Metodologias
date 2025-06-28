// Backend/backend/src/main/java/tp/agil/backend/mappers/LicenciaExpiradaMapper.java
package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tp.agil.backend.dtos.LicenciaExpiradaDTO;
import tp.agil.backend.entities.LicenciaExpirada;

@Mapper(componentModel = "spring")
public interface LicenciaExpiradaMapper {
//    @Mapping(source = "titular.numeroDocumento", target = "documentoTitular")
//    @Mapping(source = "titular.nombre", target = "nombreTitular")
//    @Mapping(source = "titular.apellido", target = "apellidoTitular")
    LicenciaExpiradaDTO entityToDto(LicenciaExpirada licenciaExpirada);

    LicenciaExpirada dtoToEntity(LicenciaExpiradaDTO licenciaExpiradaDTO);
}