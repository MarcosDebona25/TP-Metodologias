package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tp.agil.backend.dtos.LicenciaExpiradaDTO;
import tp.agil.backend.entities.LicenciaExpirada;

@Mapper(componentModel = "spring")
public interface LicenciaExpiradaMapper {
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
    LicenciaExpiradaDTO entityToDto(LicenciaExpirada licenciaExpirada);
    LicenciaExpirada dtoToEntity(LicenciaExpiradaDTO licenciaExpiradaDTO);
}