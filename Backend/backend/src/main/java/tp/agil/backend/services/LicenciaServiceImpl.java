package tp.agil.backend.services;

import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.ComprobanteDTO;
import tp.agil.backend.dtos.LicenciaActivaDTO;
import tp.agil.backend.dtos.LicenciaEmitidaDTO;
import tp.agil.backend.dtos.LicenciaFormDTO;
import tp.agil.backend.entities.LicenciaActiva;
import tp.agil.backend.entities.LicenciaExpirada;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.entities.Usuario;
import tp.agil.backend.exceptions.LicenciaNoEncontradaException;
import tp.agil.backend.mappers.LicenciaActivaMapper;
import tp.agil.backend.mappers.LicenciaEmitidaMapper;
import tp.agil.backend.repositories.LicenciaActivaRepository;
import tp.agil.backend.repositories.LicenciaVencidaRepository;
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
    private final LicenciaEmitidaMapper licenciaEmitidaMapper;
    private final LicenciaVencidaRepository licenciaVencidaRepository;

    private static final double GASTOS_ADMINISTRATIVOS = 8.0;

    public LicenciaServiceImpl(
            LicenciaActivaRepository licenciaActivaRepository,
            TitularRepository titularRepository,
            UsuarioRepository usuarioRepository,
            LicenciaActivaMapper licenciaActivaMapper,
            LicenciaEmitidaMapper licenciaEmitidaMapper, LicenciaVencidaRepository licenciaVencidaRepository
    ) {
        this.licenciaActivaRepository = licenciaActivaRepository;
        this.titularRepository = titularRepository;
        this.usuarioRepository = usuarioRepository;
        this.licenciaActivaMapper = licenciaActivaMapper;
        this.licenciaEmitidaMapper = licenciaEmitidaMapper;
        this.licenciaVencidaRepository = licenciaVencidaRepository;
    }

    @Override
    public LicenciaEmitidaDTO emitirLicencia(LicenciaFormDTO licenciaFormDTO) {
        String documentoUsuario = "11999888";
        Titular titular = titularRepository.findByNumeroDocumento(licenciaFormDTO.getDocumentoTitular());
        Usuario usuario = usuarioRepository.findByNumeroDocumento(documentoUsuario);
        LocalDate fechaEmision = LocalDate.now();
        LocalDate fechaVencimiento = calcularFechaVencimiento(titular);

        LicenciaActiva licenciaAnterior = titular.getLicenciaActiva();
        if (licenciaAnterior != null) {
            titular.setLicenciaActiva(null);
            titularRepository.save(titular);

            LicenciaExpirada expirada = new LicenciaExpirada();
            expirada.setTitular(titular);
            expirada.setUsuario(licenciaAnterior.getUsuario());
            expirada.setObservaciones(licenciaAnterior.getObservaciones());
            expirada.setClases(licenciaAnterior.getClases());
            expirada.setFechaEmision(licenciaAnterior.getFechaEmision());
            expirada.setFechaVencimiento(licenciaAnterior.getFechaVencimiento());
            licenciaVencidaRepository.save(expirada);

            licenciaActivaRepository.delete(licenciaAnterior);
        }

        LicenciaActiva nuevaLicencia = new LicenciaActiva();
        nuevaLicencia.setTitular(titular);
        nuevaLicencia.setUsuario(usuario);
        nuevaLicencia.setObservaciones(licenciaFormDTO.getObservaciones());
        nuevaLicencia.setClases(licenciaFormDTO.getClases());
        nuevaLicencia.setFechaEmision(fechaEmision);
        nuevaLicencia.setFechaVencimiento(fechaVencimiento);

        licenciaActivaRepository.save(nuevaLicencia);

        titular.setLicenciaActiva(nuevaLicencia);
        titularRepository.save(titular);

        LicenciaEmitidaDTO dto = licenciaEmitidaMapper.entityToDto(nuevaLicencia);
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
        dto.setFechaNacimientoTitular(titular.getFechaNacimiento());
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
        dto.setCostosEmision(calcularCostoEmision(licencia.getClases(), titular.calcularEdad()));
        dto.setCostosAdministrativos(GASTOS_ADMINISTRATIVOS);
        return dto;
    }

    private double calcularCostoEmision(String clases, int edad) {
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

        double total = 0.0;
        for (String clase : clases.trim().split("\\s+")) {
            total += costoPorClaseYVigencia(clase, vigencia);
        }
        return total;
    }

    private double costoPorClaseYVigencia(String clase, int vigencia) {
        switch (clase) {
            case "A":
            case "B":
            case "G":
            case "F":
                switch (vigencia) {
                    case 5: return 40.0;
                    case 4: return 30.0;
                    case 3: return 25.0;
                    case 1: return 20.0;
                }
                break;
            case "C":
                switch (vigencia) {
                    case 5: return 47.0;
                    case 4: return 35.0;
                    case 3: return 30.0;
                    case 1: return 23.0;
                }
                break;
            case "D":
                switch (vigencia) {
                    case 5: return 50.0;
                    case 4: return 40.0;
                    case 3: return 35.0;
                    case 1: return 28.0;
                }
                break;
            case "E":
                switch (vigencia) {
                    case 5: return 59.0;
                    case 4: return 44.0;
                    case 3: return 39.0;
                    case 1: return 29.0;
                }
                break;
        }
        throw new IllegalArgumentException("Clase o vigencia no válida");
    }
}