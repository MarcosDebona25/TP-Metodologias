package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.entities.Clase;
import tp.agil.backend.entities.Licencia;
import tp.agil.backend.entities.TipoClase;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LicenciaMapper {

    @Mapping(source = "clases", target = "clases", qualifiedByName = "clasesToStrings")
    LicenciaDTO entityToDto(Licencia licencia);

    @Mapping(source = "clases", target = "clases", qualifiedByName = "stringsToClases")
    Licencia dtoToEntity(LicenciaDTO licenciaDTO);

    @Named("clasesToStrings")
    default List<String> clasesToStrings(List<Clase> clases) {
        if (clases == null) return null;
        return clases.stream()
                .map(clase -> clase.getTipo().name())
                .collect(Collectors.toList());
    }

    @Named("stringsToClases")
    default List<Clase> stringsToClases(List<String> clases) {
        if (clases == null) return null;
        return clases.stream()
                .map(tipo -> {
                    Clase clase = new Clase();
                    clase.setTipo(TipoClase.valueOf(tipo));
                    return clase;
                })
                .collect(Collectors.toList());
    }
}