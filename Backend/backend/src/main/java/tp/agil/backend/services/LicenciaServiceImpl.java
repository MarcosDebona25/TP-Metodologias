package tp.agil.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.agil.backend.entities.*;
import tp.agil.backend.repositories.LicenciaRepository;
import tp.agil.backend.repositories.TitularRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class LicenciaServiceImpl implements LicenciaService {

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private TitularRepository titularRepository;

    @Override
    public Licencia crearLicencia(Titular titular, TipoClase tipoClase) {
        if (!validarDatosTitular(titular)) {
            throw new IllegalArgumentException("Datos del titular inválidos.");
        }
        if (!verificarRequisitosPorClase(titular, tipoClase)) {
            throw new IllegalArgumentException("El titular no cumple los requisitos para la clase de licencia.");
        }

        Licencia nuevaLicencia = new Licencia();
        nuevaLicencia.setNumero(generarNumeroLicencia());
        nuevaLicencia.setTipoClase(tipoClase);
        nuevaLicencia.setFechaEmision(LocalDate.now());
        nuevaLicencia.setFechaVencimiento(LocalDate.now().plusYears(5));
        nuevaLicencia.setEstadoLicencia(EstadoLicencia.VIGENTE);
        nuevaLicencia.setTitular(titular);

        titularRepository.save(titular);
        licenciaRepository.save(nuevaLicencia);

        return nuevaLicencia;
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

    private boolean validarDatosTitular(Titular titular) {
        return titular.getNumeroDocumento() != null &&
                titular.getTipoDocumento() != null &&
                titular.getNombre() != null &&
                titular.getApellido() != null &&
                titular.getFechaNacimiento() != null;
    }
}