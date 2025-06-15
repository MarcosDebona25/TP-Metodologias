package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LicenciaDTO {
    private Long numero;
    private Long titularId;
    private List<String> clases;
    private String observaciones;
}