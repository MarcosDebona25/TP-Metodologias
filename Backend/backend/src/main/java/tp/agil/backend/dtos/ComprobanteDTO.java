package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComprobanteDTO {
    private String nombreTitular;
    private String apellidoTitular;
    private String clases;
    private Double costosEmision;
    private Double costosAdministrativos;
}