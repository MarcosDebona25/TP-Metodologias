package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class LicenciaActivaDTO {
    //private Long numero;
    private String documentoTitular;
    private String nombreTitular;
    private String apellidoTitular;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimientoTitular;
    private String clases;
    private String domicilioTitular;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEmisionLicencia;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimientoLicencia;
    private String grupoFactor;
    private String donanteOrganos;
    private String observacionesLicencia;
}