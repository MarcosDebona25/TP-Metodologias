package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ComprobanteDTO {
    private String nombreTitular;
    private String apellidoTitular;
    private String clases;
    private Double costosEmision;
    private Double costosAdministrativos;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEmisionComprobante;
}