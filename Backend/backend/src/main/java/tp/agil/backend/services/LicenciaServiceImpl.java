package tp.agil.backend.services;

import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.dtos.LicenciaFormDTO;
import tp.agil.backend.entities.*;
import tp.agil.backend.mappers.LicenciaMapper;
import tp.agil.backend.repositories.LicenciaRepository;
import tp.agil.backend.repositories.TitularRepository;
import tp.agil.backend.repositories.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenciaServiceImpl implements LicenciaService {

    private final LicenciaRepository licenciaRepository;
    private final TitularRepository titularRepository;
    private final LicenciaMapper licenciaMapper;
    private final UsuarioRepository usuarioRepository;

    public LicenciaServiceImpl(LicenciaRepository licenciaRepository, TitularRepository titularRepository, LicenciaMapper licenciaMapper, UsuarioRepository usuarioRepository) {
        this.licenciaRepository = licenciaRepository;
        this.titularRepository = titularRepository;
        this.licenciaMapper = licenciaMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public LicenciaDTO emitirLicencia(LicenciaFormDTO licenciaFormDTO) {
        Titular titular = titularRepository.findByNumeroDocumento(licenciaFormDTO.getTitularID());
        Usuario usuario = usuarioRepository.findByNumeroDocumento(licenciaFormDTO.getUsuarioId());
        int edad = titular.calcularEdad();
        List<Licencia> licenciasPrevias = licenciaRepository.findByTitular(titular);

        List<Clase> clases = licenciaFormDTO.getListaLicencias().stream().map(tipo -> {
            TipoClase tipoClase = TipoClase.valueOf(tipo);
            verificarRequisitosPorClase(titular, tipoClase, licenciasPrevias, edad);
            Clase clase = new Clase();
            clase.setTipo(tipoClase);
            clase.setFechaEmision(LocalDate.now());
            clase.setFechaVencimiento(calcularFechaVencimiento(titular));
            clase.setEstado(EstadoLicencia.VIGENTE);
            return clase;
        }).collect(Collectors.toList());

        Licencia licencia = new Licencia();
        licencia.setTitular(titular);
        licencia.setUsuario(usuario);
        licencia.setObservaciones(licenciaFormDTO.getObservaciones());

        licencia.setClases(clases);
        licenciaRepository.save(licencia);
        return licenciaMapper.entityToDto(licencia);
    }

    private void verificarRequisitosPorClase(Titular titular, TipoClase tipoClase, List<Licencia> licenciasPrevias, int edad) {
        if (tipoClase == TipoClase.C || tipoClase == TipoClase.D || tipoClase == TipoClase.E) {
            if (edad < 21)
                throw new IllegalArgumentException("Debe tener al menos 21 años para clase " + tipoClase);

            // Validar clase B con 1 año de antigüedad
            boolean tieneB = licenciasPrevias.stream()
                    .flatMap(l -> l.getClases().stream())
                    .anyMatch(c -> c.getTipo() == TipoClase.B &&
                            c.getFechaEmision().isBefore(LocalDate.now().minusYears(1)) &&
                            c.getEstado() == EstadoLicencia.VIGENTE);
            if (!tieneB)
                throw new IllegalArgumentException("Debe poseer clase B con al menos 1 año de antigüedad para clase " + tipoClase);

            // No emitir profesional por primera vez a mayores de 65
            boolean yaTuvoProfesional = licenciasPrevias.stream()
                    .flatMap(l -> l.getClases().stream())
                    .anyMatch(c -> c.getTipo() == tipoClase);
            if (!yaTuvoProfesional && edad > 65)
                throw new IllegalArgumentException("No se puede emitir clase profesional por primera vez a mayores de 65 años");
        } else {
            if (edad < 17)
                throw new IllegalArgumentException("Debe tener al menos 17 años para clase " + tipoClase);
        }
    }
    
    // SE TIENE QUE IMPLEMENTAR (HISTORIA DEDICADA)
    public LocalDate calcularFechaVencimiento(Titular titular){
        return LocalDate.parse("0000-00-00");
    }

}