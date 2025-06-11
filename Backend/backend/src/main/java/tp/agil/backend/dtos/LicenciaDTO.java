package tp.agil.backend.dtos;

import tp.agil.backend.entities.EstadoLicencia;

import java.time.LocalDate;

/**
 * @author Marcos Debona
 */

public class LicenciaDTO {
    private String tipoClase;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private EstadoLicencia estadoLicencia;
    private String observaciones;
    private Long titularId;
    private Long usuarioId;
}