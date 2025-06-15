package tp.agil.backend.services;

import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.entities.*;
import tp.agil.backend.mappers.LicenciaMapper;
import tp.agil.backend.mappers.TitularMapper;
import tp.agil.backend.repositories.LicenciaRepository;
import tp.agil.backend.repositories.TitularRepository;
import tp.agil.backend.repositories.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class LicenciaServiceImpl implements LicenciaService {

    private final LicenciaRepository licenciaRepository;
    private final TitularRepository titularRepository;
    private final UsuarioRepository usuarioRepository;
    private final TitularMapper titularMapper;
    private final LicenciaMapper licenciaMapper;

    public LicenciaServiceImpl(LicenciaRepository licenciaRepository, TitularRepository titularRepository, UsuarioRepository usuarioRepository, TitularMapper titularMapper, LicenciaMapper licenciaMapper) {
        this.licenciaRepository = licenciaRepository;
        this.titularRepository = titularRepository;
        this.usuarioRepository = usuarioRepository;
        this.titularMapper = titularMapper;
        this.licenciaMapper = licenciaMapper;
    }

    @Override
    public LicenciaDTO crearLicencia(LicenciaDTO licenciaDTO) {
        TipoClase tipoClase = TipoClase.valueOf(licenciaDTO.getTipoClase());
        Titular titularLicencia = titularRepository.findByNumeroDocumento(licenciaDTO.getTitularId());
        Usuario usuarioAdm = usuarioRepository.findByNumeroDocumento(licenciaDTO.getUsuarioId());
        List<Licencia> listaLicencias = licenciaRepository.findByTitular(titularLicencia);

        // VERIFICAR LAS COMPATIBILIDADES Y LIMITES ETARIOS DEL TITULAR CON LA CLASE QUE ESTÁ SOLICITANTO Y SUS LICENCIAS YA REGISTRADAS

        if (!verificarRequisitosPorClase(titularLicencia, tipoClase)) {
            throw new IllegalArgumentException("El titular no cumple los requisitos para la clase de licencia.");
        }

    }

    @Override
    public boolean verificarRequisitosPorClase(Titular titular, TipoClase tipoClase) {
        int edad = titular.calcularEdad();
        if (tipoClase == TipoClase.A || tipoClase == TipoClase.B) {
            return edad >= 17;
        } else if (tipoClase == TipoClase.C || tipoClase == TipoClase.D || tipoClase == TipoClase.E) {
            if (edad < 21 || edad > 65) {
                return false;
            }
            List<Licencia> licencias = licenciaRepository.findByTitular(titular);
            return licencias.stream().anyMatch(licencia ->
                    licencia.getTipoClase() == TipoClase.B &&
                            licencia.getFechaEmision().isBefore(LocalDate.now().minusYears(1)) &&
                            licencia.getEstadoLicencia() == EstadoLicencia.VIGENTE
            );
        }
        return true;
    }

    @Override
    public Long generarNumeroLicencia() {
        return licenciaRepository.findAll().stream()
                .mapToLong(Licencia::getNumero)
                .max()
                .orElse(0L) + 1;
    }

    private boolean     validarDatosTitular(Titular titular) {
        return titular.getNumeroDocumento() != null &&
                titular.getTipoDocumento() != null &&
                titular.getNombre() != null &&
                titular.getApellido() != null &&
                titular.getFechaNacimiento() != null;
    }
}