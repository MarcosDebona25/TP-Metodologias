package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tp.agil.backend.dtos.LicenciaActivaDTO;
import tp.agil.backend.entities.LicenciaActiva;

@Mapper(componentModel = "spring")
public interface LicenciaActivaMapper {
    @Mapping(source = "titular.numeroDocumento", target = "documentoTitular")
    @Mapping(source = "titular.nombre", target = "nombreTitular")
    @Mapping(source = "titular.apellido", target = "apellidoTitular")
    @Mapping(source = "titular.fechaNacimiento", target = "fechaNacimientoTitular")
    @Mapping(source = "titular.domicilio", target = "domicilioTitular")
    @Mapping(source = "titular.grupoFactor", target = "grupoFactor")
    @Mapping(source = "titular.donanteOrganos", target = "donanteOrganos")
    @Mapping(source = "clases", target = "clases")
    @Mapping(source = "fechaEmision", target = "fechaEmisionLicencia")
    @Mapping(source = "fechaVencimiento", target = "fechaVencimientoLicencia")
    @Mapping(source = "observaciones", target = "observacionesLicencia")
    LicenciaActivaDTO entityToDto(LicenciaActiva licenciaActiva);

    LicenciaActiva dtoToEntity(LicenciaActivaDTO licenciaActivaDTO);
}