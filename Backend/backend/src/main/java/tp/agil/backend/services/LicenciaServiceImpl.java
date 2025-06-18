package tp.agil.backend.services;

import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.ComprobanteDTO;
import tp.agil.backend.dtos.LicenciaActivaDTO;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.dtos.LicenciaFormDTO;
import tp.agil.backend.entities.LicenciaActiva;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.entities.Usuario;
import tp.agil.backend.exceptions.LicenciaNoEncontradaException;
import tp.agil.backend.mappers.LicenciaActivaMapper;
import tp.agil.backend.repositories.LicenciaActivaRepository;
import tp.agil.backend.repositories.TitularRepository;
import tp.agil.backend.repositories.UsuarioRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class LicenciaServiceImpl implements LicenciaService {

    private final LicenciaActivaRepository licenciaActivaRepository;
    private final TitularRepository titularRepository;
    private final UsuarioRepository usuarioRepository;
    private final LicenciaActivaMapper licenciaActivaMapper;

    public LicenciaServiceImpl(
            LicenciaActivaRepository licenciaActivaRepository,
            TitularRepository titularRepository,
            UsuarioRepository usuarioRepository,
            LicenciaActivaMapper licenciaActivaMapper
    ) {
        this.licenciaActivaRepository = licenciaActivaRepository;
        this.titularRepository = titularRepository;
        this.usuarioRepository = usuarioRepository;
        this.licenciaActivaMapper = licenciaActivaMapper;
    }

    @Override
    public LicenciaDTO emitirLicencia(LicenciaFormDTO licenciaFormDTO) {
        Titular titular = titularRepository.findByNumeroDocumento(licenciaFormDTO.getDocumentoTitular());
        Usuario usuario = usuarioRepository.findByNumeroDocumento("11999888"); // getDocumentoUsuario() SERÁ CUANDO TENGAMOS LOGIN

        LocalDate fechaEmision = LocalDate.now();
        LocalDate fechaVencimiento = calcularFechaVencimiento(titular);

        LicenciaActiva licenciaActiva = new LicenciaActiva();
        licenciaActiva.setTitular(titular);
        licenciaActiva.setUsuario(usuario);
        licenciaActiva.setObservaciones(licenciaFormDTO.getObservaciones());
        licenciaActiva.setClases(licenciaFormDTO.getClases());
        licenciaActiva.setFechaEmision(fechaEmision);
        licenciaActiva.setFechaVencimiento(fechaVencimiento);

        licenciaActivaRepository.save(licenciaActiva);

        LicenciaDTO dto = licenciaActivaMapper.entityToDto(licenciaActiva);
        dto.setDocumentoTitular(titular.getNumeroDocumento());
        dto.setDocumentoUsuario(usuario.getNumeroDocumento());
        return dto;
    }

    private LocalDate calcularFechaVencimiento(Titular titular) {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = titular.getFechaNacimiento();
        int edad = titular.calcularEdad();

        int vigencia;
        if (edad < 21) {
            vigencia = 1;
        } else if (edad <= 46) {
            vigencia = 5;
        } else if (edad <= 60) {
            vigencia = 4;
        } else if (edad <= 70) {
            vigencia = 3;
        } else {
            vigencia = 1;
        }

        LocalDate proximoCumple = nacimiento.withYear(hoy.getYear());
        if (!proximoCumple.isAfter(hoy)) {
            proximoCumple = proximoCumple.plusYears(1);
        }

        long diasHastaCumple = ChronoUnit.DAYS.between(hoy, proximoCumple);

        if (diasHastaCumple > 31) {
            vigencia--;
        }

        return proximoCumple.plusYears(vigencia);
    }

    @Override
    public LicenciaActivaDTO buscarLicenciaActivaPorDni(String numeroDocumento) {
        LicenciaActiva licencia = licenciaActivaRepository.findByTitular_NumeroDocumento(numeroDocumento);
        if (licencia == null) {
            throw new LicenciaNoEncontradaException("No se encontró una licencia activa para el DNI: " + numeroDocumento);
        }
        Titular titular = licencia.getTitular();
        LicenciaActivaDTO dto = new LicenciaActivaDTO();
        dto.setDocumentoTitular(titular.getNumeroDocumento());
        dto.setNombreTitular(titular.getNombre());
        dto.setApellidoTitular(titular.getApellido());
        dto.setClases(licencia.getClases());
        dto.setDomicilioTitular(titular.getDomicilio());
        dto.setFechaEmisionLicencia(licencia.getFechaEmision());
        dto.setFechaVencimientoLicencia(licencia.getFechaVencimiento());
        dto.setGrupoFactor(titular.getGrupoFactor());
        dto.setDonanteOrganos(titular.isDonanteOrganos() ? "Si" : "No");
        dto.setObservacionesLicencia(licencia.getObservaciones());
        return dto;
    }

    @Override
    public ComprobanteDTO devolverComprobanteLicenciaPorDni(String numeroDocumento) {
        LicenciaActiva licencia = licenciaActivaRepository.findByTitular_NumeroDocumento(numeroDocumento);
        if (licencia == null) {
            throw new LicenciaNoEncontradaException("No se encontró una licencia activa para el DNI: " + numeroDocumento);
        }
        Titular titular = licencia.getTitular();
        ComprobanteDTO dto = new ComprobanteDTO();
        dto.setNombreTitular(titular.getNombre());
        dto.setApellidoTitular(titular.getApellido());
        dto.setClases(licencia.getClases());
        dto.setCostosEmision(1000.0);
        dto.setCostosAdministrativos(500.0);
        return dto;
    }
}