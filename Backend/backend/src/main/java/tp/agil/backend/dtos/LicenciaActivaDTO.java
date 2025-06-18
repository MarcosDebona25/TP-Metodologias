package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class LicenciaActivaDTO {
    private Long dniTitular;
    private String nombreTitular;
    private String apellidoTitular;
    private String clases;
    private String domicilioTitular;
    private LocalDate fechaEmisionLicencia;
    private LocalDate fechaVencimientoLicencia;
    private String grupoFactor;
    private String donanteOrganos;
    private String observacionesLicencia;
}