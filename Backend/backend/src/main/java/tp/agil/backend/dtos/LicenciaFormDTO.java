package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class LicenciaFormDTO {
        private String documentoTitular; // dni del titular de la licencia
        private String clases;
        private String observaciones;
}