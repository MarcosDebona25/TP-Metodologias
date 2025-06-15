package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import tp.agil.backend.entities.EstadoLicencia;

import java.time.LocalDate;

/**
 * @author Marcos Debona
 */

@Getter
@Setter
public class LicenciaDTO {
    private String tipoClase;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private EstadoLicencia estadoLicencia;
    private String observaciones;
    private Long titularId;
    private Long usuarioId;
}