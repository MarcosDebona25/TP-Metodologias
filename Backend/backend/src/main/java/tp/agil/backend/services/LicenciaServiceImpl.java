package tp.agil.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.*;
import tp.agil.backend.entities.LicenciaActiva;
import tp.agil.backend.entities.LicenciaExpirada;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.entities.Usuario;
import tp.agil.backend.exceptions.LicenciaNoEncontradaException;
import tp.agil.backend.exceptions.TitularNoEncontradoException;
import tp.agil.backend.exceptions.UsuarioNoEncontradoException;
import tp.agil.backend.mappers.LicenciaActivaMapper;
import tp.agil.backend.mappers.LicenciaEmitidaMapper;
import tp.agil.backend.mappers.LicenciaExpiradaMapper;
import tp.agil.backend.repositories.LicenciaActivaRepository;
import tp.agil.backend.repositories.LicenciaExpiradaRepository;
import tp.agil.backend.repositories.TitularRepository;
import tp.agil.backend.repositories.UsuarioRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenciaServiceImpl implements LicenciaService {

    private final LicenciaActivaRepository licenciaActivaRepository;
    private final LicenciaExpiradaRepository licenciaExpiradaRepository;
    private final TitularRepository titularRepository;
    private final UsuarioRepository usuarioRepository;
    private final LicenciaActivaMapper licenciaActivaMapper;
    private final LicenciaEmitidaMapper licenciaEmitidaMapper;
    private final LicenciaExpiradaMapper licenciaExpiradaMapper;

    private static final double GASTOS_ADMINISTRATIVOS = 8.0;

    public LicenciaServiceImpl(LicenciaActivaRepository licenciaActivaRepository, LicenciaExpiradaRepository licenciaExpiradaRepository, TitularRepository titularRepository, UsuarioRepository usuarioRepository, LicenciaActivaMapper licenciaActivaMapper, LicenciaEmitidaMapper licenciaEmitidaMapper, LicenciaExpiradaMapper licenciaExpiradaMapper) {
        this.licenciaActivaRepository = licenciaActivaRepository;
        this.licenciaExpiradaRepository = licenciaExpiradaRepository;
        this.titularRepository = titularRepository;
        this.usuarioRepository = usuarioRepository;
        this.licenciaActivaMapper = licenciaActivaMapper;
        this.licenciaEmitidaMapper = licenciaEmitidaMapper;
        this.licenciaExpiradaMapper = licenciaExpiradaMapper;
    }

    @Transactional
    @Override
    public LicenciaEmitidaDTO emitirLicencia(LicenciaFormDTO licenciaFormDTO) {
        validarFormularioLicencia(licenciaFormDTO);
        String documentoUsuario = "11999888";

        Titular titular = titularRepository.findByNumeroDocumento(licenciaFormDTO.getDocumentoTitular());
        if (titular == null) throw new TitularNoEncontradoException("No se encontró un titular con el número de documento: " + licenciaFormDTO.getDocumentoTitular());

        Usuario usuario = usuarioRepository.findByNumeroDocumento(documentoUsuario);
        LocalDate fechaEmision = LocalDate.now();
        LocalDate fechaVencimiento = calcularFechaVencimiento(titular);

        LicenciaActiva licenciaAnterior = titular.getLicenciaActiva();
        if (licenciaAnterior != null) {
            LicenciaExpirada expirada = new LicenciaExpirada();
            expirada.setTitular(titular);
            expirada.setUsuario(licenciaAnterior.getUsuario());
            expirada.setObservaciones(licenciaAnterior.getObservaciones());
            expirada.setClases(licenciaAnterior.getClases());
            expirada.setFechaEmision(licenciaAnterior.getFechaEmision());
            expirada.setFechaVencimiento(licenciaAnterior.getFechaVencimiento());

            titular.setLicenciaActiva(null);
            licenciaAnterior.setTitular(null);

            licenciaExpiradaRepository.save(expirada);

            licenciaActivaRepository.delete(licenciaAnterior);

            licenciaActivaRepository.flush();
        }

        LicenciaActiva nuevaLicencia = new LicenciaActiva();
        nuevaLicencia.setTitular(titular);
        nuevaLicencia.setUsuario(usuario);
        nuevaLicencia.setObservaciones(licenciaFormDTO.getObservaciones());
        nuevaLicencia.setClases(licenciaFormDTO.getClases());
        nuevaLicencia.setFechaEmision(fechaEmision);
        nuevaLicencia.setFechaVencimiento(fechaVencimiento);

        titular.setLicenciaActiva(nuevaLicencia);

        licenciaActivaRepository.save(nuevaLicencia);
        titularRepository.save(titular);

        LicenciaEmitidaDTO dto = licenciaEmitidaMapper.entityToDto(nuevaLicencia);
        dto.setDocumentoTitular(titular.getNumeroDocumento());
        dto.setDocumentoUsuario(usuario.getNumeroDocumento());
        return dto;
    }

    @Override
    public LicenciaActivaDTO buscarLicenciaActivaPorDni(String numeroDocumento) {
        LicenciaActiva licencia = licenciaActivaRepository.findByTitular_NumeroDocumento(numeroDocumento);
        if (licencia == null) throw new LicenciaNoEncontradaException("No se encontró una licencia activa para el DNI: " + numeroDocumento);

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
        if (licencia == null)
            throw new LicenciaNoEncontradaException("No se encontró una licencia activa para el DNI: " + numeroDocumento);

        Titular titular = licencia.getTitular();
        ComprobanteDTO dto = new ComprobanteDTO();
        dto.setNombreTitular(titular.getNombre());
        dto.setApellidoTitular(titular.getApellido());
        dto.setClases(licencia.getClases());
        dto.setCostosEmision(calcularCostoEmision(licencia.getClases(), titular));
        dto.setFechaEmisionComprobante(licencia.getFechaEmision());
        dto.setCostosAdministrativos(GASTOS_ADMINISTRATIVOS);
        return dto;
    }

    @Transactional
    @Override
    public LicenciaEmitidaDTO renovarLicencia(LicenciaFormDTO licenciaFormDTO, String motivo) {
        if (!"vencimiento".equalsIgnoreCase(motivo) && !"modificacion".equalsIgnoreCase(motivo)) {
            throw new IllegalArgumentException("El motivo debe ser 'vencimiento' o 'modificacion'.");
        }
        if (!"vencimiento".equalsIgnoreCase(motivo)) {
            throw new IllegalArgumentException("La renovación solo está permitida por vencimiento en esta versión.");
        }

        Titular titular = titularRepository.findByNumeroDocumento(licenciaFormDTO.getDocumentoTitular());
        if (titular == null)
            throw new TitularNoEncontradoException("No se encontró un titular con el número de documento: " + licenciaFormDTO.getDocumentoTitular());

        LicenciaActiva licenciaAnterior = titular.getLicenciaActiva();
        if (licenciaAnterior == null) throw new LicenciaNoEncontradaException("No hay licencia activa para renovar.");
        if (licenciaAnterior.getFechaVencimiento().isAfter(LocalDate.now()) && "vencimiento".equalsIgnoreCase(motivo))
            throw new IllegalArgumentException("La licencia aún no está vencida, no se puede renovar.");

        Usuario usuario = usuarioRepository.findByNumeroDocumento("11999888");
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No se encontró un usuario con el número de documento: 11999888");
        }

        LicenciaExpirada expirada = new LicenciaExpirada();
        expirada.setTitular(titular);
        expirada.setUsuario(licenciaAnterior.getUsuario());
        expirada.setObservaciones(licenciaAnterior.getObservaciones());
        expirada.setClases(licenciaAnterior.getClases());
        expirada.setFechaEmision(licenciaAnterior.getFechaEmision());
        expirada.setFechaVencimiento(licenciaAnterior.getFechaVencimiento());

        titular.setLicenciaActiva(null);
        licenciaAnterior.setTitular(null);

        licenciaExpiradaRepository.save(expirada);

        licenciaActivaRepository.delete(licenciaAnterior);

        licenciaActivaRepository.flush();

        LocalDate fechaEmision = LocalDate.now();
        LocalDate fechaVencimiento = calcularFechaVencimiento(titular);

        LicenciaActiva nuevaLicencia = new LicenciaActiva();
        nuevaLicencia.setTitular(titular);
        nuevaLicencia.setUsuario(usuario);
        nuevaLicencia.setObservaciones(licenciaFormDTO.getObservaciones());
        nuevaLicencia.setClases(licenciaFormDTO.getClases());
        nuevaLicencia.setFechaEmision(fechaEmision);
        nuevaLicencia.setFechaVencimiento(fechaVencimiento);

        titular.setLicenciaActiva(nuevaLicencia);

        licenciaActivaRepository.save(nuevaLicencia);
        titularRepository.save(titular);

        LicenciaEmitidaDTO dto = licenciaEmitidaMapper.entityToDto(nuevaLicencia);
        dto.setDocumentoTitular(titular.getNumeroDocumento());
        dto.setDocumentoUsuario(usuario.getNumeroDocumento());
        return dto;
    }

    @Override
    public LicenciasVencidasDTO obtenerLicenciasVencidasEntre(LocalDate desde, LocalDate hasta) {
        List<LicenciaExpiradaDTO> expiradas = licenciaExpiradaRepository
                .findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(desde, hasta)
                .stream()
                .map(licenciaExpiradaMapper::entityToDto)
                .collect(Collectors.toList());

        List<LicenciaActivaDTO> activasVencidas = licenciaActivaRepository
                .findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(desde, hasta)
                .stream()
                .map(licenciaActivaMapper::entityToDto)
                .collect(Collectors.toList());

        LicenciasVencidasDTO resultado = new LicenciasVencidasDTO();
        resultado.setExpiradas(expiradas);
        resultado.setActivasVencidas(activasVencidas);
        return resultado;
    }

    @Override
    public List<LicenciaActivaDTO> buscarLicenciasPorCriterios(String nombre, String apellido, String grupoFactor, Boolean esDonante) {
        Specification<LicenciaActiva> spec = null;

        if (nombre != null && !nombre.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("titular").get("nombre"), nombre));
        }
        if (apellido != null && !apellido.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("titular").get("apellido"), apellido));
        }
        if (grupoFactor != null && !grupoFactor.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("titular").get("grupoFactor"), grupoFactor));
        }
        if (esDonante != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("titular").get("donanteOrganos"), esDonante));
        }

        List<LicenciaActiva> licencias = licenciaActivaRepository.findAll(spec);
        return licencias.stream()
                .map(licenciaActivaMapper::entityToDto)
                .collect(Collectors.toList());
    }

    private LocalDate calcularFechaVencimiento(Titular titular) {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = titular.getFechaNacimiento();
        int vigencia = calcularVigencia(titular);

        LocalDate proximoCumple = nacimiento.withYear(hoy.getYear());
        if (!proximoCumple.isAfter(hoy)) proximoCumple = proximoCumple.plusYears(1);

        long diasHastaCumple = ChronoUnit.DAYS.between(hoy, proximoCumple);
        if (diasHastaCumple > 31) vigencia--;

        return proximoCumple.plusYears(vigencia);
    }

    private double calcularCostoEmision(String clases, Titular titular) {
        int vigencia = calcularVigencia(titular);
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
                    case 5:
                        return 40.0;
                    case 4:
                        return 30.0;
                    case 3:
                        return 25.0;
                    case 1:
                        return 20.0;
                }
                break;
            case "C":
                switch (vigencia) {
                    case 5:
                        return 47.0;
                    case 4:
                        return 35.0;
                    case 3:
                        return 30.0;
                    case 1:
                        return 23.0;
                }
                break;
            case "D":
                switch (vigencia) {
                    case 5:
                        return 50.0;
                    case 4:
                        return 40.0;
                    case 3:
                        return 35.0;
                    case 1:
                        return 28.0;
                }
                break;
            case "E":
                switch (vigencia) {
                    case 5:
                        return 59.0;
                    case 4:
                        return 44.0;
                    case 3:
                        return 39.0;
                    case 1:
                        return 29.0;
                }
                break;
        }
        throw new IllegalArgumentException("Clase o vigencia no válida");
    }

    private int calcularVigencia(Titular titular) {
        int edad = titular.calcularEdad();

        boolean tuvoLicencia = false;
        if (licenciaActivaRepository.findByTitular_NumeroDocumento(titular.getNumeroDocumento()) != null) {
            tuvoLicencia = true;
        } else {
            if (!licenciaExpiradaRepository.findByTitular_NumeroDocumento(titular.getNumeroDocumento()).isEmpty()) {
                tuvoLicencia = true;
            }
        }

        if (edad < 21) {
            return tuvoLicencia ? 3 : 1;
        } else if (edad <= 46) {
            return 5;
        } else if (edad <= 60) {
            return 4;
        } else if (edad <= 70) {
            return 3;
        } else {
            return 1;
        }
    }

    private void validarFormularioLicencia(LicenciaFormDTO form) {
        if (form.getDocumentoTitular() == null || form.getDocumentoTitular().isBlank())
            throw new IllegalArgumentException("El documento del titular es obligatorio");
        if (form.getClases() == null || form.getClases().isBlank())
            throw new IllegalArgumentException("Debe especificar al menos una clase");
        if (!form.getDocumentoTitular().matches("\\d{7,9}"))
            throw new IllegalArgumentException("El documento debe tener entre 7 y 9 dígitos numéricos");

        Titular titular = titularRepository.findByNumeroDocumento(form.getDocumentoTitular());
        if (titular == null)
            throw new TitularNoEncontradoException("No se encontró un titular con el número de documento: " + form.getDocumentoTitular());

        int edad = titular.calcularEdad();
        String[] clases = form.getClases().trim().split("\\s+");

        for (String clase : clases) {
            boolean esProfesional = clase.equals("C") || clase.equals("D") || clase.equals("E");
            if (esProfesional) {
                if (edad < 21) throw new IllegalArgumentException("Debe tener al menos 21 años para clase " + clase);

                boolean tieneLicenciaProfesional = false;
                LicenciaActiva activa = licenciaActivaRepository.findByTitular_NumeroDocumento(titular.getNumeroDocumento());
                if (activa != null && activa.getClases() != null && activa.getClases().matches(".*[CDE].*")) {
                    tieneLicenciaProfesional = true;
                } else {
                    var expiradas = licenciaExpiradaRepository.findByTitular_NumeroDocumento(titular.getNumeroDocumento());
                    tieneLicenciaProfesional = expiradas.stream().anyMatch(l -> l.getClases() != null && l.getClases().matches(".*[CDE].*"));
                }
                if (!tieneLicenciaProfesional && edad > 65)
                    throw new IllegalArgumentException("Edad máxima para primera licencia profesional (" + clase + ") es 65 años.");

                boolean tieneLicenciaB = false;
                LocalDate fechaEmisionB = null;
                LicenciaActiva licenciaB = licenciaActivaRepository.findByTitular_NumeroDocumento(titular.getNumeroDocumento());
                if (licenciaB != null && licenciaB.getClases() != null && licenciaB.getClases().contains("B")) {
                    tieneLicenciaB = true;
                    fechaEmisionB = licenciaB.getFechaEmision();
                } else {
                    var expiradas = licenciaExpiradaRepository.findByTitular_NumeroDocumento(titular.getNumeroDocumento());
                    for (var l : expiradas) {
                        if (l.getClases() != null && l.getClases().contains("B")) {
                            tieneLicenciaB = true;
                            if (fechaEmisionB == null || l.getFechaEmision().isBefore(fechaEmisionB)) {
                                fechaEmisionB = l.getFechaEmision();
                            }
                        }
                    }
                }
                if (!tieneLicenciaB)
                    throw new IllegalArgumentException("Debe poseer licencia B para tramitar clase " + clase);
                if (fechaEmisionB == null || fechaEmisionB.isAfter(LocalDate.now().minusYears(1)))
                    throw new IllegalArgumentException("Debe tener al menos 1 año de antigüedad con licencia B para clase " + clase);
            } else {
                if (edad < 17) throw new IllegalArgumentException("Debe tener al menos 17 años para clase " + clase);
            }
        }
    }
}